package com.binchencoder.elasticsearch.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author: chenbin
 * @date: 2021/11/25 下午12:59
 */
@Data
@Builder
public class ChildWarning implements Serializable {

  private String label;

  private Number score;

}
