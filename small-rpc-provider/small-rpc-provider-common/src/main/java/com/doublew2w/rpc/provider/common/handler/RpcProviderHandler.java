package com.doublew2w.rpc.provider.common.handler;

import com.alibaba.fastjson2.JSONObject;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.enumeration.RpcType;
import com.doublew2w.rpc.protocol.header.RpcHeader;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: DoubleW2w
 * @date: 2024/6/6 3:04
 * @project: small-rpc
 */
@Slf4j
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
  private final Map<String, Object> handlerMap;

  public RpcProviderHandler(Map<String, Object> handlerMap) {
    this.handlerMap = handlerMap;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg)
      throws Exception {
    log.info("RPC提供者收到的数据为====>>> " + JSONObject.toJSONString(msg));
    log.info("handlerMap中存放的数据如下所示：");
    for (Map.Entry<String, Object> entry : handlerMap.entrySet()) {
      log.info(entry.getKey() + " === " + entry.getValue());
    }
    RpcHeader header = msg.getHeader();
    RpcRequest request = msg.getBody();

    // 将header中的消息类型设置为响应类型的消息
    header.setMsgType((byte) RpcType.RESPONSE.getType());
    // 构建响应协议数据
    RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
    RpcResponse response = new RpcResponse();
    response.setResult("数据交互成功");
    response.setAsync(request.isAsync());
    response.setOneway(request.isOneway());
    responseRpcProtocol.setHeader(header);
    responseRpcProtocol.setBody(response);
    // 直接返回数据
    ctx.writeAndFlush(responseRpcProtocol);
  }
}
