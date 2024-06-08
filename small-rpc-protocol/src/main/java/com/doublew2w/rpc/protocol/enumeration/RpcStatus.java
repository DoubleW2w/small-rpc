package com.doublew2w.rpc.protocol.enumeration;

import lombok.Getter;

/**
 * RPC服务状态
 *
 * @author: DoubleW2w
 * @date: 2024/6/9 0:54
 * @project: small-rpc
 */
@Getter
public enum RpcStatus {
  /** 成功 */
  SUCCESS(0),
  /** 失败 */
  FAIL(1);

  private final int code;

  RpcStatus(int code) {
    this.code = code;
  }
}
