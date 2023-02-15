package com.yurim.placesearch.external.service.modifier;

import com.yurim.placesearch.search.domain.Place;

public interface PlaceResponseModifier<T>{
    Place modify(T response);
}
