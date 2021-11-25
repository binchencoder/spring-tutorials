package com.binchencoder.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.binchencoder.elasticsearch.model.AiWarningModel;
import com.binchencoder.elasticsearch.model.ChildWarning;
import com.binchencoder.elasticsearch.model.Warning;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests
 *
 * @author: chenbin
 * @date: 2021/11/25 上午10:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Slf4j
public class Tests {

  @Resource
  protected ElasticsearchOperations elasticsearchOperations;

  List<String> warnNames = Lists.newArrayList("wear_slippers_or_bare_feet_warning",
      "beer_bottle_warning", "not_uniform_warning", "no_door_beam_waring",
      "no_oblique_brick_waring", "smoking_warning");
  Random warnNamesRandom = new Random(warnNames.size());

  List<String> warnChildNames = Lists.newArrayList("toe", "beer_bottle", "not_uniform", "new_wall",
      "door", "door_beam", "smoke");
  Random warnChildNamesRandom = new Random(warnChildNames.size());

  @Test
  public void testInsert() {
    List<AiWarningModel> warningModels = Lists.newArrayList();
    for (int i = 1; i < 20; i++) {
      AiWarningModel aiWarningModel = AiWarningModel.builder()
          .id(UUID.randomUUID().toString()).build();
      if (i % 2 == 0) {
        elasticsearchOperations.save(aiWarningModel);
        continue;
      }

      List<Warning> warnings = Lists.newArrayList();
      for (int m = 1; m < 5; m++) {
        Warning warning = Warning.builder()
            .name(warnNames.get(warnNamesRandom.nextInt(warnNames.size()))).build();

        List<ChildWarning> childWarnings = Lists.newArrayList();
        for (int j = 1; j < 4; j++) {
          ChildWarning childWarning = ChildWarning.builder()
              .label(warnChildNames.get(warnChildNamesRandom.nextInt(warnChildNames.size())))
              .score(Math.random())
              .build();
          childWarnings.add(childWarning);
        }
        warning.setChildren(childWarnings);

        warnings.add(warning);
      }

      aiWarningModel.setWarnings(warnings);
      warningModels.add(aiWarningModel);
    }

//    System.out.println(JSON.toJSON(warningModels));
    elasticsearchOperations.save(warningModels);
  }

}
