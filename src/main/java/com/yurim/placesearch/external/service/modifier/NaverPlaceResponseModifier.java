package com.yurim.placesearch.external.service.modifier;

import com.yurim.placesearch.external.ExternalType;
import com.yurim.placesearch.external.dto.naver.PlaceItem;
import com.yurim.placesearch.external.service.geo.GeoPoint;
import com.yurim.placesearch.external.service.geo.GeoTransferService;
import com.yurim.placesearch.search.domain.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverPlaceResponseModifier implements PlaceResponseModifier<PlaceItem> {

    private final GeoTransferService geoTransferService;

    @Override
    public Place modify(PlaceItem response) {

        GeoPoint geoPoint = geoTransferService.transferCoordinate(response.getMapx(), response.getMapy());

        Place place = Place.builder()
                .placeName(response.getTitle().replaceAll("<b>","").replaceAll("</b>",""))
                .phoneNumber(response.getTelephone())
                .address(response.getAddress())
                .roadAddress(response.getRoadAddress())
                .x(geoPoint.getGeoX())
                .y(geoPoint.getGeoY())
                .externalType(ExternalType.NAVER)
                .build();


        return place;
    }
}
