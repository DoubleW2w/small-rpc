package com.doublew2w.rpc.test.common.scanner;

import com.doublew2w.rpc.annotation.RpcReference;

/**
 * @author: DoubleW2w
 * @date: 2024/6/6 0:59
 * @project: small-rpc
 */
public class ConsumerBizLogicServiceImpl implements ConsumerBizLogicService {
  @RpcReference(
      registryType = "zookeeper",
      registryAddress = "127.0.0.1:2181",
      version = "1.0.0",
      group = "doublew")
  private ProviderService providerService;
}
