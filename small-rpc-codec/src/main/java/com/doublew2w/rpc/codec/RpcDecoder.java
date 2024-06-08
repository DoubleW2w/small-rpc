package com.doublew2w.rpc.codec;

import com.doublew2w.rpc.common.utils.SerializationUtils;
import com.doublew2w.rpc.constants.RpcConstants;
import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.enumeration.RpcType;
import com.doublew2w.rpc.protocol.header.RpcHeader;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import com.doublew2w.rpc.protocol.response.RpcResponse;
import com.doublew2w.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import java.util.List;

/**
 * 实现RPC解码操作
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
 * @date: 2024/6/8 9:58
 * @project: small-rpc
 */
public class RpcDecoder extends ByteToMessageDecoder implements RpcCodec {
  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (in.readableBytes() < RpcConstants.HEADER_TOTAL_LEN) {
      return;
    }
    in.markReaderIndex();
    // 16bit - 2字节 - 魔数
    short magic = in.readShort();
    if (magic != RpcConstants.MAGIC) {
      throw new IllegalArgumentException("magic number is illegal, " + magic);
    }
    // 报文类型
    byte msgType = in.readByte();
    // 状态
    byte status = in.readByte();
    // 消息ID
    long requestId = in.readLong();
    // 序列化类型，并去0操作
    ByteBuf serializationTypeByteBuf =
        in.readBytes(SerializationUtils.MAX_SERIALIZATION_TYPE_COUNT);
    String serializationType =
        SerializationUtils.subString(serializationTypeByteBuf.toString(CharsetUtil.UTF_8));
    // 数据长度 4字节
    int dataLength = in.readInt();

    // 如果可读字节数小于消息长度，重置读索引并返回
    if (in.readableBytes() < dataLength) {
      in.resetReaderIndex();
      return;
    }

    byte[] data = new byte[dataLength];
    in.readBytes(data);

    RpcType msgTypeEnum = RpcType.findByType(msgType);
    if (msgTypeEnum == null) {
      return;
    }

    // 消息头部分
    RpcHeader header = new RpcHeader();
    header.setMagic(magic);
    header.setStatus(status);
    header.setRequestId(requestId);
    header.setMsgType(msgType);
    header.setSerializationType(serializationType);
    header.setMsgLen(dataLength);

    // TODO Serialization是扩展点
    Serialization serialization = getJdkSerialization();
    switch (msgTypeEnum) {
      case REQUEST:
        RpcRequest request = serialization.deserialize(data, RpcRequest.class);
        if (request != null) {
          RpcProtocol<RpcRequest> protocol =
              RpcProtocol.<RpcRequest>builder().header(header).body(request).build();
          out.add(protocol);
        }
        break;
      case RESPONSE:
        RpcResponse response = serialization.deserialize(data, RpcResponse.class);
        if (response != null) {
          RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
          protocol.setHeader(header);
          protocol.setBody(response);
          out.add(protocol);
        }
        break;
      case HEARTBEAT:
        // TODO
        break;
    }
  }
}
