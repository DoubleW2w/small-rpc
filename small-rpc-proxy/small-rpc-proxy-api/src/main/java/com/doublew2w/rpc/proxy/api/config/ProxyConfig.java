package com.doublew2w.rpc.proxy.api.config;

import com.doublew2w.rpc.proxy.api.consumer.Consumer;
import java.io.Serializable;
import lombok.*;

/**
 * 动态代理配置类
 *
 * @author: DoubleW2w
 * @date: 2024/6/13 0:27
 * @project: small-rpc
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProxyConfig<T> implements Serializable {
  private static final long serialVersionUID = 6648940252795742398L;

  /** 接口的Class实例 */
  private Class<T> clazz;

  /** 服务版本号 */
  private String serviceVersion;

  /** 服务分组 */
  private String serviceGroup;

  /** 超时时间 */
  private long timeout;

  /** 消费者接口 */
  private Consumer consumer;

  /** 序列化类型 */
  private String serializationType;

  /** 是否异步调用 */
  private boolean async;

  /** 是否单向调用 */
  private boolean oneway;
}
