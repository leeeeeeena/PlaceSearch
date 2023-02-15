package com.yurim.placesearch.search.service;

import com.yurim.placesearch.external.ExternalType;

import java.util.List;
import java.util.Map;

public interface ResultSort <T> {

    List<T> sort(Map<ExternalType,List<T>> sortMap);

}
