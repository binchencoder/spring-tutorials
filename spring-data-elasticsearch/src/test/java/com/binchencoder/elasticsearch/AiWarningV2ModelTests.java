package com.binchencoder.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.binchencoder.elasticsearch.model2.AiWarningV2Model;
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
 * Tests
 *
 * @author: chenbin
 * @date: 2021/11/25 上午10:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Slf4j
public class AiWarningV2ModelTests {

  static {
    System.setProperty("es.set.netty.runtime.available.processors", "false");
  }

  @Resource
  protected ElasticsearchOperations elasticsearchOperations;

  List<String> warnNames = Lists.newArrayList("wear_slippers_or_bare_feet_warning",
      "beer_bottle_warning", "not_uniform_warning", "no_door_beam_waring",
      "no_oblique_brick_waring", "smoking_warning", "test");
  Random warnNamesRandom = new Random(warnNames.size());

  List<String> warnChildNames = Lists.newArrayList("toe", "beer_bottle", "not_uniform", "new_wall",
      "door", "door_beam", "smoke");
  Random warnChildNamesRandom = new Random(warnChildNames.size());

  Random scoreRandom = new Random(100);

  @Before
  public void beforeTest() {
    if (!elasticsearchOperations.indexOps(AiWarningV2Model.class).exists()) {
      elasticsearchOperations.indexOps(AiWarningV2Model.class)
          .create(Document.create().append("number_of_shards", 3).append("number_of_replicas", 2));
    }
  }

  @Test
  public void testInsertV1() {
    List<AiWarningV2Model> warningModels = Lists.newArrayList();
    for (int i = 1; i < 20; i++) {
      AiWarningV2Model aiWarningModel = AiWarningV2Model.builder()
          .id(UUID.randomUUID().toString()).build();
      if (i % 2 == 0) {
        elasticsearchOperations.save(aiWarningModel);
        continue;
      }

      Map<String, Double> labelScore = Maps.newHashMap();
      for (int m = 1; m < 5; m++) {
        String warnName = warnNames.get(warnNamesRandom.nextInt(warnNames.size()));
        String warnLabel = warnChildNames.get(warnChildNamesRandom.nextInt(warnChildNames.size()));
        labelScore.put(warnName + "." + warnLabel, Math.random());
      }
      aiWarningModel.setWarnings(labelScore);
      warningModels.add(aiWarningModel);
    }

//    System.out.println(JSON.toJSON(warningModels));
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
    SearchHits<AiWarningV2Model> search = elasticsearchOperations.search(queryBuilder.build(),
        AiWarningV2Model.class);
    if (!search.isEmpty()) {
      List<AiWarningV2Model> result = search.get().map(SearchHit::getContent)
          .collect(Collectors.toList());
      System.out.println(JSON.toJSON(result));
    }
  }
}
