
package com.code_n_droid.dwell;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class CustomerAddress {

    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("flat")
    @Expose
    private String flat;
    @SerializedName("building")
    @Expose
    private String building;
    @SerializedName("wing")
    @Expose
    private String wing;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("lat_long")
    @Expose
    private LatLong latLong;
    @SerializedName("google_address_data")
    @Expose
    private String googleAddressData;
    @SerializedName("instructions")
    @Expose
    private String instructions;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getWing() {
        return wing;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LatLong getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLong latLong) {
        this.latLong = latLong;
    }

    public String getGoogleAddressData() {
        return googleAddressData;
    }

    public void setGoogleAddressData(String googleAddressData) {
        this.googleAddressData = googleAddressData;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

}
