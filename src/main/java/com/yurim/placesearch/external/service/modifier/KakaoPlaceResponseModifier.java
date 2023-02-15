package com.yurim.placesearch.external.service.modifier;

import com.yurim.placesearch.external.ExternalType;
import com.yurim.placesearch.external.dto.kakao.PlaceDocument;
import com.yurim.placesearch.search.domain.Place;
import org.springframework.stereotype.Component;

@Component
public class KakaoPlaceResponseModifier implements PlaceResponseModifier<PlaceDocument> {

    @Override
    public Place modify(PlaceDocument response) {

        Place place = Place.builder()
                .placeName(response.getPlace_name())
                .phoneNumber(response.getPhone())
                .address(response.getAddress_name())
                .roadAddress(response.getRoad_address_name())
                .x(response.getX())
                .y(response.getY())
                .externalType(ExternalType.KAKAO)
                .build();
        return place;
    }

}
