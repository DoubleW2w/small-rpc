package com.doublew2w.rpc.provider.common.server.api;

/**
 * 启动RPC服务的接口
 *
 * @author: DoubleW2w
 * @date: 2024/6/6 3:02
 * @project: small-rpc
 */
public interface Server {

  /** 启动Netty服务 */
  void startNettyServer();
}
