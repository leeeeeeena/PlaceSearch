package com.yurim.placesearch.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestExchangeUtil {

    private final RestTemplate restTemplate;

    protected URI buildUri(String baseUrl, String path, Map<String, Object> queryParams) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl).path(path);
        if (queryParams != null) {
            queryParams.forEach((key, value) -> builder.queryParam(key, value));
        }
        return builder.build().toUri();
    }

    protected <T> HttpEntity<T> buildHttpEntity(Map<String, String> headers) {

//        headers.put("Content-Type", "application/json;charset=UTF-8");
//        headers.put("Accept", "application/json");
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach((key, value) -> httpHeaders.add(key, value));
        }

        return new HttpEntity<T>(httpHeaders);
    }

    public <T> ResponseEntity<T> get(Map<String, String> headers, String baseUrl, String path, Map<String, Object> queryParams, Class<T> responseType) {
        URI uri = buildUri(baseUrl, path, queryParams);
        return restTemplate.exchange(uri, HttpMethod.GET, buildHttpEntity(headers), responseType);
    }

    public <T> Map buildQueryParams(T searchClass) {
        // Class 의 필드를 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Map queryParams = objectMapper.convertValue(searchClass, Map.class);
        return queryParams;
    }

}
