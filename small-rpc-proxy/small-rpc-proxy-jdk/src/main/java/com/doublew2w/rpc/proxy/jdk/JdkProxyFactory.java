package com.doublew2w.rpc.proxy.jdk;

import com.doublew2w.rpc.proxy.api.BaseProxyFactory;
import com.doublew2w.rpc.proxy.api.ProxyFactory;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理工厂
 *
 * @author: DoubleW2w
 * @date: 2024/6/12 15:34
 * @project: small-rpc
 */
public class JdkProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getProxy(Class<T> clazz) {
    return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, objectProxy);
  }
}
