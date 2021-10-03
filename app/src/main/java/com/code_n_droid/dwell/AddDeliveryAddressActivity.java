package com.code_n_droid.dwell;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.code_n_droid.dwell.databinding.ActivityAddDeliveryAddressBinding;

public class AddDeliveryAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddDeliveryAddressBinding binding = ActivityAddDeliveryAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CustomerDetail customerDetail = new CustomerDetail();
        CustomerAddress customerAddress = new CustomerAddress();
        LatLong latLong = new LatLong();

        latLong.setLatitude(binding.latitude.getText().toString());
        latLong.setLongitude(binding.longitude.getText().toString());
        customerAddress.setLatLong(latLong);

        String phone = binding.phoneNo.getText().toString();
        customerAddress.setAddressId(phone);
        customerAddress.setPhoneNo(phone);
        customerAddress.setArea(binding.area.getText().toString());
        customerAddress.setBuilding(binding.building.getText().toString());
        customerAddress.setCity(binding.city.getText().toString());
        customerAddress.setFlat(binding.flatNo.getText().toString());
        customerAddress.setGoogleAddressData(binding.googleAddressData.getText().toString());
        customerAddress.setInstructions(binding.instructions.getText().toString());
        customerAddress.setWing(binding.wing.getText().toString());

        customerDetail.setCustomerAddress(customerAddress);
        customerDetail.setCustomerName(binding.name.getText().toString());
        customerDetail.setVisited(false);
        customerDetail.setId(binding.phoneNo.getText().toString());

        binding.cancel.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.add.setOnClickListener(view -> {
            DataBase.addAddressData(customerDetail);
            onBackPressed();
        });
    }
}