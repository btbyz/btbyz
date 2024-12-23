package com.ruoyi.common.utils.http;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author yuji
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        // 连接池最大连接数
        poolingConnectionManager.setMaxTotal(1000);
        // 每个主机的并发
        poolingConnectionManager.setDefaultMaxPerRoute(500);
        return poolingConnectionManager;
    }
    @Bean
    public HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //设置HTTP连接管理器
        httpClientBuilder.setConnectionManager(poolingConnectionManager());
        return httpClientBuilder;
    }
    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setHttpClient(httpClientBuilder().build());
        //获取链接超时时间
        httpRequestFactory.setConnectionRequestTimeout(3000);
        //指客户端和服务器建立连接的timeout
        httpRequestFactory.setConnectTimeout(3000);
        //读取数据的超时时间
        httpRequestFactory.setReadTimeout(120000);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        //换上fastjson
        List<HttpMessageConverter<?>> messageConverters= restTemplate.getMessageConverters();
        //由于系统中默认有jackson 在转换json时自动会启用  但是我们不想使用它 可以直接移除
        messageConverters.removeIf(converter -> converter instanceof GsonHttpMessageConverter || converter instanceof MappingJackson2HttpMessageConverter);
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setReaderFeatures(JSONReader.Feature.FieldBased, JSONReader.Feature.SupportArrayToBean);
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteMapNullValue, JSONWriter.Feature.PrettyFormat);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        fastJsonHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(fastJsonHttpMessageConverter);

        return restTemplate;
    }
}
