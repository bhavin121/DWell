package com.code_n_droid.dwell;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Data {

    @SerializedName("customer_details")
    @Expose
    private List<CustomerDetail> customerDetails = null;

    public List<CustomerDetail> getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(List<CustomerDetail> customerDetails) {
        this.customerDetails = customerDetails;
    }
}
