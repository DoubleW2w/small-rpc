package com.doublew2w.rpc.common.scanner.server;

import com.doublew2w.rpc.annotation.RpcService;
import com.doublew2w.rpc.common.scanner.ClassScanner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
    if (CollectionUtils.isEmpty(classNameList)) {
      return handlerMap;
    }
    classNameList.forEach(
        (className) -> {
          try {
            Class<?> clazz = Class.forName(className);
            RpcService rpcService = clazz.getAnnotation(RpcService.class);
            if (rpcService != null) {
              String serviceName = getServiceName(rpcService);
              String key = serviceName.concat(rpcService.version()).concat(rpcService.group());
              handlerMap.put(key, clazz.getDeclaredConstructor().newInstance());
            }
          } catch (Exception e) {
            log.error("scan classes throws exception: {}", e.getMessage(), e);
          }
        });
    return handlerMap;
  }

  /** 获取serviceName */
  private static String getServiceName(RpcService rpcService) {
    // 优先使用interfaceClass
    Class<?> clazz = rpcService.interfaceClass();
    if (clazz == void.class) {
      return rpcService.interfaceClassName();
    }
    String serviceName = clazz.getName();
    if (StringUtils.isBlank(serviceName)) {
      serviceName = rpcService.interfaceClassName();
    }
    return serviceName;
  }
}
