package com.doublew2w.rpc.test.protocol;

import com.doublew2w.rpc.protocol.RpcProtocol;
import com.doublew2w.rpc.protocol.header.RpcHeader;
import com.doublew2w.rpc.protocol.header.RpcHeaderFactory;
import com.doublew2w.rpc.protocol.request.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author: DoubleW2w
 * @date: 2024/6/7 2:16
 * @project: small-rpc
 */
@Slf4j
public class RpcProtocolTest {
  @Test
  public void testGetRpcProtocol(){
    RpcHeader header = RpcHeaderFactory.getRequestHeader("jdk");
    RpcRequest body = new RpcRequest();
    body.setOneway(false);
    body.setAsync(false);
    body.setClassName("com.doublew2w.rpc.protocol.RpcProtocol");
    body.setMethodName("helloWorld");
    body.setGroup("doublew2w");
    body.setParameters(new Object[]{"doublew2w"});
    body.setParameterTypes(new Class[]{String.class});
    body.setVersion("1.0.0");
    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
    protocol.setBody(body);
    protocol.setHeader(header);

    log.info("{}",protocol);

  }
}
