package com.doublew2w.rpc.codec;

import com.doublew2w.rpc.serialization.api.Serialization;
import com.doublew2w.rpc.serialization.jdk.JdkSerialization;

/**
 * 实现编解码的接口，提供序列化和反序列化的默认方法
 *
 * @author: DoubleW2w
 * @date: 2024/6/8 9:58
 * @project: small-rpc
 */
public interface RpcCodec {
  default Serialization getJdkSerialization() {
    return new JdkSerialization();
  }
}
