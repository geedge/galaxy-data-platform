package com.mesalab.tool.domain;/**
 * Created by dell on 2018-10-8.
 */

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @ClassName CountryResponse
 * @Description TODO
 * @Author dell
 * @Date 2018-10-8 18:56
 * @Version 1.0
 **/
public class LocationResponse {

    private String areaCode;

    private String asn;

    private String city;

    private String country;

    private String isp;

    private String latitude;

    private String longitude;

    private String province;

    private String organization;

    private String countryCode;

    public LocationResponse() {

        this("", "", "", "", "", "", "", "", "", "");
    }

    public LocationResponse(@JsonProperty("AREA_CODE") String areaCode, @JsonProperty("ASN") String asn,
                            @JsonProperty("CITY") String city, @JsonProperty("COUNTRY") String country,
                            @JsonProperty("ISP") String isp, @JsonProperty("LATITUDE") String latitude,
                            @JsonProperty("LONGITUDE") String longitude, @JsonProperty("PROVINCE") String province,
                            @JsonProperty("ORGANIZATION") String organization, @JsonProperty("COUNTRY_CODE") String countryCode) {
        this.areaCode = areaCode;
        this.asn = asn;
        this.city = city;
        this.isp = isp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.province = province;
        this.country = country;
        this.organization = organization;
        this.countryCode = countryCode;

    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}

