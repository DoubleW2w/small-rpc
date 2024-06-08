package com.doublew2w.rpc.protocol.request;


import com.doublew2w.rpc.protocol.base.RpcMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: DoubleW2w
 * @date: 2024/6/7 2:07
 * @project: small-rpc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest extends RpcMessage {
  private static final long serialVersionUID = 5555776886650396129L;

  /** 类名称 */
  private String className;

  /** 方法名称 */
  private String methodName;

  /** 参数类型数组 */
  private Class<?>[] parameterTypes;

  /** 参数数组 */
  private Object[] parameters;

  /** 版本号 */
  private String version;

  /** 服务分组 */
  private String group;
}
