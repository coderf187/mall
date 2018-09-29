package com.jianf.commons.advice;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dongchen on 2017/7/19.
 */
public class LoanInputMessage implements HttpInputMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanInputMessage.class);

    private HttpHeaders headers;
    private InputStream body;

    public LoanInputMessage(HttpInputMessage inputMessage) throws IOException {
        this.headers = inputMessage.getHeaders();
        String token = headers.getFirst("token");
        String version = headers.getFirst("version");
        String type = headers.getFirst("type");
        String deviceToken = headers.getFirst("deviceToken");
        String channel = headers.getFirst("channel");

        InputStream bodyInput = inputMessage.getBody();
        JSONObject jsonObject = new JSONObject();
        String body = "";
        if(bodyInput != null){
            body = IOUtils.toString(bodyInput, "UTF-8");
            jsonObject = JSONObject.parseObject(body);
        }
        HttpServletRequest request =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request != null){
            Object uid = request.getAttribute("uid");
            if(uid != null){
                jsonObject.put("uid", uid);
            }
        }

        if (StringUtils.isNotBlank(deviceToken)) {
            jsonObject.put("deviceToken", deviceToken);
        }
        if (StringUtils.isNotBlank(version)) {
            jsonObject.put("version", version);
        }
        if (StringUtils.isNotBlank(type)) {
            jsonObject.put("type", type);
        }
        if (StringUtils.isNotBlank(channel)) {
            jsonObject.put("channel", channel);
        }

        LOGGER.info("url: {}, token: {}, request basic info: {} body: {}", (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE), token, jsonObject.toJSONString(), body);

        this.body = IOUtils.toInputStream(jsonObject.toString(), "UTF-8");
    }

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

}
