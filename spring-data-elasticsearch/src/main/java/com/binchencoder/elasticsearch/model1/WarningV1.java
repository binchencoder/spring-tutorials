package com.binchencoder.elasticsearch.model1;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author: chenbin
 * @date: 2021/11/25 下午12:59
 */
@Data
@Builder
public class WarningV1 implements Serializable {

  private String name;

  private Number score;

}
