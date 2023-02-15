package com.yurim.placesearch.search.service;

import com.yurim.placesearch.search.domain.KeywordSearchRankResponse;
import com.yurim.placesearch.search.domain.PlaceSearchResponse;
import com.yurim.placesearch.search.domain.SearchQuery;

public interface SearchService {
    PlaceSearchResponse searchPlace(SearchQuery search);

    KeywordSearchRankResponse searchRankedKeyword();
}
