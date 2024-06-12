package com.doublew2w.rpc.test.consumer;

import com.doublew2w.rpc.consumer.RpcClient;
import com.doublew2w.rpc.test.api.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 测试Java原生启动服务消费者
 *
 * @author: DoubleW2w
 * @date: 2024/6/12 17:35
 * @project: small-rpc
 */
@Slf4j
public class RpcConsumerNativeTest {

  @Test
  public void testRpcClientCreate() {
    // 构造服务消费客户端
    RpcClient rpcClient = new RpcClient("1.0.0", "double", "jdk", 3000, false, false);
    // 根据接口类生成了代理对象
    DemoService demoService = rpcClient.create(DemoService.class);
    String result = demoService.hello("double");
    log.info("返回的结果数据===>>> " + result);
    rpcClient.shutdown();
  }
}
