package com.doublew2w.rpc.common.scanner.reference;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存在{@code @RpcReference}注解字段代理实例的上下文
 *
 * @author: DoubleW2w
 * @date: 2024/6/5 22:14
 * @project: small-rpc
 */
public class RpcReferenceContext {
  private static volatile Map<String, Object> instance;

  static {
    instance = new ConcurrentHashMap<>();
  }

  public static void put(String key, Object value) {
    instance.put(key, value);
  }

  public static Object get(String key) {
    return instance.get(key);
  }

  public static Object remove(String key) {
    return instance.remove(key);
  }
}
