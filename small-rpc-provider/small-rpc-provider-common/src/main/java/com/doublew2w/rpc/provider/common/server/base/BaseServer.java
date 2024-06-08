package com.doublew2w.rpc.provider.common.server.base;

import com.doublew2w.rpc.codec.RpcDecoder;
import com.doublew2w.rpc.codec.RpcEncoder;
import com.doublew2w.rpc.provider.common.handler.RpcProviderHandler;
import com.doublew2w.rpc.provider.common.server.api.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 基础服务
 *
 * @author: DoubleW2w
 * @date: 2024/6/6 3:03
 * @project: small-rpc
 */
@Slf4j
public class BaseServer implements Server {
  /** 主机域名或者IP地址 */
  protected String host = "127.0.0.1";

  /** 端口号 */
  protected int port = 27110;

  // 存储的是实体类关系
  protected Map<String, Object> handlerMap = new HashMap<>();

  public BaseServer(String serverAddress) {
    if (!StringUtils.isBlank(serverAddress)) {
      String[] serverArray = serverAddress.split(":");
      this.host = serverArray[0];
      this.port = Integer.parseInt(serverArray[1]);
    }
  }

  @Override
  public void startNettyServer() {
    // 配置服务器的NIO线程组
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 128)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                  ch.pipeline()
                      .addLast(new RpcDecoder())
                      .addLast(new RpcEncoder())
                      .addLast(new RpcProviderHandler(handlerMap));
                }
              })
          .childOption(ChannelOption.SO_KEEPALIVE, true);
      // 绑定端口，等待同步成功
      ChannelFuture f = b.bind(host, port).sync();
      log.info("Server started on host:{} port {}", host, port);
      // 等待服务器监听端口关闭
      f.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      log.error("RPC Server start error", e);
      Thread.currentThread().interrupt();
    } finally {
      // 优雅退出，释放线程池资源
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
