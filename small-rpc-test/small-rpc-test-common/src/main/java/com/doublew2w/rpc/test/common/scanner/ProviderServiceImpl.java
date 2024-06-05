package com.doublew2w.rpc.test.common.scanner;

import com.doublew2w.rpc.annotation.RpcService;

/**
 * @author: DoubleW2w
 * @date: 2024/6/6 1:00
 * @project: small-rpc
 */
@RpcService(
    interfaceClass = ProviderService.class,
    interfaceClassName = "com.doublew2w.rpc.test.common.scanner.ProviderService",
    version = "1.0.0",
    group = "doublew")
public class ProviderServiceImpl implements ProviderService {}
