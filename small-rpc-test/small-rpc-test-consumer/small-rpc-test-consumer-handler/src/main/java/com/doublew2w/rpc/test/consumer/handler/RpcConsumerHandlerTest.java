package com.doublew2w.rpc.test.consumer.handler;

import com.doublew2w.rpc.consumer.common.RpcConsumer;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.header.RpcHeaderFactory;
import com.doublew2w.rpc.protocol.request.RpcRequest;
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
  public void testRpcConsumerHandler() throws Exception {
    RpcConsumer consumer = RpcConsumer.getInstance();
    Object result = consumer.sendRequest(getRpcRequestProtocol());
    log.info("从服务消费者获取到的数据===>>>:{}", result.toString());
    consumer.close();
  }

  private RpcProtocol<RpcRequest> getRpcRequestProtocol() {
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
}
