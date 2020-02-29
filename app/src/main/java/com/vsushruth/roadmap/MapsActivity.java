package com.vsushruth.roadmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.animation.TimeAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.*;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Event;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private EditText mSearchText;
    private Button mSearchButton;
    private Button mAddCP;
    private ImageView mGps;
    private RelativeLayout mLayout;

    private Address address;
    private LatLng mPoint;

    private static final String TAG = MapsActivity.class.getSimpleName();

    private PlacesClient mPlacesClient;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Location mLastKnownLocation;
    LatLng cur = null;
    boolean curLocationflag = false;
    ArrayList<DB> DBArray = new ArrayList<>();

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        database.setPersistenceEnabled(true);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_maps);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        mPlacesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mSearchText = (EditText) findViewById(R.id.input_search);
        mSearchButton = (Button) findViewById(R.id.searchButton);
        mLayout = (RelativeLayout) findViewById(R.id.relLayout2);
        mAddCP = (Button) findViewById(R.id.addCP);
        mGps = (ImageView) findViewById(R.id.ic_gps);

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker placed"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                
                mPoint = latLng;
                mLayout.setVisibility(View.VISIBLE);


                List<Address> addresses = null;
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getSubLocality();
                final String cityName = addresses.get(0).getLocality();
                final String stateName = addresses.get(0).getAdminArea();

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.child(stateName).getChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                                    DB latLng1 = snapshot1.getValue(DB.class);
                                    Log.e(TAG, " " + snapshot1.child("latLng").child("latitude").getValue());
                                }
                            dataSnapshot.child(stateName + "/" + cityName).getValue().getClass();
//                            Log.e(TAG, "Exception: " + snapshot.getValue() + dataSnapshot.child(stateName + "/" + cityame).getValue().getClass());
                            }
                        }

//                Log.e(String.valueOf(Log.ERROR), dataSnapshot.getValue().toString());
//                for(i = ; i < )
//                {
//
//                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DB db = new DB();
//                Timestamp.getTime();
                db.setTimestamp(getTimeNow());
//                db.setState(stateName);
//                db.setCountry(countryName);
                db.setLatLng(latLng);

                myRef = database.getReference(stateName);
                if(cityName == null) {
                    myRef.child("Unspecified").push().setValue(db);
                }
                else {
                    myRef.child(cityName).push().setValue(db);
                }

            }
        });

         mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDeviceLocation();

            }
        });


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);

                String searchString = mSearchText.getText().toString();
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                List<Address> list = new ArrayList<>();

                try{
                    list = geocoder.getFromLocationName(searchString, 1);
                }catch (IOException e){
                    Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
                }

                if(list.size() > 0) {
                    address = list.get(0);
                    if (address.hasLatitude() && address.hasLongitude()) {
                        mPoint = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(mPoint));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPoint, DEFAULT_ZOOM));

                        mLayout.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(MapsActivity.this, "Unable to fetch data", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        mAddCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "Adsf");
                Toast.makeText(MapsActivity.this, "Click detected", Toast.LENGTH_SHORT).show();

                if(mPoint == null) {
                    Toast.makeText(MapsActivity.this, "Its fuckin null", Toast.LENGTH_LONG).show();
                }

                Bundle args = new Bundle();
                args.putParcelable("address_details", mPoint);

                //Intent intent = new Intent(MapsActivity.this, addCheckPoint.class);
                //intent.putExtra("bundle", args);
                //startActivity(intent);
            }
        });



    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                cur = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
//                                mLastKnownLocation.getLatitude(), cur.setLo mLastKnownLocation.getLongitude();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                Log.e(TAG, "in: " + cur);
                                curLocationflag = true;

                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(curLocationflag) {
                                            List<Address> addresses = null;
                                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                            try {
                                                addresses = geocoder.getFromLocation(cur.latitude, cur.longitude, 1);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

//        String address = addresses.get(0).getSubLocality();
                                            final String curCityName = addresses.get(0).getLocality();
                                            final String curStateName = addresses.get(0).getAdminArea();
//                                            Log.e(String.valueOf(Log.ERROR), "curLF " + dataSnapshot.child(curStateName).child(curCityName).getValue().toString());
                                            for(DataSnapshot dataSnapshot1: dataSnapshot.child(curStateName).child(curCityName).getChildren())
                                            {
                                                Log.e(String.valueOf(Log.ERROR), "curLF " + dataSnapshot1.getValue() + dataSnapshot1.child("latLng").child("latitude").getValue(double.class));
                                                DB d1 = new DB();
                                                d1.setLatLng(new LatLng(dataSnapshot1.child("latLng").child("latitude").getValue(double.class), dataSnapshot1.child("latLng").child("longitude").getValue(double.class)));
                                                DBArray.add(d1);
                                                mMap.addMarker(new MarkerOptions().position(d1.latLng).title("Marker1"));
//                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(d1.latLng));
                                                Log.e(String.valueOf(Log.ERROR), "curLF Arr" + d1.latLng);
                                            }
//
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            cur = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }


    }


    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private Long getTimeNow()
    {
        return System.currentTimeMillis()/1000;
    }

}


