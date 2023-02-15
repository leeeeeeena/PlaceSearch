package com.yurim.placesearch.external.service.search;

import com.yurim.placesearch.external.ExternalType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExternalSearchServiceRouter {

    @Autowired
    Map<String,ExternalSearchService> externalSearchServiceMap;

    public ExternalSearchService getImplementationByType(ExternalType type) {
        return externalSearchServiceMap.get(type.getBeanName());
    }

}
