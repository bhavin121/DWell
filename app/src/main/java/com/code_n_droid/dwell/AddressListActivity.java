package com.code_n_droid.dwell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.code_n_droid.dwell.databinding.ActivityAddressListBinding;

public class AddressListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddressListBinding binding = ActivityAddressListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.list.setLayoutManager(new LinearLayoutManager( this ));
        binding.list.setAdapter(new AddressAdapter(DataBase.getData()));
    }
}