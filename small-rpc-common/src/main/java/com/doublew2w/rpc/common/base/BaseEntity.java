package com.doublew2w.rpc.common.base;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 基础类
 *
 * @author: DoubleW2w
 * @date: 2024/6/7 2:19
 * @project: small-rpc
 */
public abstract class BaseEntity implements Serializable {
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
