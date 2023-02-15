package com.yurim.placesearch.external.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class KakaoPlaceSearchResponse {

    private PlaceMeta meta;

    private List<PlaceDocument> documents;

    public static KakaoPlaceSearchResponse getFaultReturn() {

        PlaceMeta meta = new PlaceMeta(0);
        KakaoPlaceSearchResponse response = new KakaoPlaceSearchResponse(meta, new ArrayList<>());
        return response;

    }

}
