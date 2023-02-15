package com.yurim.placesearch.integration;

import com.yurim.placesearch.search.domain.PlaceSearchResponse;
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
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
public class PlaceSearchIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    public static String SEARCH_PLACE_PATH = "/v1/search/place";

    public HttpEntity httpEntity;

    @BeforeEach
    public void prepare(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpEntity = new HttpEntity<>(httpHeaders);
    }

    @Test
    @DisplayName("정상적인 키워드 검색 요청에 대해 정상 응답")
    public void keywordRequestReturn200() {

        String uri = SEARCH_PLACE_PATH + "?keyword={keyword}";
        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("keyword","카카오");
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class, queryParams);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }



    @Test
    @DisplayName("키워드 검색 응답이 1개 이상일 시 다음 필드를 포함하고 있어야 한다.")
    public void responseContainValues() {

        String uri = SEARCH_PLACE_PATH + "?keyword={keyword}";
        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("keyword","은행");
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class, queryParams);
        assertThat(response.getBody()).contains("placeName");
        assertThat(response.getBody()).contains("address");
        assertThat(response.getBody()).contains("roadAddress");
        assertThat(response.getBody()).contains("x");
        assertThat(response.getBody()).contains("y");

    }


    @Test
    @DisplayName("keyword 파라미터가 없으면 오류발생")
    public void noKeywordParamReturn400Error() {

        String uri = SEARCH_PLACE_PATH + "?keyword={keyword}";
        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("keyword",null);
        ResponseEntity<PlaceSearchResponse> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, PlaceSearchResponse.class, queryParams);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }



    /*
     * 정상 동작할때는 테스트 불가 (연동 오류상황 테스트)
    @Test
    @DisplayName("키워드 검색 외부 API 커넥션 오류발생")
    @Deprecated
    public void externalApiCallReturn503Error() {

        String uri = SEARCH_PLACE_PATH + "?keyword={keyword}";
        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("keyword","외부API");
        ResponseEntity<PlaceSearchResponse> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, PlaceSearchResponse.class, queryParams);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);

    }

     */




}
