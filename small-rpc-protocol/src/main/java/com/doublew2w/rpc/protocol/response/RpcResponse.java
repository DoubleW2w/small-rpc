package com.doublew2w.rpc.protocol.response;

import com.doublew2w.rpc.protocol.base.RpcMessage;
import lombok.Data;

/**
 * @author: DoubleW2w
 * @date: 2024/6/7 2:10
 * @project: small-rpc
 */
@Data
public class RpcResponse extends RpcMessage {
  private static final long serialVersionUID = 425335064405584525L;

  private String error;
  private Object result;
}
