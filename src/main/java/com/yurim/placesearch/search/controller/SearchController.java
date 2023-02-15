package com.yurim.placesearch.search.controller;

import com.yurim.placesearch.error.ErrorResponseCode;
import com.yurim.placesearch.error.exception.BadRequestException;
import com.yurim.placesearch.search.domain.KeywordSearchRankResponse;
import com.yurim.placesearch.search.domain.PlaceSearchResponse;
import com.yurim.placesearch.search.domain.SearchQuery;
import com.yurim.placesearch.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/search")
public class SearchController {

    private final SearchService searchService;

    /**
     * 장소 검색 API
     */
    @GetMapping("/place")
    public PlaceSearchResponse searchPlace(@RequestParam String keyword){

        if (StringUtils.isEmpty(keyword)) {
            throw new BadRequestException(ErrorResponseCode.REQUEST_PARAMETER_MISSING);
        }

        SearchQuery search = new SearchQuery(keyword);
        return searchService.searchPlace(search);
    }


    /**
     * 검색 키워드 목록 API
     */

    @GetMapping("/rank/keyword")
    public KeywordSearchRankResponse searchRankedKeyword(){

        return searchService.searchRankedKeyword();
    }

}
