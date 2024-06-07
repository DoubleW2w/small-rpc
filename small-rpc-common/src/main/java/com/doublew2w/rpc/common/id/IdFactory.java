package com.doublew2w.rpc.common.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: DoubleW2w
 * @date: 2024/6/7 2:03
 * @project: small-rpc
 */
public class IdFactory {
  private static final AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

  public static Long getId() {
    return REQUEST_ID_GEN.incrementAndGet();
  }
}
