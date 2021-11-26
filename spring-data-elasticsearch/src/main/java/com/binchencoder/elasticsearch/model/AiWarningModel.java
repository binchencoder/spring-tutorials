package com.binchencoder.elasticsearch.model;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author: chenbin
 * @date: 2021/11/25 下午12:36
 */
@Data
@Builder
@Document(indexName = "ai_warning")
public class AiWarningModel implements Serializable {

  @Id
  @Field(type = FieldType.Text)
  private String id;

  @Field(type = FieldType.Nested)
  private List<Warning> warnings;

}
