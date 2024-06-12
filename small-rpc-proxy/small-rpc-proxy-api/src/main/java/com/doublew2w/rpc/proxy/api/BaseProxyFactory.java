package com.doublew2w.rpc.proxy.api;

import com.doublew2w.rpc.proxy.api.config.ProxyConfig;
import com.doublew2w.rpc.proxy.api.object.ObjectProxy;

/**
 * @author: DoubleW2w
 * @date: 2024/6/13 0:31
 * @project: small-rpc
 */
public abstract class BaseProxyFactory<T> implements ProxyFactory {
  protected ObjectProxy<T> objectProxy;

  @Override
  public <T> void init(ProxyConfig<T> proxyConfig) {
    this.objectProxy =
        new ObjectProxy(
            proxyConfig.getClazz(),
            proxyConfig.getServiceVersion(),
            proxyConfig.getServiceGroup(),
            proxyConfig.getSerializationType(),
            proxyConfig.getTimeout(),
            proxyConfig.getConsumer(),
            proxyConfig.isAsync(),
            proxyConfig.isOneway());
  }
}
