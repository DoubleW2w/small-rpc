package com.doublew2w.rpc.proxy.api.future;

import com.doublew2w.rpc.common.threadpool.ClientThreadPool;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.protocol.response.RpcResponse;
import com.doublew2w.rpc.proxy.api.callbak.AsyncRpcCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC框架获取异步结果的自定义Future
 *
 * @author: DoubleW2w
 * @date: 2024/6/10 0:15
 * @project: small-rpc
 */
@Slf4j
public class RpcFuture extends CompletableFuture<Object> {
  /** 同步锁 */
  private Sync sync;

  /** 请求 */
  private RpcProtocol<RpcRequest> requestRpcProtocol;

  /** 响应 */
  private RpcProtocol<RpcResponse> responseRpcProtocol;

  /** 开始时间 */
  private long startTime;

  /** 响应超时时长 */
  private long responseTimeThreshold = 5000;

  /** 异步回调接口 */
  private List<AsyncRpcCallback> pendingCallbacks = new ArrayList<>();

  /** */
  private ReentrantLock lock = new ReentrantLock();

  public RpcFuture(RpcProtocol<RpcRequest> requestRpcProtocol) {
    this.sync = new Sync();
    this.requestRpcProtocol = requestRpcProtocol;
    this.startTime = System.currentTimeMillis();
  }

  @Override
  public boolean isDone() {
    return sync.isDone();
  }

  @Override
  public Object get() throws InterruptedException, ExecutionException {
    sync.acquire(-1);
    if (this.responseRpcProtocol != null) {
      return this.responseRpcProtocol.getBody().getResult();
    } else {
      return null;
    }
  }

  @Override
  public Object get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
    if (success) {
      if (this.responseRpcProtocol != null) {
        return this.responseRpcProtocol.getBody().getResult();
      } else {
        return null;
      }
    } else {
      throw new RuntimeException(
          "Timeout exception. Request id: "
              + this.requestRpcProtocol.getHeader().getRequestId()
              + ". Request class name: "
              + this.requestRpcProtocol.getBody().getClassName()
              + ". Request method: "
              + this.requestRpcProtocol.getBody().getMethodName());
    }
  }

  @Override
  public boolean isCancelled() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    throw new UnsupportedOperationException();
  }

  /**
   * 当服务消费者接收到服务提供者响应的结果数据时，就会调用done()方法，
   *
   * <p>并传入RpcResponse类型的协议对象，此时会唤醒阻塞的线程获取响应的结果数据
   *
   * @param responseRpcProtocol 响应信息
   */
  public void done(RpcProtocol<RpcResponse> responseRpcProtocol) {
    this.responseRpcProtocol = responseRpcProtocol;
    sync.release(1);
    invokeCallbacks();
    // Threshold
    long responseTime = System.currentTimeMillis() - startTime;
    if (responseTime > this.responseTimeThreshold) {
      log.warn(
          "Service response time is too slow. Request id = "
              + responseRpcProtocol.getHeader().getRequestId()
              + ". Response Time = "
              + responseTime
              + "ms");
    }
  }

  /**
   * 添加回调接口
   *
   * @param callback 回调接口
   * @return 结果
   */
  public RpcFuture addCallback(AsyncRpcCallback callback) {
    lock.lock();
    try {
      if (isDone()) {
        runCallback(callback);
      } else {
        this.pendingCallbacks.add(callback);
      }
    } finally {
      lock.unlock();
    }
    return this;
  }

  /** 触发回调接口 */
  private void invokeCallbacks() {
    lock.lock();
    try {
      for (final AsyncRpcCallback callback : pendingCallbacks) {
        runCallback(callback);
      }
    } finally {
      lock.unlock();
    }
  }

  private void runCallback(final AsyncRpcCallback callback) {
    final RpcResponse res = this.responseRpcProtocol.getBody();
    ClientThreadPool.submit(
        () -> {
          if (!res.isError()) {
            callback.onSuccess(res.getResult());
          } else {
            callback.onException(
                new RuntimeException("Response error", new Throwable(res.getError())));
          }
        });
  }

  static class Sync extends AbstractQueuedSynchronizer {

    private static final long serialVersionUID = 1L;

    // future status
    private final int done = 1;
    private final int pending = 0;

    /**
     * 尝试获取同步状态
     *
     * @param acquires the acquire argument. This value is always the one passed to an acquire
     *     method, or is the value saved on entry to a condition wait. The value is otherwise
     *     uninterpreted and can represent anything you like.
     * @return
     */
    protected boolean tryAcquire(int acquires) {
      return getState() == done;
    }

    /**
     * 尝试释放同步状态
     *
     * @param releases the release argument. This value is always the one passed to a release
     *     method, or the current state value upon entry to a condition wait. The value is otherwise
     *     uninterpreted and can represent anything you like.
     * @return
     */
    protected boolean tryRelease(int releases) {
      if (getState() == pending) {
        if (compareAndSetState(pending, done)) {
          return true;
        }
      }
      return false;
    }

    public boolean isDone() {
      getState();
      return getState() == done;
    }
  }
}
