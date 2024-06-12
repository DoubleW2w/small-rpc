package com.doublew2w.rpc.proxy.api;

import com.doublew2w.rpc.proxy.api.config.ProxyConfig;

/**
 * 动态代理工厂
 *
 * @author: DoubleW2w
 * @date: 2024/6/13 0:30
 * @project: small-rpc
 */
public interface ProxyFactory {
  /** 获取代理对象 */
  <T> T getProxy(Class<T> clazz);

  /** 默认初始化方法 */
  default <T> void init(ProxyConfig<T> proxyConfig) {}
}
