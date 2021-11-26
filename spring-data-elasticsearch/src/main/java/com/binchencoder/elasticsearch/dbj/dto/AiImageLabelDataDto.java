package com.binchencoder.elasticsearch.dbj.dbj.dto;

import java.util.List;
import lombok.Data;

/**
 * @author: HuXiao
 * @Description:
 * @Date: 15:50 2021/7/15
 */
@Data
public class AiImageLabelDataDto {

  private List<AiImageLabelResultDto> result;

  private List<String> tags;

  private String version;
}
