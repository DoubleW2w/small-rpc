package com.doublew2w.rpc.proxy.api.consumer;

import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.proxy.api.future.RpcFuture;

/**
 * 服务消费者
 *
 * @author: DoubleW2w
 * @date: 2024/6/12 16:59
 * @project: small-rpc
 */
public interface Consumer {
  /** 消费者发送 request 请求 */
  RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception;
}
