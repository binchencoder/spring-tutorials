package com.binchencoder.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.binchencoder.elasticsearch.model1.AiWarningV1Model;
import com.binchencoder.elasticsearch.model1.WarningV1;
import com.google.common.collect.Lists;
import java.util.List;
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
public class AiWarningV1ModelTests {

  static {
    System.setProperty("es.set.netty.runtime.available.processors", "false");
  }

  @Resource
  protected ElasticsearchOperations elasticsearchOperations;

  List<String> warnNames = Lists.newArrayList("wear_slippers_or_bare_feet_warning",
      "beer_bottle_warning", "not_uniform_warning", "no_door_beam_waring",
      "no_oblique_brick_waring", "smoking_warning");
  Random warnNamesRandom = new Random(warnNames.size());

  List<String> warnChildNames = Lists.newArrayList("toe", "beer_bottle", "not_uniform", "new_wall",
      "door", "door_beam", "smoke");
  Random warnChildNamesRandom = new Random(warnChildNames.size());

  Random scoreRandom = new Random(100);

  @Before
  public void beforeTest() {
    if (!elasticsearchOperations.indexOps(AiWarningV1Model.class).exists()) {
      elasticsearchOperations.indexOps(AiWarningV1Model.class)
          .create(Document.create().append("number_of_shards", 3).append("number_of_replicas", 2));
    }
  }

  @Test
  public void testInsertV1() {
    List<AiWarningV1Model> warningModels = Lists.newArrayList();
    for (int i = 1; i < 20; i++) {
      AiWarningV1Model aiWarningModel = AiWarningV1Model.builder()
          .id(UUID.randomUUID().toString()).build();
      if (i % 2 == 0) {
        elasticsearchOperations.save(aiWarningModel);
        continue;
      }

      List<WarningV1> warnings = Lists.newArrayList();
      for (int m = 1; m < 5; m++) {
        String warnName = warnNames.get(warnNamesRandom.nextInt(warnNames.size()));
        String warnLabel = warnChildNames.get(warnChildNamesRandom.nextInt(warnChildNames.size()));
        WarningV1 warning = WarningV1.builder()
            .name(warnName + "." + warnLabel)
            .score(scoreRandom.nextInt(100))
            .build();

        warnings.add(warning);
      }

      aiWarningModel.setWarnings(warnings);
      warningModels.add(aiWarningModel);
    }

//    System.out.println(JSON.toJSON(warningModels));
    elasticsearchOperations.save(warningModels);
  }

  @Test
  public void testQueryV1() {
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    BoolQueryBuilder mustBuilder1 = QueryBuilders.boolQuery()
        .must(QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery("warnings.name",
                "wear_slippers_or_bare_feet_warning.new_wall"))
            .must(QueryBuilders.rangeQuery("warnings.score").gt(20))
        ).must(QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery("warnings.name",
                "wear_slippers_or_bare_feet_warning.beer_bottle"))
            .must(QueryBuilders.rangeQuery("warnings.score").gt(23))
        );

    BoolQueryBuilder mustBuilder2 = QueryBuilders.boolQuery()
        .must(QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery("warnings.name",
                "beer_bottle_warning.new_wall"))
            .must(QueryBuilders.rangeQuery("warnings.score").gt(50))
        ).must(QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery("warnings.name",
                "beer_bottle_warning.toe"))
            .must(QueryBuilders.rangeQuery("warnings.score").gt(66))
        );

    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
        .should(mustBuilder1)
        .should(mustBuilder2);
    queryBuilder.withQuery(boolQueryBuilder);
    SearchHits<AiWarningV1Model> search = elasticsearchOperations.search(queryBuilder.build(),
        AiWarningV1Model.class);
    if (!search.isEmpty()) {
      List<AiWarningV1Model> result = search.get().map(SearchHit::getContent)
          .collect(Collectors.toList());
      System.out.println(JSON.toJSON(result));
    }
  }
}
