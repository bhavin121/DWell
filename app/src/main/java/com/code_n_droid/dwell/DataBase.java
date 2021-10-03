package com.code_n_droid.dwell;

import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

public abstract class DataBase {

    private static Example example = new Example();
    private static Data data = new Data();

    static {
        data.setCustomerDetails(new ArrayList<>());
        example.setData(data);
    }

    public static void extractFromJsonString(String json) throws Exception{
        example = new GsonBuilder().create().fromJson(json, Example.class);
        data = example.getData();
    }

    public static void addAddressData(CustomerDetail customerDetail){
        if(data == null){
            example.setData(new Data());
            data = example.getData();
        }

        List<CustomerDetail> customerDetailList = data.getCustomerDetails();
        if(customerDetailList == null){
            data.setCustomerDetails(new ArrayList<>());
            customerDetailList = data.getCustomerDetails();
        }

        customerDetailList.add(customerDetail);
    }

    public static CustomerDetail getCustomerDetailsById(String id){
        List<CustomerDetail> customerDetailList = data.getCustomerDetails();
        for(int i=0;i<customerDetailList.size();i++){
            if(customerDetailList.get(i).getId().equals(id)){
                return  customerDetailList.get(i);
            }
        }

        return null;
    }
}
