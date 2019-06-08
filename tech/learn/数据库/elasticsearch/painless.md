---
标题: Painless脚本
---

# Painless脚本
> 是在es使用的简单，安全的脚本语言。可以以内联和文件两种方式运行。

## painless的功能点
+ 高性能
+ 更加的安全。有许多的可用的变量和函数调用。
+ 变量可以是静态也可以是运行时动态声明
+ 类似Java的句法
+ 专为es而生，所以性能等各方面都最大优化。

## painless的使用案例
+ 访问文档字段
```json
GET /students/_search
{
    "query": {
        "function_score": {
            "script_score": {
                "script": {
                    "lang": "painless",
                    "source": """
                        int total = 0;
                        for(int i=0; i<doc['goals'].length; ++i){
                            total += doc['goals'][i];
                        }
                        return total;
                    """
                }
            }
        }
    }
}
```

+ 自定义排序字段
```json
GET hockey/_search
{
  "query": {
    "match_all": {}
  },
  "sort": {
    "_script": {
      "type": "string",
      "order": "asc",
      "script": {
        "lang": "painless",
        "source": "doc['first.keyword'].value + ' ' + doc['last.keyword'].value"
      }
    }
  }
}
```

+ 检验一个字段是否存在
doc['field'].size()==0

+ 更新doc
```json
POST hockey/_update/1
{
  "script": {
    "lang": "painless",
    "source": "ctx._source.last = params.last",
    "params": {
      "last": "hockey"
    }
  }
}
```

+ 添加新字段
```json
POST hockey/_update/1
{
  "script": {
    "lang": "painless",
    "source": """
      ctx._source.last = params.last;
      ctx._source.nick = params.nick
    """,
    "params": {
      "last": "gaudreau",
      "nick": "hockey"
    }
  }
}
```

+ Date类型的字段支持 getYear getDayOfWeek getMillis 方法
>使用的时候需要去掉get前缀

```json
GET hockey/_search
{
  "script_fields": {
    "birth_year": {
      "script": {
        "source": "doc.born.value.year"
      }
    }
  }
}
```

### 使用正则表达式
> 正则表达式很强大但是很耗性能，需要手动开启 script.painless.regex.enabled: true

+ 比较
```json
POST hockey/_update_by_query
{
  "script": {
    "lang": "painless",
    "source": """
      if (ctx._source.last =~ /b/) {
        ctx._source.last += "matched";
      } else {
        ctx.op = "noop";
      }
    """
  }
}
```

+ 直接使用Macher方法
> 支持 $1 这种表达式

```json
POST hockey/_update_by_query
{
  "script": {
    "lang": "painless",
    "source": "ctx._source.last = /n([aeiou])/.matcher(ctx._source.last).replaceAll('$1')"
  }
}
```

### 使用CharSequence上的方法
```json
POST hockey/_update_by_query
{
  "script": {
    "lang": "painless",
    "source": """
      ctx._source.last = ctx._source.last.replaceFirst(/[aeiou]/, m ->
        m.group().toUpperCase(Locale.ROOT))
    """
  }
}
```

## painless怎么调度函数
> painless使用receiver，name和arity调度方法

+ painless不支持方法重载，所以使用Java标准库里的某些方法可能会带来问题。

## 怎么调试
Debug.Explain

当前最好的调试嵌入脚本的方法是抛出异常，throw new Exception("whatever")

```json
POST /hockey/_explain/1
{
  "query": {
    "script": {
      "script": "Debug.explain(doc.goals)"
    }
  }
}
```

## painless的上下文
### 测试上下文
params
```json
POST /_scripts/painless/_execute
{
  "script": {
    "source": "params.count / params.total",
    "params": {
      "count": 100.0,
      "total": 1000.0
    }
  }
}
```

### Filter上下文
+ document
+ index
```json
POST /_scripts/painless/_execute
{
  "script": {
    "source": "doc['field'].value.length() <= params.max_length",
    "params": {
      "max_length": 4
    }
  },
  "context": "filter",
  "context_setup": {
    "index": "my-index",
    "document": {
      "field": "four"
    }
  }
}
```

### Score上下文
+ index document query

```json
POST /_scripts/painless/_execute
{
  "script": {
    "source": "doc['rank'].value / params.max_rank",
    "params": {
      "max_rank": 5.0
    }
  },
  "context": "score",
  "context_setup": {
    "index": "my-index",
    "document": {
      "rank": 4
    }
  }
}
```