package com.zentagroup.gateway.mapping;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class HttpMapping {
    public HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data){
        var builder = new StringBuilder();
        for(Map.Entry<Object, Object> entry : data.entrySet()){
            if(builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
