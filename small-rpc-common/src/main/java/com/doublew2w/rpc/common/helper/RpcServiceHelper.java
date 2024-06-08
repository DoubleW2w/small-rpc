package com.doublew2w.rpc.common.helper;

/**
 * @author: DoubleW2w
 * @date: 2024/6/9 0:50
 * @project: small-rpc
 */
public class RpcServiceHelper {
  /**
   * 拼接rpc服务提供者的key
   *
   * @param serviceName 服务名称
   * @param serviceVersion 服务版本
   * @param group 服务分组
   * @return 服务名称#服务版本号#服务分组
   */
  public static String buildServiceKey(String serviceName, String serviceVersion, String group) {
    return  String.join("#", serviceName, serviceVersion, group);
  }
}
