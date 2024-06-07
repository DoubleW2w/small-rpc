package com.doublew2w.rpc.protocol.header;

import com.doublew2w.rpc.common.id.IdFactory;
import com.doublew2w.rpc.constants.RpcConstants;
import com.doublew2w.rpc.protocol.enumeration.RpcType;

/**
 * rpc消息头工厂类
 *
 * @author: DoubleW2w
 * @date: 2024/6/7 2:11
 * @project: small-rpc
 */
public class RpcHeaderFactory {
  /**
   * @param serializationType 反序列化类型
   * @return RPC消息头
   */
  public static RpcHeader getRequestHeader(String serializationType) {
    RpcHeader header = new RpcHeader();
    long requestId = IdFactory.getId();
    header.setMagic(RpcConstants.MAGIC);
    header.setRequestId(requestId);
    header.setMsgType((byte) RpcType.REQUEST.getType());
    header.setStatus((byte) 0x1);
    header.setSerializationType(serializationType);
    return header;
  }
}
