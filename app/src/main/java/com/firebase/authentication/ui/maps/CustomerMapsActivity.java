package com.firebase.authentication.ui.maps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;

import com.firebase.authentication.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomerMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    Button book_request_btn;
    private LatLng customerPickupLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        book_request_btn = findViewById(R.id.booking_request);

        book_request_btn.setOnClickListener(view -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("customerRequests");
            GeoFire geoFire = new GeoFire(myRef);
            geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

            customerPickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            book_request_btn.setText("Searching for nearest equipment owner available...");
            mMap.addMarker(new MarkerOptions().position(customerPickupLocation).title("This is my Location!"));
            getNearbyEquipmentOwners();
        });
    }



    Boolean equipment_owner_found = false;
    int radius = 1;
    String equipment_owner_found_id;
    private void getNearbyEquipmentOwners() {

        DatabaseReference equipment_owner_location= FirebaseDatabase.getInstance().getReference().child("equipmentOwnersAvailable");
        GeoFire geoFire = new GeoFire(equipment_owner_location);

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(customerPickupLocation.longitude, customerPickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!equipment_owner_found){
                    equipment_owner_found = true;
                    equipment_owner_found_id = key;

                    DatabaseReference farm_equipment_owner_Ref = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child("EquipmentOwner").child(equipment_owner_found_id);
                    String customerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    HashMap map = new HashMap();
                    map.put("customerOrderId", customerId);
                    farm_equipment_owner_Ref.updateChildren(map);
                    getEquipmentOwnerLocation();
                    book_request_btn.setText("Looking for Equipment Owners Location.....");
                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!equipment_owner_found){
                    radius++;
                    getNearbyEquipmentOwners();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private Marker equipmentOwnerMarker;
    private void getEquipmentOwnerLocation() {
        DatabaseReference equipment_owner_location_Ref = FirebaseDatabase.getInstance().getReference()
                .child("EquipmentOwnersWorking").child(equipment_owner_found_id).child("l");
        equipment_owner_location_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<Object> map = (List<Object>)snapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    book_request_btn.setText("Equipment Owner Found");

                    assert map != null;
                    if (map.get(0) != null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }

                    if (map.get(1) != null){
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }

                    LatLng equipment_owner_latlng = new LatLng(locationLat, locationLng);

                    if (equipmentOwnerMarker != null){
                        equipmentOwnerMarker.remove();
                    }

                    // location of the emergent farmer
                    Location loc1 = new Location("");
                    loc1.setLatitude(customerPickupLocation.latitude);
                    loc1.setLongitude(customerPickupLocation.longitude);

                    // location of the equipment owner
                    Location loc2 = new Location("");
                    loc2.setLatitude(equipment_owner_latlng.latitude);
                    loc2.setLongitude(equipment_owner_latlng.longitude);

                    float distance = loc1.distanceTo(loc2);
                    book_request_btn.setText("Equipment Owners Found: " + distance);
                    equipmentOwnerMarker = mMap.addMarker(new MarkerOptions().position(equipment_owner_latlng).title("Your Equipment Owner.."));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mMap = googleMap;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            }else{
                checkLocationPermission();
            }
        }
        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);
    }


    LocationCallback locationCallback = new LocationCallback() {
        // method that checks for any changes in location of the Equipment owner.
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mLastLocation = location;

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                        if(!getEquipmentOwnersAroundStarted){
                            getEquipmentOwnersAround();
                        }
                    }
                }
            }
    };

    private void checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Provide Permission")
                        .setMessage("Allow App to Access the Map")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(CustomerMapsActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(CustomerMapsActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    // method that makes sure the user is permitted to access the location services dependent on the handset their using.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    } else {
                        Toast.makeText(this, "Please Provide Permission!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    }

    boolean getEquipmentOwnersAroundStarted = false;
    List <Marker> markerList = new ArrayList<Marker>();
    public void getEquipmentOwnersAround() {
        getEquipmentOwnersAroundStarted = true;
        DatabaseReference equipment_owners_location = FirebaseDatabase.getInstance().getReference().child(("equipmentOwnersAvailable"));

        GeoFire geoFire = new GeoFire(equipment_owners_location);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 100);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                for(Marker markerIt : markerList){
                    if(Objects.requireNonNull(markerIt.getTag()).equals(key)){
                        return;
                    }

                    LatLng equipmentOwnersLocation = new LatLng(location.latitude, location.longitude);

                    Marker mEquipmentOwnerMarker = mMap.addMarker(new MarkerOptions().position(equipmentOwnersLocation).title(key));
                    assert mEquipmentOwnerMarker != null;
                    mEquipmentOwnerMarker.setTag(key);

                    markerList.add(mEquipmentOwnerMarker);
                }
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker markerIt : markerList) {
                    if (Objects.requireNonNull(markerIt.getTag()).equals(key)) {
                        markerIt.remove();
                        markerList.remove(markerIt);
                        return;
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker markerIt : markerList) {
                    if (Objects.requireNonNull(markerIt.getTag()).equals(key)) {
                        markerIt.setPosition(new LatLng(location.latitude, location.longitude));
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
}