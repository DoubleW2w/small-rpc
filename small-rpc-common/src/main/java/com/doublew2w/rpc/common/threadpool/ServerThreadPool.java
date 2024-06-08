package com.doublew2w.rpc.common.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务线程池
 *
 * @author: DoubleW2w
 * @date: 2024/6/9 0:53
 * @project: small-rpc
 */
public class ServerThreadPool {
  private static final ThreadPoolExecutor threadPoolExecutor;

  static {
    threadPoolExecutor =
        new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536));
  }

  public static void submit(Runnable task) {
    threadPoolExecutor.submit(task);
  }

  public static void shutdown() {
    threadPoolExecutor.shutdown();
  }
}
