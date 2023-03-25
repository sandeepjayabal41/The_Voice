package com.project.thevoice;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.project.thevoice.databinding.ActivityMapsBinding;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    FusedLocationProviderClient mFusedLocationClient;
    private PlacesClient mPlacesClient;
    Location location = null;
    double latitude, longitude;
    LatLng loc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        com.project.thevoice.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Places.initialize(getApplicationContext(), "AIzaSyCd83F00FOzhpe2nxTAKivZ7wKoGlGeWCY");
        mPlacesClient = Places.createClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Task<Location> getCurrentLocation = mFusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null);
        getCurrentLocation.addOnCompleteListener(getCurrentLocation1 -> {
            location = (Location) getCurrentLocation1.getResult();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            loc = new LatLng(latitude, longitude);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        });

        googleMap.setOnMapClickListener(latLng -> getPlaceId(latLng,googleMap));
    }

    private void getPlaceId(LatLng latLng,GoogleMap googleMap)
    {
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(Collections.singletonList(Place.Field.ID))
                .build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mPlacesClient.findCurrentPlace(request).addOnSuccessListener((response) -> {

            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                String placeId = placeLikelihood.getPlace().getId();

                Log.d("MapsActivity", "Place ID: " + placeId);

                // Get the details of the place
                List<Place.Field> placeFields = Arrays.asList(
                        Place.Field.NAME,
                        Place.Field.ADDRESS,
                        Place.Field.PHONE_NUMBER,
                        Place.Field.WEBSITE_URI,
                        Place.Field.RATING
                );
                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                mPlacesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener((place) ->
                {
                    // Show the place details in the UI
                    TextView nameTextView = findViewById(R.id.place_name);
                    RatingBar ratingBar = findViewById(R.id.place_rating);
                    TextView addressTextView = findViewById(R.id.place_address);
                    TextView phoneTextView = findViewById(R.id.place_phone_number);
                    TextView websiteTextView = findViewById(R.id.place_website);

                    nameTextView.setText(place.getPlace().getName());
                    ratingBar.setRating(Objects.requireNonNull(place.getPlace().getRating()).floatValue());
                    addressTextView.setText(place.getPlace().getAddress());
                    phoneTextView.setText(place.getPlace().getPhoneNumber());
                    websiteTextView.setText(Objects.requireNonNull(place.getPlace().getWebsiteUri()).toString());
                }).addOnFailureListener((exception) ->
                {
                    if (exception instanceof ApiException)
                    {
                        ApiException apiException = (ApiException) exception;
                        int statusCode = apiException.getStatusCode();
                        Log.e("MapsActivity", "Place not found: " + exception.getMessage());
                    }
                });
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                Log.e("MapsActivity", "Place not found: " + exception.getMessage());
            }
        });
    }
}