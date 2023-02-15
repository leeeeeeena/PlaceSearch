package com.yurim.placesearch.external.service.search;

import com.yurim.placesearch.search.domain.SearchQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExternalSearchService <T> {
    /** 외부 오픈 API 사용 **/

    ResponseEntity callExternalApi(SearchQuery searchQuery);

    List<T> search(SearchQuery searchQuery);
}
