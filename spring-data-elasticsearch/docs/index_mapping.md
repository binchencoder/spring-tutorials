## ai_warning

删除索引
```http
DELETE /ai_warning
```

创建索引
```http
PUT /ai_warning
{
  "settings": {
  	   "number_of_shards": 3,
	"number_of_replicas": 2
    },
    "mappings": {
	"properties" : {
	"id" : {
          "type" : "text",
           "fields" : {
                "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
            }
         }
      },
      "warnings" : {
        "type": "nested",
        "properties" : {
          "children" : {
            "type": "nested",
            "properties" : {
              "label" : {
                "type" : "text",
                "fields" : {
                  "keyword" : {
                    "type" : "keyword",
                    "ignore_above" : 256
                  }
                }
              },
              "score" : {
                "type" : "float"
              }
            }
          },
          "name" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          }
        }
      }
    } 
  }
}
```

查询DSL
```http
GET /ai_warning/_search
{
  "query": {
    "nested": {
      "path": "warnings",
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "warnings.name": "beer_bottle_warning"
              }
            },
            {
              "nested": {
                "path": "warnings.children",
                "query": {
                  "bool": {
                    "must": [
                      {
                        "bool": {
                          "must": [
                            {
                              "match": {
                                "warnings.children.label": "toe"
                              }
                            },
                            {
                             "range": {
                                "warnings.children.score": {
                                  "gt": 0.6
                                }
                              }
                            }
                          ]
                        }
                      },
                      {
                        "bool": {
                          "must": [
                            {
                              "match": {
                                "warnings.children.label": "smoke"
                              }
                            },
                            {
                             "range": {
                                "warnings.children.score": {
                                  "gt": 0.1
                                }
                              }
                            }
                          ]
                        }
                      }
                    ]
                  }
                }
              }
            }
          ]
        }
      }
    }
  }
}
```
> **NOTE**：从这个例子中看出，多层嵌套(nested) 对象查询不正确


## ai_warning_v1

删除索引
```http
DELETE /ai_warning_v1
```

创建索引
```http
PUT /ai_warning_v1
{
    "settings":{
        "number_of_shards":3,
        "number_of_replicas":2
    },
    "mappings":{
        "properties":{
            "id":{
                "type":"text",
                "fields":{
                    "keyword":{
                        "type":"keyword",
                        "ignore_above":256
                    }
                }
            },
            "warnings":{
                "type":"nested",
                "include_in_parent":true,
                "properties":{PUT /ai_warning
{
 "settings":{
   "number_of_shards":3,
   "number_of_replicas":2
  }
  "properties" : {
    "id" : {
      "type" : "text",
      "fields" : {
        "keyword" : {
          "type" : "keyword",
          "ignore_above" : 256
        }
      }
    },
    "warnings" : {
      "type": "nested",
      "properties" : {
        "children" : {
          "type": "nested",
          "properties" : {
            "label" : {
              "type" : "text",
              "fields" : {
                "keyword" : {
                  "type" : "keyword",
                  "ignore_above" : 256
                }
              }
            },
            "score" : {
              "type" : "float"
            }
          }
        },
        "name" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        }
      }
    }
  }
}
                    "name":{
                        "type":"text",
                        "fields":{
                            "keyword":{
                                "type":"keyword",
                                "ignore_above":256
                            }
                        }
                    },
                    "score":{
                        "type":"scaled_float"
                    }
                }
            }
        }
    }
}
```

## ai_warning_v2

创建索引
```http
PUT /ai_warning_v2
{
    "settings":{
        "number_of_shards":3,
        "number_of_replicas":2
    }
}
```

## ai_warning_v3

创建索引
```http
PUT /ai_warning_v3
{
    "settings":{
      "number_of_shards":3,
      "number_of_replicas":2
    },
    "mappings": {
      "properties" : {
      "id" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "warnings" : {
      	"type":"nested",
        "properties" : {
          "children" : {
            "properties" : {
              
            }
          },
          "name" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          }
        }
      }
    }
  }
}
```

```http
GET /ai_warning_v3/_search
{
  "query": {
    "nested": {
      "path": "warnings",
      "query": {
        "bool": {
          "should": [
            {
              "bool": {
               "must": [
                 {
                   "match": {
                    "warnings.name": "test"
                  }
                 },
                 {
                   "range": {
                      "warnings.children.door": {
                        "lt": 0.80
                      }
                    }
                 },
                 {
                   "range": {
                      "warnings.children.toe": {
                        "gt": 0.730
                      }
                    }
                  }
                ]
              }
            },
            {
              "bool": {
               "must": [
                 {
                   "match": {
                    "warnings.name": "smoking_warning"
                  }
                 },
                 {
                   "range": {
                      "warnings.children.door": {
                        "gt": 0.6
                      }
                    }
                 },
                 {
                   "range": {
                      "warnings.children.toe": {
                        "gt": 0.1
                      }
                    }
                  }
                ]
              }
            }
          ]
        }
      }
    }
  }
}
```