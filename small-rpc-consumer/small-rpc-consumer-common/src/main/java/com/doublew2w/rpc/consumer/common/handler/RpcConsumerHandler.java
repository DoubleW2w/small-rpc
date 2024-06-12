package com.doublew2w.rpc.consumer.common.handler;

import com.alibaba.fastjson2.JSONObject;
import com.doublew2w.rpc.consumer.common.context.RpcContext;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.header.RpcHeader;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.protocol.response.RpcResponse;
import com.doublew2w.rpc.proxy.api.future.RpcFuture;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC消费者处理器
 *
 * @author: DoubleW2w
 * @date: 2024/6/9 17:21
 * @project: small-rpc
 */
@Slf4j
public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
  /** 通道 */
  @Getter private volatile Channel channel;

  /** 远程地址 */
  @Getter private SocketAddress remotePeer;

  /** 存储请求ID与RpcResponse协议的映射关系 */
  private Map<Long, RpcFuture> pendingRPC = new ConcurrentHashMap<>();

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    this.remotePeer = this.channel.remoteAddress();
  }

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    super.channelRegistered(ctx);
    this.channel = ctx.channel();
  }

  /** 当服务提供者发送 RpcProtocol<RpcResponse> 类型的消息时，就会调用该方法 */
  @Override
  protected void channelRead0(
      ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol)
      throws Exception {
    if (protocol == null) {
      return;
    }
    log.info("服务消费者接收到的数据===>>>{}", JSONObject.toJSONString(protocol));
    RpcHeader header = protocol.getHeader();
    long requestId = header.getRequestId();
    RpcFuture rpcFuture = pendingRPC.remove(requestId);
    if (rpcFuture != null) {
      rpcFuture.done(protocol);
    }
  }

  /** 服务消费者向服务提供者发送请求 */
  public RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, boolean async, boolean oneway) {
    log.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
    return oneway
        ? this.sendRequestOneway(protocol)
        : async ? sendRequestAsync(protocol) : this.sendRequestSync(protocol);
  }

  public void close() {
    channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  /**
   * 获取请求对应的Future
   *
   * @param protocol 请求
   * @return Future
   */
  private RpcFuture getRpcFuture(RpcProtocol<RpcRequest> protocol) {
    RpcFuture rpcFuture = new RpcFuture(protocol);
    RpcHeader header = protocol.getHeader();
    long requestId = header.getRequestId();
    pendingRPC.put(requestId, rpcFuture);
    return rpcFuture;
  }

  /**
   * 发起同步调用请求
   *
   * @param protocol 请求
   * @return 请求的Future
   */
  private RpcFuture sendRequestSync(RpcProtocol<RpcRequest> protocol) {
    RpcFuture rpcFuture = this.getRpcFuture(protocol);
    channel.writeAndFlush(protocol);
    return rpcFuture;
  }

  /**
   * 发起单向调用请求
   *
   * @param protocol 请求
   * @return 请求的Future
   */
  private RpcFuture sendRequestOneway(RpcProtocol<RpcRequest> protocol) {
    channel.writeAndFlush(protocol);
    return null;
  }

  /**
   * 发起异步调用请求
   *
   * @param protocol 请求
   * @return 请求的Future
   */
  private RpcFuture sendRequestAsync(RpcProtocol<RpcRequest> protocol) {
    RpcFuture rpcFuture = this.getRpcFuture(protocol);
    // 如果是异步调用，则将RpcFuture放入RpcContext
    RpcContext.getContext().setRPCFuture(rpcFuture);
    channel.writeAndFlush(protocol);
    return null;
  }
}
