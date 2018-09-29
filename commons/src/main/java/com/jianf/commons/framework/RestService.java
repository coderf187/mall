package com.jianf.commons.framework;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * modify yhl， 因为restTemplate把错误抛给了业务层来处理，所以每个方法都添加了断言处理
 * 
 * @author: fengjian
 * @ClassName: RestService
 * @Description: <p>
 *               封装restTemplate 工具类,遵循restful风格标准。
 *               </p>
 *               <p>
 *               post默认为insert操作,如果查询请求参数过多需要使用post
 *               </p>
 *               <p>
 *               put默认为update操作
 *               </p>
 *               <p>
 *               get默认为select操作
 *               </p>
 *               <p>
 *               delete默认为delete操作
 *               </p>
 *               <p>
 *               所有方法中的request请求,默认会调用方法中的{@jsonHead()}
 *               ,封装成HttpEntity,封装格式为json
 *               </p>
 * @date 2016年3月15日 下午4:03:08
 */
@Component
public class RestService {
    @Autowired
    @Qualifier("restTemplate1")
    private RestTemplate restTemplate;

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:构造方法
     * </p>
     *
     * @param restTemplate
     */
    public RestService(RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:构造方法
     * </p>
     */
    public RestService() {
        super();
    }

    /**
     * @auth:fengjian
     * @Title: jsonHead
     * @Description: 封装HttpEntity信息
     * @param @param request
     * @param @return 设定文件
     * @return HttpEntity 返回类型
     * @throws
     */
    private HttpEntity<String> jsonHead(Object request, String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Accept", APPLICATION_JSON.toString());
        if (StringUtils.isNotBlank(authHeader)) {
            headers.add("Authorization", authHeader);
        }
        return new HttpEntity<>(JSON.toJSONString(request), headers);
    }
    private static HttpEntity<String> formHead(Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.add("Accept", APPLICATION_FORM_URLENCODED.toString());
        return new HttpEntity<>(JSON.toJSONString(request), headers);
    }
    /**
     * @auth:fengjian
     * @Title: jsonPost
     * @Description: rest post请求 参数为object
     * @param @param url
     * @param @param request
     * @param @param responseType
     * @param @param uriVariables
     * @param @param authHeader
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     */
    public <T> T jsonPost(String url, Object request, Class<T> responseType, Object[] uriVariables, String authHeader) {
        ResponseEntity<T> res = this.restTemplate.postForEntity(url, jsonHead(request, authHeader), responseType,
                uriVariables);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()// NOSONAR
                + ", error body: " + res.getBody() + ", request url is :" + url);// NOSONAR
        return res.getBody();
    }

    /**
     * @auth:fengjian
     * @Title: jsonPost
     * @Description: rest post请求 参数为map
     * @param @param url
     * @param @param request
     * @param @param responseType
     * @param @param urlVariables
     * @param @param authHeader
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     */
    public <T> T jsonPost(String url, Object request, Class<T> responseType, Map<String, ?> urlVariables,
            String authHeader) {
        ResponseEntity<T> res = this.restTemplate.postForEntity(url, jsonHead(request, authHeader), responseType,
                urlVariables);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
        return res.getBody();
    }

    public <T> T formPost(String url, Object request, Class<T> responseType) {
        ResponseEntity<T> res = this.restTemplate.postForEntity(url, formHead(request), responseType);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
        return res.getBody();
    }
    /**
     * @auth:fengjian
     * @Title: jsonPost
     * @Description: rest post请求
     * @param @param url
     * @param @param request
     * @param @param responseType
     * @param @param authHeader
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     */
    public <T> T jsonPost(String url, Object request, Class<T> responseType, String authHeader) {
        ResponseEntity<T> res = this.restTemplate.postForEntity(url, jsonHead(request, authHeader), responseType);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
        return res.getBody();
    }

    /**
     * json put请求
     * @author yhl
     *
     * @param url
     * @param uriVariables
     * @param authHeader
     */
    public void jsonPut(String url, Object[] uriVariables, String authHeader) {
        ResponseEntity<String> res = this.restTemplate.exchange(url, HttpMethod.PUT, jsonHead(null, authHeader), String.class, uriVariables);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
    }

    /**
     * @auth:fengjian
     * @Title: jsonPut
     * @Description: 封装put请求 rest服务
     * @param @param url
     * @param @param request
     * @param @param uriVariables
     * @param @param authHeader 设定文件
     * @return void 返回类型
     * @throws
     */
    public void jsonPut(String url, Object request, Object[] uriVariables, String authHeader) {
        ResponseEntity<String> res = this.restTemplate.exchange(url, HttpMethod.PUT, jsonHead(request, authHeader), String.class, uriVariables);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
    }

