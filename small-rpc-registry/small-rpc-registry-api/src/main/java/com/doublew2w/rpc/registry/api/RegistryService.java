package com.doublew2w.rpc.registry.api;

import com.doublew2w.rpc.protocol.meta.ServiceMeta;
import com.doublew2w.rpc.registry.api.config.RegistryConfig;
import java.io.IOException;

/**
 * 服务注册与发现接口
 *
 * @author: DoubleW2w
 * @date: 2024/6/13 5:32
 * @project: small-rpc
 */
public interface RegistryService {
  /**
   * 服务注册
   *
   * @param serviceMeta 服务元数据
   * @throws Exception 抛出异常
   */
  void register(ServiceMeta serviceMeta) throws Exception;

  /**
   * 服务取消注册
   *
   * @param serviceMeta 服务元数据
   * @throws Exception 抛出异常
   */
  void unRegister(ServiceMeta serviceMeta) throws Exception;

  /**
   * 服务发现
   *
   * @param serviceName 服务名称
   * @param invokerHashCode HashCode值
   * @return 服务元数据
   * @throws Exception 抛出异常
   */
  ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

  /**
   * 服务销毁
   *
   * @throws IOException 抛出异常
   */
  void destroy() throws IOException;

  /** 默认初始化方法 */
  default void init(RegistryConfig registryConfig) throws Exception {}
}
