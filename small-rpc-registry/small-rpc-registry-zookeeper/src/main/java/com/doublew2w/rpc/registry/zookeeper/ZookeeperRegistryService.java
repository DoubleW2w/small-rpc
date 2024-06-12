package com.doublew2w.rpc.registry.zookeeper;

import com.doublew2w.rpc.common.helper.RpcServiceHelper;
import com.doublew2w.rpc.protocol.meta.ServiceMeta;
import com.doublew2w.rpc.registry.api.RegistryService;
import com.doublew2w.rpc.registry.api.config.RegistryConfig;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

/**
 * zookeeper 服务实现的服务注册与发现
 *
 * @author: DoubleW2w
 * @date: 2024/6/13 5:33
 * @project: small-rpc
 */
public class ZookeeperRegistryService implements RegistryService {
  /** 初始重试等待时间 */
  public static final int BASE_SLEEP_TIME_MS = 1000;

  /** 重试次数 */
  public static final int MAX_RETRIES = 3;

  /** 基础路径 */
  public static final String ZK_BASE_PATH = "/doublew2w_rpc";

  private ServiceDiscovery<ServiceMeta> serviceDiscovery;

  @Override
  public void init(RegistryConfig registryConfig) throws Exception {
    CuratorFramework client =
        CuratorFrameworkFactory.newClient(
            registryConfig.getRegistryAddr(),
            // 指数退避重试策略
            new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
    client.start();
    // 用于序列化和反序列化服务元数据的JSON序列化器
    JsonInstanceSerializer<ServiceMeta> serializer =
        new JsonInstanceSerializer<>(ServiceMeta.class);
    // 创建并启动一个ServiceDiscovery实例，以实现服务的注册和发现。
    this.serviceDiscovery =
        ServiceDiscoveryBuilder.builder(ServiceMeta.class)
            .client(client)
            .serializer(serializer)
            .basePath(ZK_BASE_PATH)
            .build();
    this.serviceDiscovery.start();
  }

  @Override
  public void register(ServiceMeta serviceMeta) throws Exception {
    ServiceInstance<ServiceMeta> serviceInstance =
        ServiceInstance.<ServiceMeta>builder()
            .name(
                RpcServiceHelper.buildServiceKey(
                    serviceMeta.getServiceName(),
                    serviceMeta.getServiceVersion(),
                    serviceMeta.getServiceGroup()))
            .address(serviceMeta.getServiceAddr())
            .port(serviceMeta.getServicePort())
            .payload(serviceMeta)
            .build();
    serviceDiscovery.registerService(serviceInstance);
  }

  @Override
  public void unRegister(ServiceMeta serviceMeta) throws Exception {
    ServiceInstance<ServiceMeta> serviceInstance =
        ServiceInstance.<ServiceMeta>builder()
            .name(
                RpcServiceHelper.buildServiceKey(
                    serviceMeta.getServiceName(),
                    serviceMeta.getServiceVersion(),
                    serviceMeta.getServiceGroup()))
            .address(serviceMeta.getServiceAddr())
            .port(serviceMeta.getServicePort())
            .payload(serviceMeta)
            .build();
    serviceDiscovery.unregisterService(serviceInstance);
  }

  @Override
  public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
    Collection<ServiceInstance<ServiceMeta>> serviceInstances =
        serviceDiscovery.queryForInstances(serviceName);
    ServiceInstance<ServiceMeta> instance =
        this.selectOneServiceInstance((List<ServiceInstance<ServiceMeta>>) serviceInstances);
    if (instance != null) {
      return instance.getPayload();
    }
    return null;
  }

  @Override
  public void destroy() throws IOException {
    serviceDiscovery.close();
  }

  // 随机挑选一个
  private ServiceInstance<ServiceMeta> selectOneServiceInstance(
      List<ServiceInstance<ServiceMeta>> serviceInstances) {
    if (serviceInstances == null || serviceInstances.isEmpty()) {
      return null;
    }
    Random random = new Random();
    int index = random.nextInt(serviceInstances.size());
    return serviceInstances.get(index);
  }
}
