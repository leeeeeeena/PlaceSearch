package com.yurim.placesearch.search.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.ZSetOperations;

@Getter
@AllArgsConstructor
public class KeywordSearchCount {

    private String keyword;

    private int searchCount;

    public static KeywordSearchCount convertTypedTupleToKeywordSearchCount(ZSetOperations.TypedTuple<String> typedTuple) {
        KeywordSearchCount keywordSearchCount = new KeywordSearchCount(typedTuple.getValue(), typedTuple.getScore().intValue());
        return keywordSearchCount;
    }
}
