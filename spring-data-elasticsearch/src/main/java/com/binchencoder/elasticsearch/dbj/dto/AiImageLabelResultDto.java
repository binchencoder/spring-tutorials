package com.binchencoder.elasticsearch.dbj.dbj.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author: HuXiao
 * @Description:
 * @Date: 15:50 2021/7/15
 */
@Data
public class AiImageLabelResultDto implements Serializable {

  private String label;

  private Number score;

  private Integer left;

  private Integer top;

  private Integer right;

  private Integer botton;
}
