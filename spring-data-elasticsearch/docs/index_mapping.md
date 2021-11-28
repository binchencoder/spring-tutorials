## ai_warning

删除索引
```http
DELETE /ai_warning
```

创建索引
```http
PUT /ai_warning
{
    "settings":{
        "number_of_shards":3,
        "number_of_replicas":2
    }
}
```

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
                "properties":{
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
                        "type":"float"
                    }
                }
            }
        }
    }
}
```

## ai_warning_v2

```http
PUT /ai_warning_v2
{
    "settings":{
        "number_of_shards":3,
        "number_of_replicas":2
    }
}
```


```http
GET /ai_warning_v2/_search
{
  "profile": true, 
  "query": {
    "bool": {
      "should": [
        {
          "bool": {
            "must": [
              {
                "range": {
                  "warnings.wear_slippers_or_bare_feet_warning.door_beam": {
                    "gt": 0.4692
                  }
                }
              },
              {
                "range": {
                  "warnings.wear_slippers_or_bare_feet_warning.not_uniform": {
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
                "range": {
                  "warnings.beer_bottle_warning.toe": {
                    "gt": 0.4692
                  }
                }
              },
              {
                "range": {
                  "warnings.beer_bottle_warning.new_wall": {
                    "gt": 0.6
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
```

