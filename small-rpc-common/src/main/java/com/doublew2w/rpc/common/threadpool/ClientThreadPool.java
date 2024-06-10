package com.doublew2w.rpc.common.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: DoubleW2w
 * @date: 2024/6/10 18:05
 * @project: small-rpc
 */
@Slf4j
public class ClientThreadPool {
  private static ThreadPoolExecutor threadPoolExecutor;

  static {
    threadPoolExecutor =
        new ThreadPoolExecutor(
            16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
  }

  public static void submit(Runnable task) {
    threadPoolExecutor.submit(task);
  }

  public static void shutdown() {
    threadPoolExecutor.shutdown();
  }
}
