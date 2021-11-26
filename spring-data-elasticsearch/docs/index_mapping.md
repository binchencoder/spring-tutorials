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

## ai_warning_v2

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

