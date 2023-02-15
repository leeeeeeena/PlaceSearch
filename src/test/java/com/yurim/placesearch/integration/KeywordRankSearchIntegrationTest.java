package com.yurim.placesearch.integration;

import com.yurim.placesearch.search.domain.SearchQuery;
import com.yurim.placesearch.search.repository.SearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
public class KeywordRankSearchIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    public static String KEYWORD_RANK_SEARCH = "/v1/search/rank/keyword";

    public HttpEntity httpEntity;

    @Autowired
    public SearchRepository searchRepository;

    @BeforeEach
    public void prepare(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpEntity = new HttpEntity<>(httpHeaders);
        setUpData();
    }


    public void setUpData(){

        SearchQuery searchQuery = new SearchQuery("키워드 검색");
        searchRepository.save(searchQuery);

    }

    @Test
    @DisplayName("정상적인 검색키워드 목록 API 호출 대해 200리턴")
    public void keywordRequestReturn200() {

        ResponseEntity<String> response = restTemplate.exchange(KEYWORD_RANK_SEARCH, HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    @Test
    @DisplayName("데이터가 존재하는 요청에 대해 다음의 필드 포함")
    public void keywordResponseContains() {

        ResponseEntity<String> response = restTemplate.exchange(KEYWORD_RANK_SEARCH, HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getBody()).contains("keyword");
        assertThat(response.getBody()).contains("searchCount");
        assertThat(response.getBody()).contains("rankedKeywords");
        assertThat(response.getBody()).contains("total");

    }


}
