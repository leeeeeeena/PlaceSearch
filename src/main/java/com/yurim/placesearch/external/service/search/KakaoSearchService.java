package com.yurim.placesearch.external.service.search;

import com.yurim.placesearch.common.RestExchangeUtil;
import com.yurim.placesearch.external.service.modifier.KakaoPlaceResponseModifier;
import com.yurim.placesearch.external.dto.kakao.KakaoPlaceSearchRequest;
import com.yurim.placesearch.external.dto.kakao.KakaoPlaceSearchResponse;
import com.yurim.placesearch.external.dto.kakao.PlaceDocument;
import com.yurim.placesearch.search.domain.Place;
import com.yurim.placesearch.search.domain.SearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Qualifier("KAKAO")
@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoSearchService implements ExternalSearchService <Place>{

    @Value("${external.kakao.host}")
    private String host;

    @Value("${external.kakao.search-uri}")
    private String searchPath;

    @Value("${external.kakao.api-key}")
    private String apiKey;

    private final RestExchangeUtil restExchangeUtil;

    private final int SEARCH_SIZE = 5;

    private final KakaoPlaceResponseModifier modifier;

    private final CircuitBreakerFactory circuitBreakerFactory;

    private CircuitBreaker circuitBreaker;

    @PostConstruct
    private void init() {
        circuitBreaker = circuitBreakerFactory.create("KAKAO_OPEN_API");
    }

    @Override
    public ResponseEntity callExternalApi(SearchQuery searchQuery) {

        String authorization = getAuthorization();

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",authorization);

        KakaoPlaceSearchRequest searchRequest = KakaoPlaceSearchRequest.builder()
                .query(searchQuery.getKeyword())
                .page(1)
                .size(SEARCH_SIZE)
                .sort(KakaoPlaceSearchRequest.SORT_TYPE.accuracy)
                .build();

        Map queryParams = restExchangeUtil.buildQueryParams(searchRequest);

        log.debug("Request Param: {}",queryParams);
        //FIXME: EvaluateException : NoFallbackAvailableException 발생. circuitbreaker와 restemplate 호환이 잘 안되는 듯...
//        ResponseEntity responseEntity = circuitBreaker.run(() -> restExchangeUtil.get(headers, host, searchPath, queryParams, KakaoPlaceSearchResponse.class),
//                throwable -> ResponseEntity.badRequest().body(KakaoPlaceSearchResponse.getFaultReturn()));
        ResponseEntity<KakaoPlaceSearchResponse> responseEntity = restExchangeUtil.get(headers, host, searchPath, queryParams, KakaoPlaceSearchResponse.class);
        return responseEntity;
    }

    @Override
    public List<Place> search(SearchQuery searchQuery) {
        ResponseEntity<KakaoPlaceSearchResponse> responseEntity = this.callExternalApi(searchQuery);
        KakaoPlaceSearchResponse response = responseEntity.getBody();
        if (response == null || response.getDocuments() == null) {
            return new ArrayList<>();
        }
        List<PlaceDocument> documents = response.getDocuments();
        List<Place> places = documents.stream()
                .map(document -> modifier.modify(document))
                .collect(Collectors.toList());
        return places;
    }

    private String getAuthorization() {
        return new StringBuilder("KakaoAK ")
                .append(apiKey).toString();
    }
}
