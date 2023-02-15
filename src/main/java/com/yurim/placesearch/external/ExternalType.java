package com.yurim.placesearch.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExternalType {

    KAKAO("kakaoSearchService",0),
    NAVER("naverSearchService",1),

    ;

    private String beanName;
    private int order;
}
