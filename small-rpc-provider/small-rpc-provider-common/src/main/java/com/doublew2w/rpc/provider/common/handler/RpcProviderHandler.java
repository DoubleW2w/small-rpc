package com.doublew2w.rpc.provider.common.handler;

import cn.hutool.core.util.StrUtil;
import com.doublew2w.rpc.common.helper.RpcServiceHelper;
import com.doublew2w.rpc.common.threadpool.ServerThreadPool;
import com.doublew2w.rpc.constants.RpcConstants;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.enumeration.RpcStatus;
import com.doublew2w.rpc.protocol.enumeration.RpcType;
import com.doublew2w.rpc.protocol.header.RpcHeader;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

/**
 * @author: DoubleW2w
 * @date: 2024/6/6 3:04
 * @project: small-rpc
 */
@Slf4j
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
  /** 服务提供者 */
  private final Map<String, Object> handlerMap;

  /** 调用采用哪种类型调用真实方法 */
  private final String reflectType;

  public RpcProviderHandler(Map<String, Object> handlerMap, String reflectType) {
    this.handlerMap = handlerMap;
    this.reflectType = reflectType;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg)
      throws Exception {
    ServerThreadPool.submit(
        () -> {
          // 消息头
          RpcHeader header = msg.getHeader();
          header.setMsgType((byte) RpcType.RESPONSE.getType());
          log.debug("Receive request " + header.getRequestId());

          // 消息体
          RpcRequest request = msg.getBody();
          RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
          RpcResponse response = new RpcResponse();
          try {
            // 成功调用
            Object result = handle(request);
            response.setResult(result);
            response.setAsync(request.isAsync());
            response.setOneway(request.isOneway());
            header.setStatus((byte) RpcStatus.SUCCESS.getCode());
          } catch (Throwable t) {
            // 失败
            response.setError(t.toString());
            header.setStatus((byte) RpcStatus.FAIL.getCode());
            log.error("RPC Server handle request error", t);
          }
          // 返回
          responseRpcProtocol.setHeader(header);
          responseRpcProtocol.setBody(response);
          ctx.writeAndFlush(responseRpcProtocol)
              .addListener(
                  (ChannelFutureListener)
                      channelFuture ->
                          log.debug("Send response for request " + header.getRequestId()));
        });
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("server caught exception", cause);
    ctx.close();
  }

  private Object handle(RpcRequest request) throws Throwable {
    // 构造key
    String serviceKey =
        RpcServiceHelper.buildServiceKey(
            request.getClassName(), request.getVersion(), request.getGroup());
    // 获取对应实例
    Object serviceBean = handlerMap.get(serviceKey);
    if (serviceBean == null) {
      throw new RuntimeException(
          StrUtil.format(
              "service not exist: {}:{}", request.getClassName(), request.getMethodName()));
    }

    Class<?> serviceClass = serviceBean.getClass();
    String methodName = request.getMethodName();
    Class<?>[] parameterTypes = request.getParameterTypes();
    Object[] parameters = request.getParameters();

    log.debug(serviceClass.getName());
    log.debug(methodName);
    if (parameterTypes != null && parameters.length > 0) {
      for (Class<?> parameterType : parameterTypes) {
        log.debug(parameterType.getName());
      }
    }

    if (parameters != null && parameters.length > 0) {
      for (Object parameter : parameters) {
        log.debug(parameter.toString());
      }
    }
    return invokeMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
  }

  /** 调用方法 */
  private Object invokeMethod(
      Object serviceBean,
      Class<?> serviceClass,
      String methodName,
      Class<?>[] parameterTypes,
      Object[] parameters)
      throws Throwable {
    switch (this.reflectType) {
      case RpcConstants.REFLECT_TYPE_JDK:
        return this.invokeJDKMethod(
            serviceBean, serviceClass, methodName, parameterTypes, parameters);
      case RpcConstants.REFLECT_TYPE_CGLIB:
        return this.invokeCGLibMethod(
            serviceBean, serviceClass, methodName, parameterTypes, parameters);
      default:
        throw new IllegalArgumentException("not support reflect type");
    }
  }

  /**
   * CGLib代理方式
   *
   * @param serviceBean 实例对象
   * @param serviceClass 目标类
   * @param methodName 目标方法
   * @param parameterTypes 目标参数类型列表
   * @param parameters 目标参数列表
   * @return 方法返回结果
   * @throws Throwable 异常
   */
  private Object invokeCGLibMethod(
      Object serviceBean,
      Class<?> serviceClass,
      String methodName,
      Class<?>[] parameterTypes,
      Object[] parameters)
      throws Throwable {
    // Cglib reflect
    log.info("use cglib reflect type invoke method...");
    FastClass serviceFastClass = FastClass.create(serviceClass);
    FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
    return serviceFastMethod.invoke(serviceBean, parameters);
  }

  /**
   * jdk反射方式调用方法
   *
   * @param serviceBean 实例对象
   * @param serviceClass 目标类
   * @param methodName 目标方法
   * @param parameterTypes 目标参数类型列表
   * @param parameters 目标参数列表
   * @return 方法返回结果
   * @throws Throwable 异常
   */
  private Object invokeJDKMethod(
      Object serviceBean,
      Class<?> serviceClass,
      String methodName,
      Class<?>[] parameterTypes,
      Object[] parameters)
      throws Throwable {
    log.info("Start invoking method...:");
    Method method = serviceClass.getMethod(methodName, parameterTypes);
    method.setAccessible(true);
    return method.invoke(serviceBean, parameters);
  }
}
