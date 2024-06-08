package com.doublew2w.rpc.test.consumer.codec.handler;

import com.alibaba.fastjson2.JSONObject;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.header.RpcHeaderFactory;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC消费者处理器
 *
 * @author: DoubleW2w
 * @date: 2024/6/8 22:29
 * @project: small-rpc
 */
@Slf4j
public class RpcTestConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg)
      throws Exception {
    log.info("服务消费者接收到的数据===>>>{}", JSONObject.toJSONString(msg));
  }

  /**
   * 当通道连接上时，调用该方法
   *
   * @param ctx 通道处理器上下文
   * @throws Exception 异常
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.info("发送数据开始...");
    // 模拟发送数据
    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
    protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
    RpcRequest request =
        RpcRequest.builder()
            .className("com.doublew2w.rpc.test.provider.naive.DemoService")
            .group("double")
            .methodName("helloWorld")
            .parameters(new Object[] {"teteteteteteasdasdas "})
            .parameterTypes(new Class[] {String.class})
            .version("1.0.0")
            .build();
    request.setAsync(false);
    request.setOneway(false);
    protocol.setBody(request);
    log.info("服务消费者发送的数据 ===>>>{}", JSONObject.toJSONString(protocol));
    ctx.writeAndFlush(protocol);
    log.info("发送数据完毕...");
  }
}
