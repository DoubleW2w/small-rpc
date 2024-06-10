package com.doublew2w.rpc.consumer.common.callback;

/**
 * 异步回调接口
 *
 * @author: DoubleW2w
 * @date: 2024/6/10 18:09
 * @project: small-rpc
 */
public interface AsyncRpcCallback {
  /** 成功后的回调方法 */
  void onSuccess(Object result);

  /** 异常的回调方法 */
  void onException(Exception e);
}
