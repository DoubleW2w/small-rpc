package com.doublew2w.rpc.test.consumer.codec.init;

import com.doublew2w.rpc.codec.RpcDecoder;
import com.doublew2w.rpc.codec.RpcEncoder;
import com.doublew2w.rpc.test.consumer.codec.handler.RpcTestConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: DoubleW2w
 * @date: 2024/6/8 22:26
 * @project: small-rpc
 */
public class RpcTestConsumerInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    socketChannel
        .pipeline()
        .addLast(new RpcEncoder())
        .addLast(new RpcDecoder())
        .addLast(new RpcTestConsumerHandler());
  }
}
