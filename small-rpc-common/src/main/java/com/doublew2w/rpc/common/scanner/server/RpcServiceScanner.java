package com.doublew2w.rpc.common.scanner.server;

import com.doublew2w.rpc.annotation.RpcService;
import com.doublew2w.rpc.common.scanner.ClassScanner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * {@code @RpcService}注解扫描器
 *
 * @author: DoubleW2w
 * @date: 2024/6/5 22:19
 * @project: small-rpc
 */
@Slf4j
public class RpcServiceScanner extends ClassScanner {
  /**
   * 扫描指定包下的类，并筛选使用@RpcService注解标注的类
   *
   * @param scanPackage 指定包
   * @return 类名->类信息
   * @throws Exception 异常
   */
  public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService(
      String scanPackage) throws Exception {
    Map<String, Object> handlerMap = new HashMap<>(8);
    List<String> classNameList = getClassNameList(scanPackage);
    if (classNameList == null || classNameList.isEmpty()) {
      return handlerMap;
    }
    classNameList.forEach(
        (className) -> {
          try {
            Class<?> clazz = Class.forName(className);
            RpcService rpcService = clazz.getAnnotation(RpcService.class);
            if (rpcService != null) {
              log.info("当前标注了@RpcService注解的类实例名称===>>> " + clazz.getName());
              log.info("@RpcService注解上标注的属性信息如下：");
              log.info("interfaceClass===>>> " + rpcService.interfaceClass().getName());
              log.info("interfaceClassName===>>> " + rpcService.interfaceClassName());
              log.info("version===>>> " + rpcService.version());
              log.info("group===>>> " + rpcService.group());
            }
          } catch (Exception e) {
            log.error("scan classes throws exception: {}", e.getMessage(), e);
          }
        });
    return handlerMap;
  }
}
