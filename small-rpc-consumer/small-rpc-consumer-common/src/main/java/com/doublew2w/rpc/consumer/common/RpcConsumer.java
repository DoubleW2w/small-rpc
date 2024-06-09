package com.doublew2w.rpc.consumer.common;

import com.doublew2w.rpc.consumer.common.handler.RpcConsumerHandler;
import com.doublew2w.rpc.consumer.common.init.RpcConsumerInitializer;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务消费者
 *
 * @author: DoubleW2w
 * @date: 2024/6/9 17:17
 * @project: small-rpc
 */
@Slf4j
public class RpcConsumer {
  /** 用于引导和配置 Netty 客户端。 */
  private final Bootstrap bootstrap;

  /** 管理并调度用于处理 I/O 操作的线程组 */
  private final EventLoopGroup eventLoopGroup;

  /** 服务消费者缓存 */
  private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();

  private static volatile RpcConsumer instance;

  private RpcConsumer() {
    bootstrap = new Bootstrap();
    eventLoopGroup = new NioEventLoopGroup(4);
    bootstrap
        .group(eventLoopGroup)
        .channel(NioSocketChannel.class)
        .handler(new RpcConsumerInitializer());
  }

  /**
   * 双重校验锁-单例模式
   *
   * @return RpcConsumer实例
   */
  public static RpcConsumer getInstance() {
    if (instance == null) {
      synchronized (RpcConsumer.class) {
        if (instance == null) {
          instance = new RpcConsumer();
        }
      }
    }
    return instance;
  }

  public void close() {
    eventLoopGroup.shutdownGracefully();
  }

  //修改返回数据的类型
  public Object sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception {
    // TODO 暂时写死，后续在引入注册中心时，从注册中心获取
    String serviceAddress = "127.0.0.1";
    int port = 27880;
    String key = serviceAddress.concat("_").concat(String.valueOf(port));
    RpcConsumerHandler handler = handlerMap.get(key);
    // 缓存中无RpcClientHandler
    if (handler == null) {
      handler = getRpcConsumerHandler(serviceAddress, port);
      handlerMap.put(key, handler);
    } else if (!handler.getChannel().isActive()) { // 缓存中存在RpcClientHandler，但不活跃
      handler.close();
      handler = getRpcConsumerHandler(serviceAddress, port);
      handlerMap.put(key, handler);
    }
    return handler.sendRequest(protocol);
  }

  /** 创建连接并返回RpcClientHandler */
  private RpcConsumerHandler getRpcConsumerHandler(String serviceAddress, int port)
      throws InterruptedException {
    ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();
    channelFuture.addListener(
        (ChannelFutureListener)
            listener -> {
              if (channelFuture.isSuccess()) {
                log.info("connect rpc server {} on port {} success.", serviceAddress, port);
              } else {
                log.error("connect rpc server {} on port {} failed.", serviceAddress, port);
                log.error(channelFuture.cause().getMessage(), channelFuture.cause());
                eventLoopGroup.shutdownGracefully();
              }
            });
    return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
  }
}
