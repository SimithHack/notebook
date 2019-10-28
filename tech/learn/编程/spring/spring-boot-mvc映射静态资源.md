# mvc
## 显式配置 @EnableWebMvc 导致静态资源访问失败
+ spring中静态资源访问的实现
```java
org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class,
		WebMvcConfigurerAdapter.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class,
		ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
}
```
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)这一句导致这个配置失效

因为@EnableWebMvc导入了这个配置
```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DelegatingWebMvcConfiguration.class})
public @interface EnableWebMvc {
}
```
而DelegatingWebMvcConfiguration继承了WebMvcConfigurationSupport
```java
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
    ...
}
```

+ spring官网也这样说
```
if you want to keep Spring Boot MVC features, and you just want to add additional MVC configuration (interceptors, formatters, view controllers etc.) you can add your own @Configuration class of type WebMvcConfigurerAdapter, but without @EnableWebMvc. If you wish to provide custom instances of RequestMappingHandlerMapping, RequestMappingHandlerAdapter or ExceptionHandlerExceptionResolver you can declare a WebMvcRegistrationsAdapter instance providing such components.
大体意思是：如果你想保留spring boot mvc的特性，但是自己又想添加额外的mvc配置（拦截器，格式化等）你需要添加你自己的WebMvcConfigurerAdapter实现，而且记住不要有 @EnableWebMvc注解。
```

+ 解决办法
重写WebMvcConfigurerAdapter的addResourceHandlers方法

例如，我想将spring-rest-docs的目录映射出去
```java
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
    /**
     * 映射spring-rest-docs
     * @param converters
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/");
    }
}
```