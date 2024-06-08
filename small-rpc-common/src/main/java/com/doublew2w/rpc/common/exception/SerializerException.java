package com.doublew2w.rpc.common.exception;

/**
 * SerializerException
 *
 * <p>序列化与反序列化异常
 *
 * @author: DoubleW2w
 * @date: 2024/6/8 9:50
 * @project: small-rpc
 */
public class SerializerException extends RuntimeException {
  private static final long serialVersionUID = -6783134254669118520L;

  /**
   * Instantiates a new Serializer exception.
   *
   * @param e the e
   */
  public SerializerException(final Throwable e) {
    super(e);
  }

  /**
   * Instantiates a new Serializer exception.
   *
   * @param message the message
   */
  public SerializerException(final String message) {
    super(message);
  }

  /**
   * Instantiates a new Serializer exception.
   *
   * @param message the message
   * @param throwable the throwable
   */
  public SerializerException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
