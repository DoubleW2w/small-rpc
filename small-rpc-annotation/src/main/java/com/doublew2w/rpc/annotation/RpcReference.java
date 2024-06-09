package com.doublew2w.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 服务消费者注解
 *
 * @author: DoubleW2w
 * @date: 2024/6/5 16:11
 * @project: small-rpc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {
  /** 版本号 */
  String version() default "1.0.0";

  /** 注册中心类型, 目前的类型包含：zookeeper、nacos、etcd、consul */
  String registryType() default "zookeeper";

  /** 注册地址 */
  String registryAddress() default "127.0.0.1:2181";

  /** 负载均衡类型，默认基于ZK的一致性Hash */
  String loadBalanceType() default "zkconsistenthash";

  /** 序列化类型，目前的类型包含：protostuff、kryo、json、jdk、hessian2、fst */
  String serializationType() default "protostuff";

  /** 超时时间，默认5s */
  long timeout() default 5000;

  /** 是否异步执行 */
  boolean async() default false;

  /** 是否单向调用 */
  boolean oneway() default false;

  /**
   * 代理的类型
   * <li>jdk:jdk代理
   * <li>javassist: javassist代理
   * <li>cglib: cglib代理
   */
  String proxy() default "jdk";

  /** 服务分组，默认为空 */
  String group() default "";
}
