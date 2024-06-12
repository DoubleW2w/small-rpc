package com.doublew2w.rpc.test.registry;

import com.doublew2w.rpc.protocol.meta.ServiceMeta;
import com.doublew2w.rpc.registry.api.RegistryService;
import com.doublew2w.rpc.registry.api.config.RegistryConfig;
import com.doublew2w.rpc.registry.zookeeper.ZookeeperRegistryService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author: DoubleW2w
 * @date: 2024/6/13 6:42
 * @project: small-rpc
 */
@Slf4j
public class ZookeeperRegistryTest {
  private static RegistryService registryService;

  private static ServiceMeta serviceMeta;

  @BeforeAll
  public static void init() throws Exception {
    RegistryConfig registryConfig = new RegistryConfig("127.0.0.1:2181", "zookeeper");
    registryService = new ZookeeperRegistryService();
    registryService.init(registryConfig);
    serviceMeta =
        new ServiceMeta(
            ZookeeperRegistryTest.class.getName(), "1.0.0", "127.0.0.1", 8080, "double");
  }

  @Test
  public void testRegister() throws Exception {
    registryService.register(serviceMeta);
  }

  @Test
  public void testUnRegister() throws Exception {
    registryService.unRegister(serviceMeta);
  }

  @Test
  public void testDiscovery() throws Exception {
    registryService.discovery(RegistryService.class.getName(), "double".hashCode());
  }

  @Test
  public void testDestroy() throws IOException {
    registryService.destroy();
  }
}
