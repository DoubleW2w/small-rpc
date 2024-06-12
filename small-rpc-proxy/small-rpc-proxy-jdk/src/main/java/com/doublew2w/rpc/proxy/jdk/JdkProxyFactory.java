package com.doublew2w.rpc.proxy.jdk;

import com.doublew2w.rpc.proxy.api.consumer.Consumer;
import com.doublew2w.rpc.proxy.api.object.ObjectProxy;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理工厂
 *
 * @author: DoubleW2w
 * @date: 2024/6/12 15:34
 * @project: small-rpc
 */
public class JdkProxyFactory<T> {
  /** 服务版本号 */
  private String serviceVersion;

  /** 服务分组 */
  private String serviceGroup;

  /** 超时时间，默认15s */
  private long timeout = 15000;

  /** 服务消费者 */
  private Consumer consumer;

  /** 序列化类型 */
  private String serializationType;

  /** 是否异步调用 */
  private boolean async;

  /** 是否单向调用 */
  private boolean oneway;

  public JdkProxyFactory(
      String serviceVersion,
      String serviceGroup,
      String serializationType,
      long timeout,
      Consumer consumer,
      boolean async,
      boolean oneway) {
    this.serviceVersion = serviceVersion;
    this.timeout = timeout;
    this.serviceGroup = serviceGroup;
    this.consumer = consumer;
    this.serializationType = serializationType;
    this.async = async;
    this.oneway = oneway;
  }

  @SuppressWarnings("unchecked")
  public <T> T getProxy(Class<T> clazz) {
    return (T)
        Proxy.newProxyInstance(
            clazz.getClassLoader(),
            new Class<?>[] {clazz},
            new ObjectProxy<>(
                clazz,
                serviceVersion,
                serviceGroup,
                serializationType,
                timeout,
                consumer,
                async,
                oneway));
  }
}
