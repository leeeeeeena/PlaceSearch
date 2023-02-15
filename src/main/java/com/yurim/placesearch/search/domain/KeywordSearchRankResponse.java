package com.yurim.placesearch.search.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KeywordSearchRankResponse {
    private int total;
    private List<KeywordSearchCount> rankedKeywords;

}
