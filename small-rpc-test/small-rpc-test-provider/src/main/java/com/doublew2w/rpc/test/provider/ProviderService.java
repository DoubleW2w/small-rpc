package com.doublew2w.rpc.test.provider;

import com.doublew2w.rpc.annotation.RpcService;
import com.doublew2w.rpc.test.api.DemoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: DoubleW2w
 * @date: 2024/6/6 3:20
 * @project: small-rpc
 */
@RpcService(
    interfaceClass = DemoService.class,
    interfaceClassName = "com.doublew2w.rpc.test.api.DemoService",
    group = "double")
@Slf4j
public class ProviderService implements DemoService {

  @Override
  public String hello(String name) {
    log.info("调用hello方法传入的参数为===>>>{}", name);
    return "hello " + name;
  }
}
