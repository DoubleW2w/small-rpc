package com.doublew2w.rpc.proxy.api.async;

import com.doublew2w.rpc.proxy.api.future.RpcFuture;

/**
 * 异步代理类
 *
 * @author: DoubleW2w
 * @date: 2024/6/12 23:13
 * @project: small-rpc
 */
public interface IAsyncObjectProxy {
  /**
   * 异步代理对象调用方法
   *
   * @param funcName 方法名称
   * @param args 方法参数
   * @return 封装好的RPCFuture对象
   */
  RpcFuture call(String funcName, Object... args);
}
