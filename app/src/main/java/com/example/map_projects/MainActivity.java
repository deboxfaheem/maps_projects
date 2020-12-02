package com.example.map_projects;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;

import com.example.map_projects.Adapter.SendAdapter;
import com.example.map_projects.R;
import com.example.map_projects.SetRecyclerview_data_Activity;
import com.example.map_projects.model.Demo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    protected GeoDataClient mGeoDataClient;
    GoogleApiClient mGoogleApiClient;
    public static final int REQUEST_CHECK_SETTINGS = 0x17;
    private FloatingActionButton floatingActionButton;
    private Marker marker;
    private String address;
    private LatLngBounds.Builder builderMap;
    private ImageView backpage;
    Location currentLocation;
    private NestedScrollView bootamsheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private View view;
    private static final int REQUEST_CODE = 101;
    private TextView LocationAddress, saveAddress;
    private LinearLayout nickNameLayout;
    FusedLocationProviderClient fusedLocationProviderClient;
    SendAdapter sendAdapter;
    EditText name, street, flatno, title;
    ArrayList<Demo> demolist;
    ArrayList<Demo> olddata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        displayLocationSettingsRequest(MainActivity.this);
        floatingActionButton = findViewById(R.id.floationcheckbutton);
        bootamsheet = findViewById(R.id.bootamsheet);
        backpage = findViewById(R.id.back);
        name = findViewById(R.id.name);
        nickNameLayout = findViewById(R.id.nickNameLayout);
        street = findViewById(R.id.streett);
        flatno = findViewById(R.id.flatNo);
        title = findViewById(R.id.titlee);
        LocationAddress = findViewById(R.id.textaddress);
        saveAddress = findViewById(R.id.saveaddress);
        view = findViewById(R.id.bg);
        demolist = new ArrayList<>();
        olddata = new ArrayList<>();
        sendAdapter = new SendAdapter(this, demolist);
        olddata.addAll(demolist);
        bottomSheetBehavior = BottomSheetBehavior.from(bootamsheet);
        fetchLocation();
        bottomSheetBehavior.setPeekHeight(0);
        mGoogleApiClient = new GoogleApiClient
                .Builder(MainActivity.this)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .addApi(com.google.android.gms.location.places.Places.PLACE_DETECTION_API)
                .enableAutoManage(MainActivity.this, this)
                .build();
        mGoogleApiClient.connect();
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    view.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                view.setVisibility(View.VISIBLE);
                view.setAlpha(slideOffset);
            }
        });

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertMethod((String.valueOf(name.getText().toString())),(String.valueOf(flatno.getText().toString())),(String.valueOf(street.getText().toString())),(String.valueOf(title.getText().toString())));
                Intent intent = new Intent(getApplicationContext(), SetRecyclerview_data_Activity.class);
                intent.putExtra("selectedList", (Serializable)  demolist);
                startActivity(intent);
            }
        });
    }
    private void insertMethod(String name, String flatno, String street,String title) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("flat", flatno);
            jsonObject.put("street", street);
            jsonObject.put("title", title);
            Demo demos = gson.fromJson(String.valueOf(jsonObject), Demo.class);
            demolist.add(demos);
            sendAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        private void displayLocationSettingsRequest (Context context) {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult result1) {
                    final Status status = result1.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            SupportMapFragment mapFragment = (SupportMapFragment) MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.map);
                            if (mapFragment != null) {
                                mapFragment.getMapAsync(MainActivity.this);
                            }
                            break;

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:


                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            alertDialogBuilder
                                    .setTitle(Html.fromHtml("<h4>" + MainActivity.this.getString(R.string.permissions_heading) + "</h4>"))
                                    .setMessage(Html.fromHtml("<h5>" + MainActivity.this.getString(R.string.location_enable_request) + "</h5>"))
                                    .setCancelable(false)
                                    .setPositiveButton(MainActivity.this.getString(android.R.string.ok),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    MainActivity.this.moveTaskToBack(true);
                                                    android.os.Process.killProcess(android.os.Process.myPid());
                                                    System.exit(0);
                                                }
                                            }).show();
                            break;
                    }
                }
            });
        }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        if (mapFragment != null) {
                            mapFragment.getMapAsync(this);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
            }
        }
        @Override
        public void onConnectionFailed (@NonNull ConnectionResult connectionResult){
        }
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onMarkerClick(Marker marker1) {
                floatingActionButton.setVisibility(View.VISIBLE);
                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryDark)));
                marker.showInfoWindow();
                getlocation();
                LocationAddress.setText(address);
                return true;
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
                mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(120000);
            mLocationRequest.setFastestInterval(120000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGeoDataClient = com.google.android.gms.location.places.Places.getGeoDataClient(MainActivity.this, null);
                builderMap = new LatLngBounds.Builder();
                getDeviceLocation();
            }
        }
            @SuppressLint("MissingPermission")
            private void getDeviceLocation() {
                try {
                    Task<Location> task = fusedLocationProviderClient.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLocation = location;
                                Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                                marker = mMap.addMarker(markerOptions);
                                marker.showInfoWindow();
                            } else {
                                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }

            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    List<Location> locationList = locationResult.getLocations();
                    LatLng latLng = new LatLng(locationList.get(0).getLatitude(), locationList.get(0).getLongitude());
//                    LatLng latLng = new LatLng(locationList.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                    marker = mMap.addMarker(markerOptions);
                    marker.showInfoWindow();

                }
            };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                    getDeviceLocation();
                }
                break;
        }
    }
    public List<Address> getlocation(){
        Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
        {
            try {
                List<Address> addresses=geocoder.getFromLocation(marker.getPosition().latitude,marker.getPosition().longitude,1);
                address= addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
