package com.binchencoder.elasticsearch.dbj.model;

import com.binchencoder.elasticsearch.dbj.dto.AiImageLabelResultDto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author: HuXiao
 * @Description:
 * @Date: 19:33 2021/7/30
 */
@Data
@Document(indexName = "iot_image_sample_" + "#{@env}")
public class AiLabelModel implements Serializable {

  @Id
  @Field(type = FieldType.Text)
  private String id;

  @Field(type = FieldType.Keyword)
  private String brandCode;

  @Field(type = FieldType.Keyword)
  private String deviceNo;

  @Field(type = FieldType.Keyword)
  private String deviceSerial;

  @Field(type = FieldType.Keyword)
  private String deviceCode;

  @Field(type = FieldType.Keyword)
  private String room;

  @Field(type = FieldType.Text, analyzer = "ik_max_word")
  private String address;

  @Field(type = FieldType.Keyword)
  private String imageUrl;

  @Field(type = FieldType.Text, analyzer = "label_type_analyzer")
  private String labelType;

  @Field(type = FieldType.Keyword)
  private String warningLevel;

  @Field(type = FieldType.Nested)
  private List<AiImageLabelResultDto> result;

  @Field(type = FieldType.Nested)
  private List<String> tags;

  @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
  private Date alarmTime;

  @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  @Field(type = FieldType.Keyword)
  private String version;
}
