package com.doublew2w.rpc.consumer.common.handler;

import com.alibaba.fastjson2.JSONObject;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.protocol.response.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.SocketAddress;
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
  private volatile Channel channel;
  private SocketAddress remotePeer;

  public Channel getChannel() {
    return channel;
  }

  public SocketAddress getRemotePeer() {
    return remotePeer;
  }

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

  @Override
  protected void channelRead0(
      ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol)
      throws Exception {
    log.info("服务消费者接收到的数据===>>>{}", JSONObject.toJSONString(protocol));
  }

  /** 服务消费者向服务提供者发送请求 */
  public void sendRequest(RpcProtocol<RpcRequest> protocol) {
    log.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
    channel.writeAndFlush(protocol);
  }

  public void close() {
    channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }
}
