package com.yurim.placesearch.external.service.search;

import com.yurim.placesearch.common.RestExchangeUtil;
import com.yurim.placesearch.external.service.modifier.NaverPlaceResponseModifier;
import com.yurim.placesearch.external.dto.naver.PlaceItem;
import com.yurim.placesearch.external.dto.naver.NaverPlaceSearchRequest;
import com.yurim.placesearch.external.dto.naver.NaverPlaceSearchResponse;
import com.yurim.placesearch.search.domain.Place;
import com.yurim.placesearch.search.domain.SearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Qualifier("NAVER")
@Service
@RequiredArgsConstructor
@Slf4j
public class NaverSearchService implements ExternalSearchService <Place> {

    @Value("${external.naver.client-id}")
    private String clientId;

    @Value("${external.naver.client-secret}")
    private String clientSecret;

    @Value("${external.naver.host}")
    private String host;

    @Value("${external.naver.search-uri}")
    private String searchPath;

    private final String REQUEST_HEADER_CLIENT_ID = "X-Naver-Client-Id";

    private final String REQUEST_HEADER_CLIENT_SECRET = "X-Naver-Client-Secret";

    private final RestExchangeUtil restExchangeUtil;

    private final NaverPlaceResponseModifier modifier;

    private final int SEARCH_SIZE = 5;
    @Override
    public ResponseEntity callExternalApi(SearchQuery searchQuery) {

        Map<String,String> headers = new HashMap<>();
        headers.put(REQUEST_HEADER_CLIENT_ID,clientId);
        headers.put(REQUEST_HEADER_CLIENT_SECRET,clientSecret);

        NaverPlaceSearchRequest searchRequest = NaverPlaceSearchRequest.builder()
                .query(searchQuery.getKeyword())
                .display(SEARCH_SIZE)
                .start(1)
                .sort(NaverPlaceSearchRequest.SORT_TYPE.random)
                .build();

        Map queryParams = restExchangeUtil.buildQueryParams(searchRequest);

        log.debug("Request Param: {}",queryParams);
        ResponseEntity<NaverPlaceSearchResponse> responseEntity = restExchangeUtil.get(headers, host, searchPath, queryParams, NaverPlaceSearchResponse.class);
        return responseEntity;
    }

    @Override
    public List<Place> search(SearchQuery searchQuery) {
        ResponseEntity<NaverPlaceSearchResponse> responseEntity = this.callExternalApi(searchQuery);
        NaverPlaceSearchResponse response = responseEntity.getBody();
        if (response == null || response.getItems() == null) {
            return new ArrayList<>();
        }
        List<PlaceItem> placeItems = response.getItems();
        List<Place> places = placeItems.stream()
                .map(placeItem -> modifier.modify(placeItem))
                .collect(Collectors.toList());
        return places;
    }

}
