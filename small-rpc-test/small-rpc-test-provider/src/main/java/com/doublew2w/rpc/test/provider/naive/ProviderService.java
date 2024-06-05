package com.doublew2w.rpc.test.provider.naive;

import com.doublew2w.rpc.annotation.RpcService;

/**
 * @author: DoubleW2w
 * @date: 2024/6/6 3:20
 * @project: small-rpc
 */
@RpcService(
    interfaceClass = DemoService.class,
    interfaceClassName = "com.doublew2w.rpc.test.provider.naive.DemoService")
public class ProviderService implements DemoService {

}