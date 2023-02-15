package com.yurim.placesearch.external.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDocument
{

    private String id;

    private String place_name;

    private String category_name;

    private String category_group_code;

    private String category_group_name;

    private String phone;

    private String address_name; //전체 지번 주소

    private String road_address_name; //전체 도로명 주소

    private String x;

    private String y;

    private String place_url;

    private String distance; // 중심좌표까지의 거리 (m)

}
