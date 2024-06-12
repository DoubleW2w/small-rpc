package com.doublew2w.rpc.test.consumer.handler;

import com.doublew2w.rpc.consumer.common.RpcConsumer;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.header.RpcHeaderFactory;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.proxy.api.callbak.AsyncRpcCallback;
import com.doublew2w.rpc.proxy.api.future.RpcFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 服务消费者-消息处理器测试类
 *
 * @author: DoubleW2w
 * @date: 2024/6/9 17:47
 * @project: small-rpc
 */
@Slf4j
public class RpcConsumerHandlerTest {

  @Test
  public void testConsumerAsync() throws Exception {
    RpcConsumer consumer = RpcConsumer.getInstance();
    RpcFuture rpcFuture = consumer.sendRequest(getRpcRequestProtocolAsync());
    Object o = rpcFuture.get();
    log.info("从服务消费者获取到的数据===>>>:{}", o.toString());
    consumer.close();
  }

  @Test
  public void testConsumerSync() throws Exception {
    RpcConsumer consumer = RpcConsumer.getInstance();
    RpcFuture rpcFuture = consumer.sendRequest(getRpcRequestProtocolSync());
    log.info("从服务消费者获取到的数据===>>>:{}", rpcFuture.get());
    consumer.close();
  }

  @Test
  public void testConsumerOneway() throws Exception {
    RpcConsumer consumer = RpcConsumer.getInstance();
    consumer.sendRequest(getRpcRequestProtocolOneway());
    log.info("无需返回的数据");
    consumer.close();
  }

  @Test
  public void testCallBack() throws Exception {
    RpcConsumer consumer = RpcConsumer.getInstance();
    RpcFuture rpcFuture = consumer.sendRequest(getRpcRequestProtocolSync());
    rpcFuture.addCallback(
        new AsyncRpcCallback() {
          @Override
          public void onSuccess(Object result) {
            log.info("从服务消费者获取到的数据===>>>" + result);
          }

          @Override
          public void onException(Exception e) {
            log.info("抛出了异常===>>>" + e);
          }
        });
    Thread.sleep(200);
    consumer.close();
  }

  private RpcProtocol<RpcRequest> getRpcRequestProtocolAsync() {
    // 模拟发送数据
    RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
    protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
    RpcRequest request = new RpcRequest();
    request.setClassName("com.doublew2w.rpc.test.api.DemoService");
    request.setGroup("double");
    request.setMethodName("hello");
    request.setParameters(new Object[] {"mynadasdasweeqqwwe"});
    request.setParameterTypes(new Class[] {String.class});
    request.setVersion("1.0.0");
    request.setAsync(true);
    request.setOneway(false);
    protocol.setBody(request);
    return protocol;
  }

  private RpcProtocol<RpcRequest> getRpcRequestProtocolSync() {
    // 模拟发送数据
    RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
    protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
    RpcRequest request = new RpcRequest();
    request.setClassName("com.doublew2w.rpc.test.api.DemoService");
    request.setGroup("double");
    request.setMethodName("hello");
    request.setParameters(new Object[] {"mynadasdasweeqqwwe"});
    request.setParameterTypes(new Class[] {String.class});
    request.setVersion("1.0.0");
    request.setAsync(false);
    request.setOneway(false);
    protocol.setBody(request);
    return protocol;
  }

  private RpcProtocol<RpcRequest> getRpcRequestProtocolOneway() {
    // 模拟发送数据
    RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
    protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
    RpcRequest request = new RpcRequest();
    request.setClassName("com.doublew2w.rpc.test.api.DemoService");
    request.setGroup("double");
    request.setMethodName("hello");
    request.setParameters(new Object[] {"mynadasdasweeqqwwe"});
    request.setParameterTypes(new Class[] {String.class});
    request.setVersion("1.0.0");
    request.setAsync(false);
    request.setOneway(true);
    protocol.setBody(request);
    return protocol;
  }

  public static class ThreadLocalExample {
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);

    public static void main(String[] args) {
      Runnable task =
          () -> {
            System.out.println(
                Thread.currentThread().getName() + " initial value: " + threadLocal.get());
            threadLocal.set(threadLocal.get() + 1);
            System.out.println(
                Thread.currentThread().getName() + " updated value: " + threadLocal.get());
          };

      Thread thread1 = new Thread(task);
      Thread thread2 = new Thread(task);

      thread1.start();
      thread2.start();
    }
  }

  public static class InheritableThreadLocalExample {
    private static InheritableThreadLocal<Integer> inheritableThreadLocal =
        new InheritableThreadLocal<>();

    public static void main(String[] args) {
      inheritableThreadLocal.set(1);

      Runnable task =
          () -> {
            System.out.println(
                Thread.currentThread().getName()
                    + " initial value: "
                    + inheritableThreadLocal.get());
            inheritableThreadLocal.set(inheritableThreadLocal.get() + 1);
            System.out.println(
                Thread.currentThread().getName()
                    + " updated value: "
                    + inheritableThreadLocal.get());
          };

      Thread thread1 = new Thread(task);
      Thread thread2 = new Thread(task);

      thread1.start();
      thread2.start();
    }
  }
}
