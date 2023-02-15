package com.yurim.placesearch.search.service;

import com.yurim.placesearch.external.ExternalType;
import com.yurim.placesearch.search.domain.Place;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PriorityPlaceResultSort implements ResultSort<Place>{


    @Override
    public List<Place> sort(Map<ExternalType, List<Place>> sortMap) {

        ExternalType[] externalTypes = ExternalType.values(); //values() 는 enum 정의한 순서대로 반환
        List<Place> highPriorityResults = sortMap.get(externalTypes[0]);
        List<Place> lowPriorityResults = sortMap.get(externalTypes[1]);

        List<Place> results = new ArrayList<>();

        for (Place kakaoResult : highPriorityResults) {
            if (checkDuplicate(lowPriorityResults, kakaoResult)) {
                results.add(kakaoResult);
                lowPriorityResults.remove(kakaoResult);
            }
        }

        highPriorityResults.removeAll(results);
        results.addAll(highPriorityResults);
        results.addAll(lowPriorityResults);

        return results;
    }

    private static boolean checkDuplicate(List<Place> naverResultSet, Place kakaoResult) {
        return naverResultSet.stream()
                .anyMatch(place -> place.equals(kakaoResult));
    }
}
