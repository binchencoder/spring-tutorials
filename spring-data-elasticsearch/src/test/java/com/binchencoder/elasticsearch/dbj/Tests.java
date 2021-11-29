package com.binchencoder.elasticsearch.dbj;

import com.binchencoder.elasticsearch.Application;
import com.binchencoder.elasticsearch.dbj.model.AiLabelModel;
import com.binchencoder.elasticsearch.dbj.query.AiLabelQuery;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;

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

  @Test
  public void test() {
    AiLabelQuery query = AiLabelQuery.builder().page(1).rows(15).build();
    NativeSearchQueryBuilder queryBuilder = this.buildQuery(query);
    if (Objects.nonNull(query.getPage()) && Objects.nonNull(query.getRows())) {
      queryBuilder.withPageable(PageRequest.of(query.getPage() - 1, query.getRows()));
    }
    queryBuilder.withSort(SortBuilders.fieldSort("alarmTime").order(SortOrder.DESC));
    SearchHits<AiLabelModel> search = elasticsearchOperations.search(queryBuilder.build(),
        AiLabelModel.class);
    if (!search.isEmpty()) {
      search.get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    log.info("pageList-DSL-->:{}", queryBuilder.build().getQuery());
  }

  private NativeSearchQueryBuilder buildQuery(AiLabelQuery query) {
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    BoolQueryBuilder queryBool = QueryBuilders.boolQuery();
    if (!ObjectUtils.isEmpty(query.getDeviceSerial())) {
      queryBool.filter(QueryBuilders.termQuery("deviceSerial", query.getDeviceSerial()));
    }
    //类别
    if (!ObjectUtils.isEmpty(query.getLabelType())) {
      queryBool.filter(QueryBuilders.termsQuery("labelType", query.getLabelType()));
    }
    //标签
    if (!ObjectUtils.isEmpty(query.getTags())) {
      queryBool.filter(QueryBuilders.termsQuery("tags", query.getTags()));
    }
    //告警级别
    if (!ObjectUtils.isEmpty(query.getWarningLevel())) {
      queryBool.filter(QueryBuilders.termQuery("warningLevel", query.getWarningLevel()));
    }
    //项目地址
    if (!ObjectUtils.isEmpty(query.getAddress())) {
      queryBool.filter(QueryBuilders.matchQuery("address", query.getAddress()));
    }
    //时间范围
    if (!ObjectUtils.isEmpty(query.getAlarmStartTime()) &&
        !ObjectUtils.isEmpty(query.getAlarmEndTime())) {
      queryBool.must(QueryBuilders.rangeQuery("alarmTime").
          gte(query.getAlarmStartTime()).lte(query.getAlarmEndTime()).timeZone("Asia/Shanghai"));
    }
    queryBuilder.withQuery(queryBool);
    return queryBuilder;
  }

}
