package com.doublew2w.rpc.common.scanner.reference;

import com.doublew2w.rpc.annotation.RpcReference;
import com.doublew2w.rpc.common.scanner.ClassScanner;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

/**
 * {@code @RpcReference}注解扫描器
 *
 * @author: DoubleW2w
 * @date: 2024/6/5 22:20
 * @project: small-rpc
 */
@Slf4j
public class RpcReferenceScanner extends ClassScanner {
  /**
   * 扫描指定包下的类，并筛选使用@RpcService注解标注的类
   *
   * @param scanPackage 指定包
   * @return 扫描结果
   * @throws Exception 异常
   */
  public static Map<String, Object> doScannerWithRpcReferenceAnnotationFilter(String scanPackage)
      throws Exception {
    Map<String, Object> handlerMap = new HashMap<>(8);
    List<String> classNameList = getClassNameList(scanPackage);
    if (CollectionUtils.isEmpty(classNameList)) {
      return handlerMap;
    }
    classNameList.forEach(
        (className) -> {
          try {
            Class<?> clazz = Class.forName(className);
            Field[] declaredFields = clazz.getDeclaredFields();
            Stream.of(declaredFields)
                .forEach(
                    (field) -> {
                      RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                      if (rpcReference != null) {
                        log.info("当前标注了@RpcReference注解的字段名称===>>> " + field.getName());
                        log.info("@RpcReference注解上标注的属性信息如下：");
                        log.info("version===>>> " + rpcReference.version());
                        log.info("group===>>> " + rpcReference.group());
                        log.info("registryType===>>> " + rpcReference.registryType());
                        log.info("registryAddress===>>> " + rpcReference.registryAddress());
                      }
                    });
          } catch (Exception e) {
            log.error("scan classes throws exception: {}", e.getMessage(), e);
          }
        });
    return handlerMap;
  }
}
