package com.binchencoder.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.binchencoder.elasticsearch.model3.AiWarningV3Model;
import com.binchencoder.elasticsearch.model3.WarningV3;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests for `ai_warning_v3`
 *
 * @author: chenbin
 * @date: 2021/11/25 上午10:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Slf4j
public class AiWarningV3ModelTests {

  static {
    System.setProperty("es.set.netty.runtime.available.processors", "false");
  }

  @Resource
  protected ElasticsearchOperations elasticsearchOperations;

//  List<String> warnNames = Lists.newArrayList("wear_slippers_or_bare_feet_warning",
//      "beer_bottle_warning", "not_uniform_warning", "no_door_beam_waring",
//      "no_oblique_brick_waring", "smoking_warning", "test");

  List<String> warnNames = Lists.newArrayList("smoking_warning", "test");
  Random warnNamesRandom = new Random(warnNames.size());

  List<String> warnChildNames = Lists.newArrayList("toe", "beer_bottle", "not_uniform", "new_wall",
      "door", "door_beam", "smoke");
  Random warnChildNamesRandom = new Random(warnChildNames.size());

  @Before
  public void beforeTest() {
    this.createIndexTest();
  }

  @Test
  public void createIndexTest() {
    if (!elasticsearchOperations.indexOps(AiWarningV3Model.class).exists()) {
      elasticsearchOperations.indexOps(AiWarningV3Model.class)
          .create(Document.create().append("number_of_shards", 3).append("number_of_replicas", 2));
    }
  }

  @Test
  public void deleteIndexTest() {
    if (elasticsearchOperations.indexOps(AiWarningV3Model.class).exists()) {
      elasticsearchOperations.indexOps(AiWarningV3Model.class).delete();
      this.createIndexTest();
    }
  }

  @Test
  public void testInsertV1() {
    List<AiWarningV3Model> warningModels = Lists.newArrayList();
    for (int i = 1; i < 20; i++) {
      AiWarningV3Model aiWarningModel = AiWarningV3Model.builder()
          .id(UUID.randomUUID().toString()).build();

      // 写入空数据
//      if (i % 2 == 0) {
//        this.saveWarningModels(Lists.newArrayList(aiWarningModel));
//        continue;
//      }

      List<WarningV3> warnings = Lists.newArrayList();
      for (int m = 1; m < 4; m++) {
        String warnName = warnNames.get(warnNamesRandom.nextInt(warnNames.size()));
        WarningV3 warning = WarningV3.builder().name(warnName).build();

        Map<String, Float> labelScoreMap = Maps.newHashMap();
        for (int n = 1; n < 5; n++) {
          String warnLabel = warnChildNames.get(
              warnChildNamesRandom.nextInt(warnChildNames.size()));
          labelScoreMap.put(warnLabel, Double.valueOf(Math.random()).floatValue());
        }
        warning.setChildren(labelScoreMap);

        warnings.add(warning);
      }
      aiWarningModel.setWarnings(warnings);

      warningModels.add(aiWarningModel);
    }

    this.saveWarningModels(warningModels);
  }

  public void saveWarningModels(List<AiWarningV3Model> warningModels) {
    if (null == warningModels) {
      return;
    }

    System.out.println(JSON.toJSON(warningModels));
    elasticsearchOperations.save(warningModels);
  }

  @Test
  public void testQueryV1() {
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    BoolQueryBuilder mustBuilder1 = QueryBuilders.boolQuery()
        .must(QueryBuilders.rangeQuery("warnings.wear_slippers_or_bare_feet_warning.door_beam")
            .gt(0.46))
        .must(QueryBuilders.rangeQuery("warnings.wear_slippers_or_bare_feet_warning.not_unitform")
            .gt(0.6));

    BoolQueryBuilder mustBuilder2 = QueryBuilders.boolQuery()
        .must(QueryBuilders.rangeQuery("warnings.beer_bottle_warning.new_wall")
            .gt(0.5))
        .must(QueryBuilders.rangeQuery("warnings.beer_bottle_warning.toe")
            .gt(0.6));

    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
        .should(mustBuilder1)
        .should(mustBuilder2);
    queryBuilder.withQuery(boolQueryBuilder);
    SearchHits<AiWarningV3Model> search = elasticsearchOperations.search(queryBuilder.build(),
        AiWarningV3Model.class);
    if (!search.isEmpty()) {
      List<AiWarningV3Model> result = search.get().map(SearchHit::getContent)
          .collect(Collectors.toList());
      System.out.println(JSON.toJSON(result));
    }
  }
}
