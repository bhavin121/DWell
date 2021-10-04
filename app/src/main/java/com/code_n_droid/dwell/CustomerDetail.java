
package com.code_n_droid.dwell;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class CustomerDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_address")
    @Expose
    private CustomerAddress customerAddress;
    @SerializedName("visited")
    @Expose
    private Boolean visited;

    private Boolean canDeliver = true;

    public Boolean getCanDeliver ( ) {
        return canDeliver;
    }

    public void setCanDeliver ( Boolean canDeliver ) {
        this.canDeliver = canDeliver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CustomerAddress getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(CustomerAddress customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

}
