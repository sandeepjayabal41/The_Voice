package com.project.thevoice;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    FusedLocationProviderClient mFusedLocationClient;
    Location location = null;
    double latitude,longitude;
    LatLng loc = null;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SearchView searchView = findViewById(R.id.searchview);
        RecyclerView myRecyclerView = findViewById(R.id.recyclerView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        Button CarehomeButton = findViewById(R.id.CarehomeButton);
        CarehomeButton.getBackground().setAlpha(150);

        Button FoodbankButton = findViewById(R.id.FoodbankButton);
        FoodbankButton.getBackground().setAlpha(150);

        CarehomeButton.setOnClickListener(new View.OnClickListener()
        {
                @Override
                public void onClick(View view)
                {
                    try {
                        mMap.clear();
                        // Load JSON data from the assets folder
                        InputStream is = getAssets().open("careHomeData.json");
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();

                        // Convert the JSON data to a string
                        String json = new String(buffer, "UTF-8");

                        // Parse the JSON data into Java objects
                        JSONArray jsonArray = new JSONArray(json);

                        // Loop through the care home objects
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Extract the latitude and longitude fields
                            double latitude = jsonObject.getDouble("Latitude");
                            double longitude = jsonObject.getDouble("Longitude");

                            LatLng latLng = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(latLng).title(jsonObject.getString("Name")));
                        }
                    }
                    catch (IOException | JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
        });

        FoodbankButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try {
                    mMap.clear();
                    // Load JSON data from the assets folder
                    InputStream is = getAssets().open("foodBanksData.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();

                    // Convert the JSON data to a string
                    String json = new String(buffer, "UTF-8");

                    // Parse the JSON data into Java objects
                    JSONArray jsonArray = new JSONArray(json);

                    // Loop through the care home objects
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        // Extract the latitude and longitude fields
                        double latitude = jsonObject.getDouble("Latitude");
                        double longitude = jsonObject.getDouble("Longitude");

                        LatLng latLng = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(jsonObject.getString("Name")));
                    }
                }
                catch (IOException | JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                mMap.clear();
                Places.initialize(getApplicationContext(), "AIzaSyCd83F00FOzhpe2nxTAKivZ7wKoGlGeWCY");
                // Perform a search with the user's query text
                searchView.clearFocus();

                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // on below line we are adding marker to that position.
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {

                // React to text change here, for example, show suggestions
                // Get suggestions from Google Places API
                Places.initialize(getApplicationContext(), "AIzaSyCd83F00FOzhpe2nxTAKivZ7wKoGlGeWCY");
                PlacesClient placesClient = Places.createClient(getApplicationContext());
                AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setSessionToken(token)
                        .setQuery(newText)
                        .build();
                placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) ->
                {
                    // Process the response and display the suggestions to the user
                    List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                    // ...
                    List<String> suggestionList = new ArrayList<>();
                    for (AutocompletePrediction prediction : predictions)
                    {
                        suggestionList.add(prediction.getPrimaryText(null).toString());
                    }

                    // Update the RecyclerView adapter with the new suggestions
                    MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(suggestionList);
                    myRecyclerView.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    myRecyclerView.setLayoutManager(layoutManager);
                    adapter.setOnClickListener((view, position) -> {
                        String suggestion = suggestionList.get(position);
                        searchView.setQuery(suggestion, true);
                        onQueryTextSubmit(suggestion);
                    });


                }).addOnFailureListener((exception) ->
                {
                    // Handle the exception
                });
                return true;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        Task<Location> getCurrentLocation = mFusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null);
        getCurrentLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task getCurrentLocation) {
                location = (Location) getCurrentLocation.getResult();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                loc = new LatLng(latitude, longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        Places.initialize(getApplicationContext(), "AIzaSyCd83F00FOzhpe2nxTAKivZ7wKoGlGeWCY");
        PlacesClient placesClient = Places.createClient(getApplicationContext());
        LatLng location = new LatLng(latitude, longitude);
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields)
                .build();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        Task<FindCurrentPlaceResponse> placeResponseTask = placesClient.findCurrentPlace(request);

        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = "";

            // Show place details in a dialog or a custom layout
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setTitle("Place Details")
                    .setMessage("Address: " + address + "\nCity: " + city + "\nState: " + state + "\nCountry: " + country + "\nPostal Code: " + postalCode + "\nKnown Name: " + knownName)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            builder.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}