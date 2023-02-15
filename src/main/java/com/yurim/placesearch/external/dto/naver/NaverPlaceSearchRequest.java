package com.yurim.placesearch.external.dto.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yurim.placesearch.search.domain.SearchQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
public class NaverPlaceSearchRequest extends SearchQuery {

    private String query;

    private int display; // 한번에 표시할 결과 갯수 1~5

    private int start; // default : 1

    private SORT_TYPE sort;


    public enum SORT_TYPE {

        random, // 정확도순
        comment // 리뷰 개수순 (내림차순)
    }
}
