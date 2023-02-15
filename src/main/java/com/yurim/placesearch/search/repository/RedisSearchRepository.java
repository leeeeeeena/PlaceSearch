package com.yurim.placesearch.search.repository;

import com.yurim.placesearch.search.domain.KeywordSearchCount;
import com.yurim.placesearch.search.domain.SearchQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisSearchRepository implements SearchRepository{

    private final RedisTemplate redisTemplate;
    private final String REDIS_SEARCH_KEYWORD_KEY = "search:keyword";

    @Override
    public void save(SearchQuery searchQuery) {

        redisTemplate.opsForZSet().incrementScore(REDIS_SEARCH_KEYWORD_KEY,searchQuery.getKeyword(),1); //조회와 업데이트을 분리하지 않고 redis 자체 제공 함수를 사용하면 동시성 보장

    }

    @Override
    public List<KeywordSearchCount> getRankedKeywords() { //인기검색어

        Set<ZSetOperations.TypedTuple<String>> keywordByScores = redisTemplate.opsForZSet().reverseRangeWithScores(REDIS_SEARCH_KEYWORD_KEY,0,9);
        List<KeywordSearchCount> rankedKeywords = keywordByScores.stream().map(KeywordSearchCount::convertTypedTupleToKeywordSearchCount).collect(Collectors.toList());
        return rankedKeywords;
    }
}
