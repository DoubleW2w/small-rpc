package com.doublew2w.rpc.consumer.common.context;

import com.doublew2w.rpc.consumer.common.future.RpcFuture;

/**
 * 保存RPC上下文
 *
 * @author: DoubleW2w
 * @date: 2024/6/10 15:14
 * @project: small-rpc
 */
public class RpcContext {
  private RpcContext() {}

  /** RpcContext实例 */
  private static final RpcContext AGENT = new RpcContext();

  /**
   * 存放RPCFuture的InheritableThreadLocal
   *
   * <p>InheritableThreadLocal 可以使子线程继承父线程的值
   */
  private static final InheritableThreadLocal<RpcFuture> RPC_FUTURE_INHERITABLE_THREAD_LOCAL =
      new InheritableThreadLocal<>();

  /**
   * 获取上下文
   *
   * @return RPC服务的上下文信息
   */
  public static RpcContext getContext() {
    return AGENT;
  }

  /** 将RPCFuture保存到线程的上下文 */
  public void setRPCFuture(RpcFuture rpcFuture) {
    RPC_FUTURE_INHERITABLE_THREAD_LOCAL.set(rpcFuture);
  }

  /** 获取RPCFuture */
  public RpcFuture getRPCFuture() {
    return RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get();
  }

  /** 移除RPCFuture */
  public void removeRPCFuture() {
    RPC_FUTURE_INHERITABLE_THREAD_LOCAL.remove();
  }
}
