package com.yurim.placesearch.search.repository;

import com.yurim.placesearch.search.domain.KeywordSearchCount;
import com.yurim.placesearch.search.domain.SearchQuery;

import java.util.List;

public interface SearchRepository {

    void save(SearchQuery searchQuery);

    List<KeywordSearchCount> getRankedKeywords();

}
