package com.yurim.placesearch.external.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionInfo {

    private List<String> region; //질의어에서 인식된 지역 리스트

    private String keyword; //지역정보를 제외한 키워드

    private String selected_region; //인식된 지역 리스트 중 현재 검색에 사용된 지역정보

}
