package com.doublew2w.rpc.test.consumer.codec;

import com.doublew2w.rpc.test.consumer.codec.init.RpcTestConsumerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 测试消费端
 *
 * @author: DoubleW2w
 * @date: 2024/6/8 22:25
 * @project: small-rpc
 */
@Slf4j
public class RpcConsumerTest {

  @Test
  public void testConsumerConnectProvider() throws InterruptedException {
    Bootstrap bootstrap = new Bootstrap();
    EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    try {
      bootstrap
          .group(eventLoopGroup)
          .channel(NioSocketChannel.class)
          .handler(new RpcTestConsumerInitializer());
      bootstrap.connect("127.0.0.1", 27880).sync();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      Thread.sleep(2000);
      eventLoopGroup.shutdownGracefully();
    }
  }
}
