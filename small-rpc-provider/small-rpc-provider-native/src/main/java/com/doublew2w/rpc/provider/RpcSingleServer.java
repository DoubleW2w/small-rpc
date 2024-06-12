package com.doublew2w.rpc.provider;

import com.doublew2w.rpc.common.scanner.server.RpcServiceScanner;
import com.doublew2w.rpc.provider.common.server.base.BaseServer;
import java.lang.reflect.Method;
import java.util.Map;
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
  public RpcSingleServer(String serverAddress, String scanPackage, String reflectType) {
    // 调用父类构造方法
    super(serverAddress, reflectType);
    try {
      this.handlerMap =
          RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(scanPackage);
      log.debug("print all RpcService");
      for (Map.Entry<String, Object> entry : handlerMap.entrySet()) {
        Method[] declaredMethods = entry.getValue().getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
          log.debug("key:{} ---> method:{}", entry.getKey(), declaredMethod.getName());
        }
      }
    } catch (Exception e) {
      log.error("RPC Server init error", e);
    }
  }
}
