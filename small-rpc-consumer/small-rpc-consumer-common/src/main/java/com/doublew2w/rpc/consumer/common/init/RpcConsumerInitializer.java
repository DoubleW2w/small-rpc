package com.doublew2w.rpc.consumer.common.init;

import com.doublew2w.rpc.codec.RpcDecoder;
import com.doublew2w.rpc.codec.RpcEncoder;
import com.doublew2w.rpc.consumer.common.handler.RpcConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: DoubleW2w
 * @date: 2024/6/9 17:21
 * @project: small-rpc
 */
public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ch.pipeline()
        .addLast(new RpcEncoder())
        .addLast(new RpcDecoder())
        .addLast(new RpcConsumerHandler());
  }
}
