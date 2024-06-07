package com.doublew2w.rpc.protocol.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议的类型
 *
 * @author: DoubleW2w
 * @date: 2024/6/7 2:06
 * @project: small-rpc
 */
@AllArgsConstructor
@Getter
public enum RpcType {
  /** 请求消息 */
  REQUEST(1),
  /** 响应消息 */
  RESPONSE(2),
  /** 心跳数据 */
  HEARTBEAT(3);

  private final int type;

  public static RpcType findByType(int type) {
    for (RpcType rpcType : RpcType.values()) {
      if (rpcType.getType() == type) {
        return rpcType;
      }
    }
    return null;
  }
}
