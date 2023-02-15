package com.yurim.placesearch.external.dto.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yurim.placesearch.search.domain.SearchQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
public class KakaoPlaceSearchRequest extends SearchQuery {

    private String query;

    private CATEGORY_GROUP_CODE categoryGroupCode;

    private String x;

    private String y;

    private String radius;

    private String rect;

    private int page;

    private int size;

    private SORT_TYPE sort;




    @Getter
    @AllArgsConstructor
    public enum CATEGORY_GROUP_CODE {

        BK9("은행"),
        MT1("대형마트"),
        CS2("편의점"),
        PS3("어린이집, 유치원"),
        SC4("학교"),
        AC5("학원"),
        PK6("주차장"),
        OL7("주유소, 충전소"),
        SW8("지하철역"),
        CT1("문화시설"),
        AG2("중개업소"),
        PO3("공공기관"),
        AT4("관광명소"),
        AD5("숙박"),
        FD6("음식점"),
        CE7("카페"),
        HP8("병원"),
        PM9("약국")

        ;

        private String description;

    }

    public enum SORT_TYPE {
        accuracy,
        distance
    }

}
