package com.code_n_droid.dwell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import com.code_n_droid.dwell.databinding.AddAddressDailogBinding;
import com.code_n_droid.dwell.databinding.AddressDetailsDialogBinding;
import com.code_n_droid.dwell.databinding.FromJsonDialogBinding;
import com.code_n_droid.dwell.databinding.MyLocationDailogBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.code_n_droid.dwell.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;
    private MarkerBuilder markerBuilder;
    private AlertDialog addAddressDialog, addressDetailsDialog, myLocationDialog, enterJsonDialog;
    private Marker myLocation;

    private final List<Marker> deliveryLocations = new ArrayList<>();

    private AddressDetailsDialogBinding addBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markerBuilder = new MarkerBuilder(getLayoutInflater());

        com.code_n_droid.dwell.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate ( getLayoutInflater () );
        setContentView( binding.getRoot());

        db = FirebaseFirestore.getInstance ();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buildAddAddressDialog();

        buildAddressDetailsDialog();

        buildMyLocationDialog();

        buildEnterJSONDialog();

        binding.showAll.setOnClickListener( view -> {
            startActivity(new Intent(this, AddressListActivity.class));
        });

        binding.addAddress.setOnClickListener( view -> {
            addAddressDialog.show();
        });

        binding.myLocation.setOnClickListener( view -> {
            myLocationDialog.show();
        });

        DataBase.getCustomerDetailsLD().observe( this , this::addMarkers );

        getMyDataFromServer();
    }

    private void getMyDataFromServer ( ) {
        ArrayList<CustomerDetail> customerDetailArrayList = new ArrayList <> ();
        db.collection ( "myData" )
                .get ()
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        for ( QueryDocumentSnapshot document : task.getResult()) {
                            CustomerDetail cd = document.toObject ( CustomerDetail.class );
                            customerDetailArrayList.add ( cd );
                        }

                        DataBase.setCustomerDetailsist ( customerDetailArrayList );
                    } else {
                        Toast.makeText ( MapsActivity.this , "Something went while fetching data" , Toast.LENGTH_SHORT ).show ();
                    }
                } );
    }

    private void uploadMyDataToServer ( ) {
        List<CustomerDetail> customerDetailList = DataBase.getData ().getCustomerDetails ();
        WriteBatch batch = db.batch ();

        for( CustomerDetail cd: customerDetailList){
            batch.set ( db.collection ( "myData" ).document (cd.getId ()), cd );
        }

        batch.commit ().addOnSuccessListener ( unused -> {
            Toast.makeText ( this , "Data Saved" , Toast.LENGTH_SHORT ).show ();
        } ).addOnFailureListener ( e -> {
            Toast.makeText ( this , "Failed To Save Data" , Toast.LENGTH_SHORT ).show ();
        } );
    }

    private void buildEnterJSONDialog() {
        FromJsonDialogBinding binding = FromJsonDialogBinding.inflate(getLayoutInflater());

        enterJsonDialog = new AlertDialog.Builder(this)
                .setView(binding.getRoot())
                .create();

        enterJsonDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.cancel.setOnClickListener(view -> enterJsonDialog.dismiss());

        binding.save.setOnClickListener(view -> {
            String json = binding.json.getText().toString();
            if(json.isEmpty()){
                Toast.makeText(this, "Enter JSON Data", Toast.LENGTH_LONG).show();
            }else{
                try {
                    DataBase.extractFromJsonString(json);
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid JSON Data", Toast.LENGTH_LONG).show();
                }
                enterJsonDialog.dismiss();
            }
        });
    }

    private void buildMyLocationDialog() {
        MyLocationDailogBinding binding = MyLocationDailogBinding.inflate(getLayoutInflater());

        myLocationDialog = new AlertDialog.Builder(this)
                .setView(binding.getRoot())
                .create();

        myLocationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.latitude.setText("12.93149700");
        binding.longitude.setText("77.67884600");

        binding.save.setOnClickListener(view -> {
            double latitude = Double.parseDouble(binding.latitude.getText().toString());
            double longitude = Double.parseDouble(binding.longitude.getText().toString());
            DataBase.lat = latitude;
            DataBase.lon = longitude;
            myLocation.setPosition(new LatLng(latitude, longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
            myLocationDialog.dismiss();
        });

        binding.cancel.setOnClickListener(view -> myLocationDialog.dismiss());
    }

    private void buildAddAddressDialog() {
        AddAddressDailogBinding binding = AddAddressDailogBinding.inflate(getLayoutInflater());

        addAddressDialog = new AlertDialog.Builder(this)
                .setView(binding.getRoot())
                .create();

        addAddressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.fromJSONFile.setOnClickListener(view -> {
            // Implement json read
            addAddressDialog.dismiss();
//            enterJsonDialog.show();
            loadData();
        });

        binding.addManually.setOnClickListener(view -> {
            startActivity(new Intent(this, AddDeliveryAddressActivity.class));
            addAddressDialog.dismiss();
        });
    }

    private void loadData ( ) {
        db.collection ( "JSON_DATA" ).document ("data")
                .get ()
                .addOnSuccessListener ( documentSnapshot -> {
                    String json = documentSnapshot.getString ( "json_string" );
                    try {
                        DataBase.extractFromJsonString ( json );
                        new Handler ().postDelayed ( (Runnable) this::uploadMyDataToServer , 10000 );
                    } catch (Exception e) {
                        Toast.makeText ( MapsActivity.this , "Invalid Json" , Toast.LENGTH_SHORT ).show ();
                    }
                } )
                .addOnFailureListener ( e -> {
                    Toast.makeText ( this , "Something went wrong" , Toast.LENGTH_SHORT ).show ();
                } );
    }

    private void buildAddressDetailsDialog(){
        addBinding = AddressDetailsDialogBinding.inflate(getLayoutInflater());

        addressDetailsDialog = new AlertDialog.Builder(this)
                .setView(addBinding.getRoot())
                .create();

        addressDetailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;
    }

    public void showDetailsDialog(int in){
        System.out.println(in);
        CustomerDetail customerDetail = DataBase.getData().getCustomerDetails().get(in);
        if(customerDetail != null){
            CustomerAddress address = customerDetail.getCustomerAddress();
            addBinding.name.setText(customerDetail.getCustomerName());
            addBinding.phoneNo.setText(address.getPhoneNo());

            String addressString = address.getFlat() + ", " + address.getBuilding()+", "
                    + address.getWing()+" wing, "+ address.getInstructions() + ", "+
                    address.getArea() + ", "+ address.getCity()+"\n"+address.getGoogleAddressData();

            addBinding.address.setText(addressString);

            addBinding.call.setOnClickListener(view -> {
                call(address.getPhoneNo());
            });

            addBinding.navigateMe.setOnClickListener(view -> {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+address.getLatLong().getLatitude()+","+address.getLatLong().getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            });

            addBinding.markDelivered.setEnabled ( !customerDetail.getVisited () );

            addBinding.markDelivered.setOnClickListener( view -> {
                deliveryLocations.get( in ).setIcon( markerBuilder.getNumberedMarker(customerDetail.getCustomerAddress().getLatLong().getRank(), MarkerBuilder.MARKER_TYPE_DELIVERED )  );
                customerDetail.setVisited ( true );
                uploadMyDataToServer ();
                addressDetailsDialog.dismiss();
            } );

            addBinding.canNotDeliver.setOnClickListener( view -> {
                deliveryLocations.get( in ).setIcon( markerBuilder.getNumberedMarker(customerDetail.getCustomerAddress().getLatLong().getRank(), MarkerBuilder.MARKER_TYPE_CAN_NOT_DELIVERED )  );
                customerDetail.setCanDeliver ( false );
                uploadMyDataToServer ();
                addressDetailsDialog.dismiss();
            } );
        }
        addressDetailsDialog.show();
    }

    private void call(String phoneNo) {

        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CALL_PHONE }, 100);
        }
        else{
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNo));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addCurrLoc();

        mMap.setOnMarkerClickListener(marker -> {
            int index = deliveryLocations.indexOf( marker );
            if(index >= 0){
                showDetailsDialog( index );
            }
            return true;
        });
    }

    private void addCurrLoc ( ) {
        LatLng myLoc = new LatLng(12.93149700, 77.67884600);
        DataBase.lat = 12.93149700;
        DataBase.lon = 77.67884600;

        myLocation = mMap.addMarker(new MarkerOptions()
                .position(myLoc)
                .icon(markerBuilder.getMyLocationMarker())
                .title("My Location")
                .zIndex ( 2f )
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
    }

    public void addMarkers(List<CustomerDetail> customerDetails){
        mMap.clear();
        deliveryLocations.clear ();
        addCurrLoc ();

        for(int i=0;i< customerDetails.size();i++){
            LatLong latLong = customerDetails.get( i ).getCustomerAddress().getLatLong();
            LatLng latLng = new LatLng( Double.parseDouble( latLong.getLatitude() ), Double.parseDouble(latLong.getLongitude() ));

            int markerType;

            if(customerDetails.get ( i ).getVisited ()){
                markerType = MarkerBuilder.MARKER_TYPE_DELIVERED;
            }else if(!customerDetails.get ( i ).getCanDeliver ()){
                markerType = MarkerBuilder.MARKER_TYPE_CAN_NOT_DELIVERED;
            }else{
                markerType = MarkerBuilder.MARKER_TYPE_UN_DELIVERED;
            }

            deliveryLocations.add( mMap.addMarker( new MarkerOptions()
            .position(latLng).icon( markerBuilder.getNumberedMarker(latLong.getRank(), markerType ))
            ));
        }
    }
}