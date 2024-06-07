package com.doublew2w.rpc.protocol.base;


import java.io.Serializable;
import lombok.Data;

/**
 * 消息体基础类
 *
 * @author: DoubleW2w
 * @date: 2024/6/7 2:05
 * @project: small-rpc
 */
@Data
public class RpcMessage implements Serializable {
  /** 是否单向发送 */
  private boolean oneway;

  /** 是否异步调用 */
  private boolean async;
}
