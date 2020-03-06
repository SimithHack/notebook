# 自定义`SpringMVCConfig`的时候或者使用`@EnableMVC`会导致资源加载失败
> 同时 使用自定义GsonMessageConverter的时候，会到之Swagger失效，的解决思路

```java
package com.msxf.retail.department.operation.front.bootstrap.config;

import com.google.gson.*;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport{

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*")
                .addResourceLocations("classpath:/static/",
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:/META-INF/resources/");
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        GsonBuilder gb = new GsonBuilder();
        gb.enableComplexMapKeySerialization();
        gb.registerTypeAdapter(Double.class, new JsonSerializer<Double>(){
            @Override
            public JsonElement serialize(Double value, Type type, JsonSerializationContext context) {
                return new JsonPrimitive(new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toString());
            }
        });
        gb.registerTypeAdapter(Float.class, new JsonSerializer<Float>(){
            @Override
            public JsonElement serialize(Float value, Type type, JsonSerializationContext context) {
                return new JsonPrimitive(new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toString());
            }
        });
        //swagger出错
        gb.registerTypeAdapter(Json.class, new JsonSerializer<Json>() {
            @Override
            public JsonElement serialize(Json json, Type type, JsonSerializationContext jsonSerializationContext) {
                final JsonParser parser = new JsonParser();
                return parser.parse(json.value());
            }
        });
        GsonHttpMessageConverter gson = new GsonHttpMessageConverter();
        gson.setGson(gb.create());
        converters.add(gson);
        super.configureMessageConverters(converters);
    }

}
```