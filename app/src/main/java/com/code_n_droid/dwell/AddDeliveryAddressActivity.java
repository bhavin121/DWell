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


    }
}