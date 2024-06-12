package com.doublew2w.rpc.proxy.api.object;

import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.header.RpcHeaderFactory;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.proxy.api.consumer.Consumer;
import com.doublew2w.rpc.proxy.api.future.RpcFuture;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: DoubleW2w
 * @date: 2024/6/12 17:02
 * @project: small-rpc
 */
@Slf4j
public class ObjectProxy<T> implements InvocationHandler {
  /** 接口的Class对象 */
  private Class<T> clazz;

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

  public ObjectProxy(Class<T> clazz) {
    this.clazz = clazz;
  }

  public ObjectProxy(
      Class<T> clazz,
      String serviceVersion,
      String serviceGroup,
      String serializationType,
      long timeout,
      Consumer consumer,
      boolean async,
      boolean oneway) {
    this.clazz = clazz;
    this.serviceVersion = serviceVersion;
    this.timeout = timeout;
    this.serviceGroup = serviceGroup;
    this.consumer = consumer;
    this.serializationType = serializationType;
    this.async = async;
    this.oneway = oneway;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // 如果是Object类
    if (Object.class == method.getDeclaringClass()) {
      return handleObjectMethods(proxy, method, args);
    }
    RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
    // 请求-消息头
    requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType));
    // 请求-消息体
    RpcRequest request = new RpcRequest();
    request.setVersion(this.serviceVersion);
    request.setClassName(method.getDeclaringClass().getName());
    request.setMethodName(method.getName());
    request.setParameterTypes(method.getParameterTypes());
    request.setGroup(this.serviceGroup);
    request.setParameters(args);
    request.setAsync(async);
    request.setOneway(oneway);
    requestRpcProtocol.setBody(request);

    // Debug
    log.debug(method.getDeclaringClass().getName());
    log.debug(method.getName());

    if (method.getParameterTypes() != null && method.getParameterTypes().length > 0) {
      for (int i = 0; i < method.getParameterTypes().length; ++i) {
        log.debug(method.getParameterTypes()[i].getName());
      }
    }

    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; ++i) {
        log.debug(args[i].toString());
      }
    }
    // 发送请求
    RpcFuture rpcFuture = this.consumer.sendRequest(requestRpcProtocol);
    return rpcFuture == null
        ? null
        : timeout > 0 ? rpcFuture.get(timeout, TimeUnit.MILLISECONDS) : rpcFuture.get();
  }

  private Object handleObjectMethods(Object proxy, Method method, Object[] args) {
    String name = method.getName();
    if ("equals".equals(name)) {
      return proxy == args[0];
    } else if ("hashCode".equals(name)) {
      return System.identityHashCode(proxy);
    } else if ("toString".equals(name)) {
      return proxy.getClass().getName()
          + "@"
          + Integer.toHexString(System.identityHashCode(proxy))
          + ", with InvocationHandler "
          + this;
    } else {
      throw new IllegalStateException(String.valueOf(method));
    }
  }
}
