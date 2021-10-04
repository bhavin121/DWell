package com.code_n_droid.dwell;

import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

public abstract class DataBase {

    public static double lat, lon;
    private static Example example = new Example();
    private static Data data = new Data();
    private static MutableLiveData<List<CustomerDetail>> customerDetailsLD = new MutableLiveData <>();

    public static MutableLiveData < List < CustomerDetail > > getCustomerDetailsLD ( ) {
        return customerDetailsLD;
    }

    static {
        data.setCustomerDetails(new ArrayList<>());
        example.setData(data);
        customerDetailsLD.postValue ( data.getCustomerDetails () );
    }

    public static void extractFromJsonString(String json) throws Exception{
        example = new GsonBuilder().create().fromJson(json, Example.class);
        data = example.getData();

        new PathTask().execute( data );
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
        customerDetailsLD.postValue ( data.getCustomerDetails () );
    }

    public static CustomerDetail getCustomerDetailsById(String id){
        System.out.println(id);
        List<CustomerDetail> customerDetailList = data.getCustomerDetails();

        for(int i=0;i<customerDetailList.size();i++){
            if(customerDetailList.get(i).getId().equals(id)){
                System.out.println(customerDetailList.get( i ).getId());
                return  customerDetailList.get(i);
            }
        }

        return null;
    }

    public static void setCustomerDetailsist ( List < CustomerDetail > customerDetails ) {
        data.setCustomerDetails ( customerDetails );
        customerDetailsLD.postValue ( customerDetails );
    }

    public static Data getData ( ) {
        return example.getData();
    }

    static class PathTask extends AsyncTask<Data, Void, Void> {
        
        @Override
        protected Void doInBackground ( Data... data ) {
//            new Path().rankaddress(data[0]);
            new Path ().rank( data[0], lat, lon );

//            for(int i=0;i<data[0].getCustomerDetails().size();i++){// Dummy Ranking
//                data[0].getCustomerDetails().get(i)
//                        .getCustomerAddress()
//                        .getLatLong()
//                        .setRank(i);
//            }// Dummy Ranking
            return null;
        }

        @Override
        protected void onPostExecute ( Void unused ) {
            super.onPostExecute( unused );
            customerDetailsLD.postValue ( data.getCustomerDetails () );
        }
    }
}
