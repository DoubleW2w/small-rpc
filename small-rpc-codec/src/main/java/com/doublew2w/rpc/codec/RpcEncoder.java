package com.doublew2w.rpc.codec;

import com.doublew2w.rpc.common.utils.SerializationUtils;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.header.RpcHeader;
import com.doublew2w.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 实现RPC编码操作
 *
 * <p>
 *
 * <p>消息头：
 *
 * <p>- 魔数：验证自定义网络传输协议的最基本的校验信息，占据 2 字节空间。
 *
 * <p>- 报文类型：消息的类型，可以分为请求消息、响应消息和心跳消息，占据 1 字节空间。
 *
 * <p>- 状态：消息的状态，占据 1 字节空间。
 *
 * <p>- 消息 ID：消息的唯一标识，占据 8 字节空间。
 *
 * <p>- 序列化类型：数据进行序列化和反序列化的类型标识，暂定 16 字节空间。
 *
 * <p>- 数据长度：标识消息体的数据长度，占据 4 字节空间。
 *
 * <p>消息体：
 *
 * @author: DoubleW2w
 * @date: 2024/6/8 9:59
 * @project: small-rpc
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> implements RpcCodec {

  @Override
  protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf out)
      throws Exception {
    if (null == msg) {
      return;
    }
    // 消息头
    RpcHeader header = msg.getHeader();
    out.writeShort(header.getMagic());
    out.writeByte(header.getMsgType());
    out.writeByte(header.getStatus());
    out.writeLong(header.getRequestId());
    String serializationType = header.getSerializationType();
    // TODO Serialization是扩展点
    Serialization serialization = getJdkSerialization();
    out.writeBytes(
        SerializationUtils.paddingString(serializationType).getBytes(StandardCharsets.UTF_8));

    // 消息体
    byte[] data = serialization.serialize(msg.getBody());
    out.writeInt(data.length);
    out.writeBytes(data);
  }
}
