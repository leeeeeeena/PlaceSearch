package com.yurim.placesearch.search.service;

import com.yurim.placesearch.external.ExternalType;
import com.yurim.placesearch.external.service.search.ExternalSearchService;
import com.yurim.placesearch.external.service.search.ExternalSearchServiceRouter;
import com.yurim.placesearch.search.domain.*;
import com.yurim.placesearch.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ExternalSearchServiceRouter externalSearchServiceRouter;

    private final ResultSort resultSort;

    private final SearchRepository searchRepository;

    @Override
    public PlaceSearchResponse searchPlace(SearchQuery search) {

        Map<ExternalType,List<Place>> placeMap = new HashMap<>();
        for(ExternalType externalType: ExternalType.values()) {
            List<Place> places = searchPlace(search, externalType);
            placeMap.put(externalType,places);
        }
        List<Place> resultPlaces = resultSort.sort(placeMap);
        PlaceSearchResponse placeSearchResponse = new PlaceSearchResponse(resultPlaces,resultPlaces.size());

        searchRepository.save(search);
        return placeSearchResponse;
    }


    private List<Place> searchPlace(SearchQuery search, ExternalType type) {
        ExternalSearchService searchService = externalSearchServiceRouter.getImplementationByType(type);
        List<Place> places = searchService.search(search);
        return places;
    }


    @Override
    public KeywordSearchRankResponse searchRankedKeyword() {

        List<KeywordSearchCount> rankedKeywords = searchRepository.getRankedKeywords();
        KeywordSearchRankResponse keywordSearchRankResponse = new KeywordSearchRankResponse(rankedKeywords.size(),rankedKeywords);
        return keywordSearchRankResponse;
    }
}
