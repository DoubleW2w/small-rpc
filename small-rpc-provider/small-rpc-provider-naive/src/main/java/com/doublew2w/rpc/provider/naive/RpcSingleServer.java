package com.doublew2w.rpc.provider.naive;

import com.doublew2w.rpc.common.scanner.server.RpcServiceScanner;
import com.doublew2w.rpc.provider.common.server.base.BaseServer;
import lombok.extern.slf4j.Slf4j;

/**
 * 以Java原生方式启动启动Rpc
 *
 * @author: DoubleW2w
 * @date: 2024/6/6 3:12
 * @project: small-rpc
 */
@Slf4j
public class RpcSingleServer extends BaseServer {

  public RpcSingleServer(String serverAddress, String scanPackage) {
    //调用父类构造方法
    super(serverAddress);
    try {
      this.handlerMap = RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(scanPackage);
    } catch (Exception e) {
      log.error("RPC Server init error", e);
    }
  }
}
