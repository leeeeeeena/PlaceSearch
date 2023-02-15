package com.yurim.placesearch.external.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceMeta {

    private int total_count;

    private int pageable_count; //total_cnt 중 노출 가능 문서 수 (max 45)

    private boolean is_end; // 현재 페이지가 마지막 헤이지인지

    private RegionInfo same_name;

    public PlaceMeta(int total_count) {
        this.total_count = total_count;
    }
}
