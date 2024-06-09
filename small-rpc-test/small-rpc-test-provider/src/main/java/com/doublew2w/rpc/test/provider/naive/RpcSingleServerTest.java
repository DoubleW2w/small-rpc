package com.doublew2w.rpc.test.provider.naive;

import com.doublew2w.rpc.provider.naive.RpcSingleServer;
import org.junit.jupiter.api.Test;

/**
 * 测试Java原生启动RPC
 *
 * @author: DoubleW2w
 * @date: 2024/6/6 3:17
 * @project: small-rpc
 */
public class RpcSingleServerTest {

  @Test
  public void testRpcSingleServerScanner() {
    // 扫描RpcService注解类
    RpcSingleServer rpcSingleServer =
        new RpcSingleServer("127.0.0.1:27880", "com.doublew2w.rpc.test", "jdk");
    // 启动服务器
    rpcSingleServer.startNettyServer();
  }
}
