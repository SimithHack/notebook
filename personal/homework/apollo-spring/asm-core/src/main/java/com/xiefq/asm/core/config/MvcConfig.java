package com.xiefq.asm.core.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.xiefq.asm.cnst.QuotaSourceType;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * spring mvc 配置
 */
@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {
    /**
     * 配置消息转换
     * @param converters
     */
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(QuotaSourceType.class, (JsonSerializer<QuotaSourceType>) (quotaSourceType, type, jsonSerializationContext) -> new JsonPrimitive(quotaSourceType.getDes()));
        gb.setDateFormat("yyyy-MM-dd HH:mm:ss");
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gb.create());
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/");
    }
}
