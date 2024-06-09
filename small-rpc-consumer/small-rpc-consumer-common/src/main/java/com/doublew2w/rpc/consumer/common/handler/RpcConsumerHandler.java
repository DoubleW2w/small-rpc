package com.doublew2w.rpc.consumer.common.handler;

import com.alibaba.fastjson2.JSONObject;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.header.RpcHeader;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.protocol.response.RpcResponse;
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
  private Map<Long, RpcProtocol<RpcResponse>> pendingResponse = new ConcurrentHashMap<>();

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
    if (protocol == null) {
      return;
    }
    log.info("服务消费者接收到的数据===>>>{}", JSONObject.toJSONString(protocol));
    RpcHeader header = protocol.getHeader();
    long requestId = header.getRequestId();
    pendingResponse.put(requestId, protocol);
  }

  /** 服务消费者向服务提供者发送请求 */
  public Object sendRequest(RpcProtocol<RpcRequest> protocol) {
    log.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
    channel.writeAndFlush(protocol);
    RpcHeader header = protocol.getHeader();
    long requestId = header.getRequestId();
    // 异步转同步
    while (true) {
      RpcProtocol<RpcResponse> responseRpcProtocol = pendingResponse.remove(requestId);
      if (responseRpcProtocol != null) {
        return responseRpcProtocol.getBody().getResult();
      }
    }
  }

  public void close() {
    channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }
}