    /**
     * @auth:fengjian
     * @Title: jsonPut
     * @Description: 封装put请求 rest服务地址有参数且为map
     * @param @param url
     * @param @param request
     * @param @param urlVariables
     * @param @param authHeader 设定文件
     * @return void 返回类型
     * @throws
     */
    public void jsonPut(String url, Object request, Map<String, ?> urlVariables, String authHeader) {
        ResponseEntity<String> res = this.restTemplate.exchange(url, HttpMethod.PUT, jsonHead(request, authHeader), String.class, urlVariables);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
    }

    /**
     * @auth:fengjian
     * @Title: jsonPut
     * @Description: 封装put请求 rest服务地址无参方法
     * @param @param url
     * @param @param request
     * @param @param authHeader 设定文件
     * @return void 返回类型
     * @throws
     */
    public void jsonPut(String url, Object request, String authHeader) {
        ResponseEntity<String> res = this.restTemplate.exchange(url, HttpMethod.PUT, jsonHead(request, authHeader), String.class);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
    }

    /**
     * @auth:fengjian
     * @Title: JsonGet
     * @Description: 封装rest服务get请求 参数为object调用
     * @param @param url HTTP地址
     * @param @param responseType 返回类型(例如：String.class)
     * @param @param uriVariables 参数类型可变参数
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     */
    public <T> T jsonGet(String url, Class<T> responseType, Object[] uriVariables) {
        ResponseEntity<T> res = this.restTemplate.exchange(url, HttpMethod.GET, null, responseType, uriVariables);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
        return res.getBody();
    }

    public <T> T jsonGet(String url, ParameterizedTypeReference<T> responseType, Object[] uriVariables) {
        ResponseEntity<T> res = this.restTemplate.exchange(url, HttpMethod.GET, null, responseType, uriVariables);
        return res.getBody();
    }

    /**
     * @auth:fengjian
     * @Title: JsonGet
     * @Description: 封装rest服务get请求 参数为map调用
     * @param @param url HTTP地址
     * @param @param responseType 返回类型(例如：String.class)
     * @param @param urlVariables 参数为map
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     */
    public <T> T jsonGet(String url, Class<T> responseType, Map<String, ?> urlVariables) {
        ResponseEntity<T> res = this.restTemplate.exchange(url, HttpMethod.GET, jsonHead(null, null), responseType, urlVariables);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
        return res.getBody();
    }

    /**
     * @auth:fengjian
     * @Title: JsonGet
     * @Description: 封装rest服务get请求 无参调用
     * @param @param url HTTP地址
     * @param @param responseType 返回类型(例如：String.class)
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     */
    public <T> T jsonGet(String url, Class<T> responseType) {
        ResponseEntity<T> res = this.restTemplate.exchange(url, HttpMethod.GET, jsonHead(null, null), responseType);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
        return res.getBody();
    }


    public <T> T jsonGetForBigData(String url, Class<T> responseType) {
        ResponseEntity<T> res = this.restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
        return res.getBody();
    }



    /**
     * @auth:fengjian
     * @Title: JsonDelete
     * @Description: 封装rest服务delete请求 无参调用
     * @param @param url HTTP地址
     * @return void 返回类型
     * @throws
     */
    public void jsonDelete(String url) {
        ResponseEntity<String> res = this.restTemplate.exchange(url, HttpMethod.DELETE, jsonHead(null, null), String.class);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
    }

    /**
     * @auth:fengjian
     * @Title: JsonDelete
     * @Description: 封装rest服务delete请求 参数为map
     * @param @param url HTTP地址
     * @param @param urlVariables map参数
     * @return void 返回类型
     * @throws
     */
    public void jsonDelete(String url, Map<String, ?> urlVariables) {
        ResponseEntity<String> res = this.restTemplate.exchange(url, HttpMethod.DELETE, jsonHead(null, null), String.class, urlVariables);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
    }

    /**
     * @auth:fengjian
     * @Title: jsonDelete
     * @Description:封装rest服务delete请求 参数为任意object
     * @param @param url
     * @param @param uriVariables 设定文件
     * @return void 返回类型
     * @throws
     */
    public void jsonDelete(String url, Object[] uriVariables) {
        ResponseEntity<String> res = this.restTemplate.exchange(url, HttpMethod.DELETE, jsonHead(null, null), String.class, uriVariables);
        Assert.isTrue(res.getStatusCode() == HttpStatus.OK, "Failed: HTTP error code: " + res.getStatusCode()
                + ", error body: " + res.getBody() + ", request url is :" + url);
    }

}