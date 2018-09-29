package com.jianf.commons.framework;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static org.springframework.http.MediaType.*;

/**
 * fengjian 封装oauth2resttemplate操作
 */
public class OauthService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(OauthService.class);

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    /**
     * @param oAuth2RestTemplate
     */
    public OauthService(OAuth2RestTemplate oAuth2RestTemplate) {
        super();
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    // 保证在所有依赖bean加载完成后执行
    @Override
    public void afterPropertiesSet() throws Exception {
        //modifyTimeout();
    }

    private void modifyTimeout() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setReadTimeout(60000); // 超时时间
        httpComponentsClientHttpRequestFactory.setConnectTimeout(60000); // 超时时间
        this.oAuth2RestTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);

        // 设置errorHandler（打印具体的错误信息）
        this.oAuth2RestTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // Ignore it
            }
        });
    }

    /**
     *
     */
    public OauthService() {
        super();
    }

    public OAuth2AccessToken getAccessToken() {
        return oAuth2RestTemplate.getAccessToken();
    }

    /**
     * 封装HttpEntity信息
     *
     * @param request
     * @return
     */
    private static HttpEntity<String> jsonHead(Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
//        headers.add("Accept", APPLICATION_JSON_UTF8.toString());
        headers.add("Connection", "close");
        return new HttpEntity<>(JSON.toJSONString(request,new SerializerFeature[]{WriteMapNullValue,WriteNullListAsEmpty}), headers);
    }

    private static HttpEntity<String> jsonHead(Object request, String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        headers.add("Accept", APPLICATION_JSON_UTF8.toString());
        headers.add("Authorization", authHeader);
        return new HttpEntity<>(JSON.toJSONString(request), headers);
    }


    private static HttpEntity<MultiValueMap<String, String>> formHead(Map<String, String> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : request.entrySet()) {
            params.add(entry.getKey(), entry.getValue());
        }
        return new HttpEntity(params, headers);
    }

    public <T> T formPost(String url, Map<String, String> request, Class<T> responseType) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.postForEntity(url, formHead(request), responseType);
        this.validateStatus(res, url);
        return res.getBody();
    }

    private static HttpEntity<String> plainHead(Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        headers.add("Accept", TEXT_PLAIN.toString());
        headers.add("Connection", "close");
        return new HttpEntity<>(JSON.toJSONString(request,new SerializerFeature[]{WriteMapNullValue,WriteNullListAsEmpty}), headers);
    }

    /**
     * 服务编排post封装
     *
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T plainPost(String url, Object request, Class<T> responseType) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.postForEntity(url, plainHead(request), responseType);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * post封装
     *
     * @param url
     * @param request
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    public <T> T jsonPost(String url, Object request, Class<T> responseType, Object[] uriVariables) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.postForEntity(url, jsonHead(request), responseType,
                uriVariables);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * post封装
     *
     * @param url
     * @param request
     * @param responseType
     * @param urlVariables
     * @param <T>
     * @return
     */
    public <T> T jsonPost(String url, Object request, Class<T> responseType, Map<String, ?> urlVariables) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.postForEntity(url, jsonHead(request), responseType,
                urlVariables);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * post封装
     *
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T jsonPost(String url, Object request, Class<T> responseType){
        ResponseEntity<T> res = this.oAuth2RestTemplate.postForEntity(url, jsonHead(request), responseType);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * post操作
     *
     * @param url
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T jsonPost(String url, Object request, ParameterizedTypeReference<T> responseType) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.POST, jsonHead(request), responseType);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * post操作
     *
     * @param url
     * @param request
     * @return
     */
    public String postForObject(String url, Object request) {
        Map map = new HashMap();
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return oAuth2RestTemplate.postForObject(url, request, String.class);
    }

    public <T> T jsonPost(String url, Object request, Class<T> responseType, String header) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.postForEntity(url, jsonHead(request, header), responseType);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * json put请求
     *
     * @param url
     * @param uriVariables
     * @author yhl
     */
    public void jsonPut(String url, Object[] uriVariables) {
        ResponseEntity<String> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.PUT, null, String.class,
                uriVariables);
        this.validateStatus(res, url);
    }

    /**
     * json put请求
     *
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T jsonPut(String url, Object request, ParameterizedTypeReference<T> responseType) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.PUT, jsonHead(request), responseType);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * json put请求
     *
     * @param url
     * @param request
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    public <T> T jsonPut(String url, Object request, ParameterizedTypeReference<T> responseType,
            Object[] uriVariables) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.PUT, jsonHead(request), responseType,
                uriVariables);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * put操作
     *
     * @param url
     * @param request
     * @param uriVariables
     */
    public void jsonPut(String url, Object request, Object[] uriVariables) {
        ResponseEntity<String> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.PUT, jsonHead(request),
                String.class, uriVariables);
        this.validateStatus(res, url);
    }

    /**
     * put操作
     *
     * @param url
     * @param request
     * @param urlVariables
     */
    public void jsonPut(String url, Object request, Map<String, ?> urlVariables) {
        ResponseEntity<String> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.PUT, jsonHead(request),
                String.class, urlVariables);
        this.validateStatus(res, url);
    }

    /**
     * put操作
     *
     * @param url
     * @param request
     */
    public void jsonPut(String url, Object request) {
        ResponseEntity<String> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.PUT, jsonHead(request),
                String.class);
        this.validateStatus(res, url);
    }

    /**
     * get操作
     *
     * @param url
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    public <T> T jsonGet(String url, Class<T> responseType, Object[] uriVariables) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.GET, null, responseType, uriVariables);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * get操作
     *
     * @param url
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    public <T> T jsonGet(String url, ParameterizedTypeReference<T> responseType, Object[] uriVariables) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.GET, null, responseType, uriVariables);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * get操作
     *
     * @param url
     * @param responseType
     * @param urlVariables
     * @param <T>
     * @return
     */
    public <T> T jsonGet(String url, Class<T> responseType, Map<String, ?> urlVariables) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.GET, null, responseType, urlVariables);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * get操作
     *
     * @param url
     * @param responseType
     * @param urlVariables
     * @param <T>
     * @return
     */
    public <T> T jsonGet(String url, ParameterizedTypeReference<T> responseType, Map<String, ?> urlVariables) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.GET, null, responseType, urlVariables);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * get操作
     *
     * @param url
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T jsonGet(String url, Class<T> responseType) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.GET, null, responseType);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * get操作
     *
     * @param url
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T jsonGet(String url, ParameterizedTypeReference<T> responseType) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.GET, null, responseType);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * delete操作
     *
     * @param url
     */
    public void jsonDelete(String url) {
        ResponseEntity<String> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
        this.validateStatus(res, url);
    }

    /**
     * delete 操作
     *
     * @param url
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    public <T> T jsonDelete(String url, ParameterizedTypeReference<T> responseType, Object[] uriVariables) {
        ResponseEntity<T> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.DELETE, null, responseType,
                uriVariables);
        this.validateStatus(res, url);
        return res.getBody();
    }

    /**
     * delete操作
     *
     * @param url
     * @param urlVariables
     */
    public void jsonDelete(String url, Map<String, ?> urlVariables) {
        ResponseEntity<String> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.DELETE, null, String.class,
                urlVariables);
        this.validateStatus(res, url);
    }

    /**
     * delete操作
     *
     * @param url
     * @param uriVariables
     */
    public void jsonDelete(String url, Object[] uriVariables) {
        ResponseEntity<String> res = this.oAuth2RestTemplate.exchange(url, HttpMethod.DELETE, null, String.class,
                uriVariables);
        this.validateStatus(res, url);
    }

    public void validateStatus(ResponseEntity responseEntity, String url) {
        if(!Objects.equals(responseEntity.getStatusCode(), HttpStatus.OK)) {
            Object body = responseEntity.getBody();
            LOGGER.error("Failed: HTTP error code: " + responseEntity.getStatusCode() + ", error body: " + body + ", request url is :" + url);
                Assert.isTrue(false, body.toString());
        }
    }

}