package com.yurim.placesearch.external.service.geo;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GeoTransferServiceImpl implements GeoTransferService{

    @Override
    public GeoPoint transferCoordinate(String mapX, String mapY) {

        if (isNoCoordinate(mapX, mapY)) {
            return new GeoPoint("","");
        }

        double coorX = Double.parseDouble(mapX);
        double coorY = Double.parseDouble(mapY);

        GeoTransPoint naverPoint = new GeoTransPoint(coorX, coorY);
        GeoTransPoint geoLocationPoint = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, naverPoint);
        Double latitude = geoLocationPoint.getY();
        Double longitude = geoLocationPoint.getX();
        GeoPoint geoPoint = new GeoPoint(String.valueOf(longitude), String.valueOf(latitude));
        return geoPoint;
    }

    private static boolean isNoCoordinate(String mapX, String mapY) {
        return !StringUtils.hasText(mapX) || !StringUtils.hasText(mapY);
    }
}
