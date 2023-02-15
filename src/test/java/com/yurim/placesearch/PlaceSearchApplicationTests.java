package com.yurim.placesearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurim.placesearch.external.ExternalType;
import com.yurim.placesearch.external.dto.kakao.KakaoPlaceSearchRequest;
import com.yurim.placesearch.external.dto.kakao.KakaoPlaceSearchResponse;
import com.yurim.placesearch.external.service.geo.GeoPoint;
import com.yurim.placesearch.external.service.geo.GeoTransferService;
import com.yurim.placesearch.external.service.search.KakaoSearchService;
import com.yurim.placesearch.external.service.search.NaverSearchService;
import com.yurim.placesearch.search.domain.Place;
import com.yurim.placesearch.search.domain.SearchQuery;
import com.yurim.placesearch.search.service.ResultSort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlaceSearchApplicationTests {

    /** 단위테스트 **/

    @Autowired
    private KakaoSearchService kakaoSearchService;

    @Autowired
    private NaverSearchService naverSearchService;

    @Autowired
    private GeoTransferService geoTransferService;

    @Autowired
    ResultSort<Place> resultSort;

    @Test
    @DisplayName("외부 API 호출을 위해 Class의 필드를 Map으로 변경할 수 있는지 테스트")
    void convertClassToMapTest() {

        KakaoPlaceSearchRequest request = KakaoPlaceSearchRequest.builder()
                .categoryGroupCode(KakaoPlaceSearchRequest.CATEGORY_GROUP_CODE.BK9)
                .query("은행")
                .size(15)
                .sort(KakaoPlaceSearchRequest.SORT_TYPE.accuracy)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.convertValue(request, Map.class);
        System.out.println(map);

    }


    @Test
    @DisplayName("KaKao open API 요청시 200 리턴")
    void callKakaoOpenApiReturn200() {

        SearchQuery query = new SearchQuery("은행");

        ResponseEntity<KakaoPlaceSearchResponse> responseEntity = kakaoSearchService.callExternalApi(query);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

    }


    @Test
    @DisplayName("Naver open API 요청시 200 리턴")
    public void callNaverOpenApiReturn200() {

        SearchQuery request = new SearchQuery("은행");

        ResponseEntity responseEntity = naverSearchService.callExternalApi(request);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

    }


    @Test
    @DisplayName("검색결과는 우선순위 전략에 의해 공통 > 카카오 > 네이버 순 정렬되어야 한다.")
    public void sortPlaceResultByPriority() {

        //given
        Map<ExternalType, List<Place>> placeMap = preparePlaceMap();

        //when
        List<Place> sortResult = resultSort.sort(placeMap);

        //then
        assertThat(sortResult.size()).isLessThan(10); //공통부분 제외
        assertThat(sortResult.get(0).getExternalType()).isEqualTo(ExternalType.KAKAO); // 카카오 데이터가 먼저
        assertThat(sortResult.get(sortResult.size()-1).getExternalType()).isEqualTo(ExternalType.NAVER); // 네이버 데이터는 카카오 다음

    }


    private static Map<ExternalType, List<Place>> preparePlaceMap() {
        Map<ExternalType, List<Place>> placeMap = new HashMap<>();


        String[] kakaoPlaceNames = {"카카오은행","국민은행","신한은행","토스","네이버은행"};
        String[] kakaoAddressNames = {"판교로 001","판교로 002","판교로 003","판교로 004","판교로 005"};
        String[] naverPlaceNames = {"카카오은행","국민은행","우리은행","토스","비씨은행"};
        String[] naverAddressNames = {"판교로 001","판교로 002","판교로 017","판교로 004","판교로 011"};

        List<Place> kakaoPlaces = new ArrayList<>();
        List<Place> naverPlaces= new ArrayList<>();

        for (int i = 0; i < 5 ; i++) {
            Place kakaoPlace = Place.builder()
                    .placeName(kakaoPlaceNames[i])
                    .address(kakaoAddressNames[i])
                    .externalType(ExternalType.KAKAO)
                    .build();
            kakaoPlaces.add(kakaoPlace);
            Place naverPlace = Place.builder()
                    .placeName(naverPlaceNames[i])
                    .address(naverAddressNames[i])
                    .externalType(ExternalType.NAVER)
                    .build();
            naverPlaces.add(naverPlace);
        }

        placeMap.put(ExternalType.KAKAO,kakaoPlaces);
        placeMap.put(ExternalType.NAVER,naverPlaces);
        return placeMap;
    }

    @Test
    @DisplayName("KATEC 좌표계 GEO 좌표로 변환 테스트")
    public void convertKatecToGeo() {

        //given
        String resultMapX = "310064";
        String resultMapY = "551629";

        //when
        GeoPoint geoPoint = geoTransferService.transferCoordinate(resultMapX, resultMapY);

        //then
        // 대한민국 경도 124~132 사이, 위도 33~43 사이
        assertThat(geoPoint.getGeoX()).isGreaterThan("124");
        assertThat(geoPoint.getGeoX()).isLessThan("132");
        assertThat(geoPoint.getGeoY()).isGreaterThan("33");
        assertThat(geoPoint.getGeoY()).isLessThan("43");

    }



}
