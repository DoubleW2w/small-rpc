package com.doublew2w.rpc.protocol;

import com.doublew2w.rpc.protocol.header.RpcHeader;
import java.io.Serializable;
import lombok.*;

/**
 * Rpc协议
 *
 * @author: DoubleW2w
 * @date: 2024/6/7 2:12
 * @project: small-rpc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcProtocol<T> implements Serializable {
  private static final long serialVersionUID = 292789485166173277L;

  /** 消息头 */
  private RpcHeader header;

  /** 消息体 */
  private T body;
}
