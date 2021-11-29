package com.binchencoder.elasticsearch.dbj.query;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author: HuXiao
 * @Description:
 * @Date: 12:26 2021/8/3
 */
@Data
@Builder
@ToString
public class AiLabelQuery {

  /**
   * id
   */
  private Long id;

  private String deviceSerial;

  private String address;

  private List<String> labelType;

  private String warningLevel;

  private List<String> tags;

  private String alarmStartTime;

  private String alarmEndTime;

  private Integer page = 1;

  private Integer rows = 10;
}
