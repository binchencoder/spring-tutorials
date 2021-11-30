package com.binchencoder.elasticsearch.model;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author: chenbin
 * @date: 2021/11/25 下午12:59
 */
@Data
@Builder
public class Warning implements Serializable {

  private String name;

  @Field(type = FieldType.Nested, includeInParent = true)
  private List<ChildWarning> children;

}
