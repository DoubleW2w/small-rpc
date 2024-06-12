package com.doublew2w.rpc.consumer;

import com.doublew2w.rpc.consumer.common.RpcConsumer;
import com.doublew2w.rpc.proxy.api.ProxyFactory;
import com.doublew2w.rpc.proxy.api.async.IAsyncObjectProxy;
import com.doublew2w.rpc.proxy.api.config.ProxyConfig;
import com.doublew2w.rpc.proxy.api.object.ObjectProxy;
import com.doublew2w.rpc.proxy.jdk.JdkProxyFactory;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务消费客户端
 *
 * @author: DoubleW2w
 * @date: 2024/6/12 17:24
 * @project: small-rpc
 */
@Slf4j
@NoArgsConstructor
public class RpcClient {
  /** 服务版本 */
  private String serviceVersion;

  /** 服务分组 */
  private String serviceGroup;

  /** 序列化类型 */
  private String serializationType;

  /** 超时时间 */
  private long timeout;

  /** 是否异步调用 */
  private boolean async;

  /** 是否单向调用 */
  private boolean oneway;

  public RpcClient(
      String serviceVersion,
      String serviceGroup,
      String serializationType,
      long timeout,
      boolean async,
      boolean oneway) {
    this.serviceVersion = serviceVersion;
    this.timeout = timeout;
    this.serviceGroup = serviceGroup;
    this.serializationType = serializationType;
    this.async = async;
    this.oneway = oneway;
  }

  public <T> T create(Class<T> interfaceClass) {
    ProxyFactory proxyFactory = new JdkProxyFactory<T>();
    proxyFactory.init(
        new ProxyConfig<>(
            interfaceClass,
            serviceVersion,
            serviceGroup,
            timeout,
            RpcConsumer.getInstance(),
            serializationType,
            async,
            oneway));
    return proxyFactory.getProxy(interfaceClass);
  }

  public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
    return new ObjectProxy<T>(
        interfaceClass,
        serviceVersion,
        serviceGroup,
        serializationType,
        timeout,
        RpcConsumer.getInstance(),
        async,
        oneway);
  }

  public void shutdown() {
    RpcConsumer.getInstance().close();
  }
}
