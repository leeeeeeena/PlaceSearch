package com.yurim.placesearch.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yurim.placesearch.external.ExternalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
public class Place {

    private String placeName;

    private String address; //지번

    private String roadAddress; //도로명

    private String x;

    private String y;

    private String phoneNumber;
    @JsonIgnore
    private ExternalType externalType;

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Place otherPlace = (Place) other;

        return refineName(this.placeName).equals(refineName(otherPlace.placeName)) &&
                refineName(this.address).equals(refineName(otherPlace.address));
    }

    @Override
    public int hashCode() {
        return address.trim().hashCode();
    }

    private String refineName(String placeName){
        String refined = placeName.replaceAll("<b>", "")
                .replaceAll("</b>", "")
                .replaceAll("특별시", "")
                .replaceAll("광역시", "")
                .replaceAll(" ","");
        return refined;
    }

}
