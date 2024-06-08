package com.doublew2w.rpc.serialization.api;

/**
 * 序列化接口：负责序列化与反序列化
 *
 * @author: DoubleW2w
 * @date: 2024/6/8 9:43
 * @project: small-rpc
 */
public interface Serialization {
  /** 序列化 */
  <T> byte[] serialize(T obj);

  /** 反序列化 */
  <T> T deserialize(byte[] data, Class<T> cls);
}
