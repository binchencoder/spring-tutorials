package com.binchencoder.elasticsearch.model3;

import java.io.Serializable;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

/**
 * @author: chenbin
 * @date: 2021/11/25 下午12:59
 */
@Data
@Builder
public class WarningV3 implements Serializable {

  private String name;

  private Map<String, Float> children;

}
