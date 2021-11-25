package com.binchencoder.elasticsearch.model;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author: chenbin
 * @date: 2021/11/25 下午12:59
 */
@Data
@Builder
public class Warning implements Serializable {

  private String name;

  private List<ChildWarning> children;

}
