package com.doublew2w.rpc.registry.api.config;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 服务注册配置类
 *
 * @author: DoubleW2w
 * @date: 2024/6/13 5:32
 * @project: small-rpc
 */
@Data
@AllArgsConstructor
public class RegistryConfig implements Serializable {
  private static final long serialVersionUID = -7248658103788758893L;

  /** 注册地址 */
  private String registryAddr;

  /** 注册类型 */
  private String registryType;
}
