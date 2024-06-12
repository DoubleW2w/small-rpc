package com.doublew2w.rpc.protocol.meta;

import java.io.Serializable;
import lombok.*;

/**
 * 服务元数据，注册到注册中心的元数据信息
 *
 * @author: DoubleW2w
 * @date: 2024/6/13 5:13
 * @project: small-rpc
 */
@Data
@AllArgsConstructor
public class ServiceMeta implements Serializable {
  private static final long serialVersionUID = 6289735590272020366L;

  /** 服务名称 */
  private String serviceName;

  /** 服务版本号 */
  private String serviceVersion;

  /** 服务地址 */
  private String serviceAddr;

  /** 服务端口 */
  private int servicePort;

  /** 服务分组 */
  private String serviceGroup;

}
