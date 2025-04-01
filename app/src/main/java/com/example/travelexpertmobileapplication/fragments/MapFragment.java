package com.example.travelexpertmobileapplication.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.model.Place;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.PlacesAPIService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // Views from XML
    private MapView mapView;
    private GoogleMap googleMap;
    private ProgressBar progressBar;

    // Services
    private PlacesAPIService apiService;
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            apiService = ApiClient.getClient().create(PlacesAPIService.class);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

            if (!Places.isInitialized()) {
                Places.initialize(requireContext(), getString(R.string.google_maps_key));
            }
            placesClient = Places.createClient(requireContext());
        } catch (Exception e) {
            Log.e("MapFragment", "Initialization error", e);
            Toast.makeText(getContext(), "Map services initialization failed", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize MapView first
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);  // This triggers onMapReady()

        progressBar = view.findViewById(R.id.progressBar);

        // Setup search with autocomplete
        setupAutocompleteSearch(view);

        return view;
    }

    private void setupAutocompleteSearch(View rootView) {
        FrameLayout container = rootView.findViewById(R.id.searchContainer);
        if (container == null) {
            Log.e("MapFragment", "Search container not found");
            return;
        }

        try {
            AutocompleteSupportFragment autocompleteFragment = new AutocompleteSupportFragment();

            getChildFragmentManager().beginTransaction()
                    .replace(container.getId(), autocompleteFragment)
                    .commit();

            // Set place fields
            autocompleteFragment.setPlaceFields(Arrays.asList(
                    com.google.android.libraries.places.api.model.Place.Field.ID,
                    com.google.android.libraries.places.api.model.Place.Field.NAME,
                    com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
                    com.google.android.libraries.places.api.model.Place.Field.ADDRESS
            ));

            // Set up the place selection listener
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                    if (place.getLatLng() != null && googleMap != null) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                place.getLatLng(), 14f));

                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions()
                                .position(place.getLatLng())
                                .title(place.getName())
                                .snippet(place.getAddress()));
                    }
                }

                @Override
                public void onError(@NonNull Status status) {
                    if (status.getStatusMessage() != null) {
                        Toast.makeText(getContext(),
                                "Error: " + status.getStatusMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            Log.e("MapFragment", "Error setting up autocomplete", e);
            Toast.makeText(getContext(), "Search functionality not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            // Enable basic map features
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);

            // Check and request location permission
            if (checkLocationPermission()) {
                googleMap.setMyLocationEnabled(true);
                getCurrentLocation();
            }

            // Set a default location if needed
            LatLng saitLocation = new LatLng(51.0649, -114.0881); // SAIT main campus coordinates
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saitLocation, 15f));

        } catch (SecurityException e) {
            Log.e("MapFragment", "Error setting up map", e);
        }
    }

    private void setupMap() {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        if (checkLocationPermission()) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestLocationPermission();
            return false;
        }
    }

    private void getCurrentLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(
                                    location.getLatitude(),
                                    location.getLongitude()
                            );
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    currentLatLng, 14f));
                            fetchNearbyPlaces(location.getLatitude(), location.getLongitude());
                        }
                    });
        } catch (SecurityException e) {
            Log.e("MapFragment", "Security Exception", e);
        }
    }

    private void fetchNearbyPlaces(double lat, double lng) {
        showLoading(true);

        Call<List<Place>> call = apiService.getNearbyPlaces(lat, lng, 5000);
        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    displayPlacesOnMap(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                showLoading(false);
                Toast.makeText(getContext(), "Failed to load places", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayPlacesOnMap(List<Place> places) {
        if (googleMap == null || places == null || places.isEmpty()) {
            Toast.makeText(getContext(), "No places to display", Toast.LENGTH_SHORT).show();
            return; // Exit if no places
        }

        googleMap.clear();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (Place place : places) {
            LatLng latLng = new LatLng(place.getLat(), place.getLng());
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(place.getName())
                    .snippet(place.getAddress()));

            boundsBuilder.include(latLng);
        }

        try {
            // Only animate camera if there are valid points
            LatLngBounds bounds = boundsBuilder.build();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            Log.e("MapFragment", "Camera update error", e);
            // Fallback: Zoom to first place if bounds fail
            LatLng firstPlace = new LatLng(places.get(0).getLat(), places.get(0).getLng());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstPlace, 12f));
        }
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void requestLocationPermission() {
        requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    // MapView lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}