package com.binchencoder.elasticsearch.model1;

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
@Document(indexName = "ai_warning_v1")
public class AiWarningV1Model implements Serializable {

  @Id
  @Field(type = FieldType.Text)
  private String id;

//  @Field(type = FieldType.Nested, includeInParent = true)
  @Field(type = FieldType.Nested)
  private List<WarningV1> warnings;

}
