package com.binchencoder.elasticsearch.model1;

import java.io.Serializable;
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
public class WarningV1 implements Serializable {

  private String name;

//  @Field(type = FieldType.Float_Range)
  private Integer score;

}
