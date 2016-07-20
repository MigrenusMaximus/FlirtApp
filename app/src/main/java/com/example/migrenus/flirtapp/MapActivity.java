package com.example.migrenus.flirtapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static String classLogTag = "MapActivity";

    private GoogleMap map;
    private GoogleApiClient googleApiClient; // used to determine our location
    private Location lastLocation;
    private int searchRadius = 500;
    private Circle radiusIndicator;

    private int updates = 0;

    private HttpsURLConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateMap();
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        updates += 1;
//        Toast.makeText(getApplicationContext(), "Updates: " + updates, Toast.LENGTH_SHORT).show();
//        updateMap(location);
//    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void updateMap(Location... varLocation) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (varLocation.length > 0)
            lastLocation = varLocation[0];
        else
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        LatLng currLatLng;
        if (lastLocation != null) {
            while (map == null); // effectively waiting for the map to load

            currLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            map.addMarker(new MarkerOptions().position(currLatLng).title("Current location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(14.0f));

            if (radiusIndicator != null) {
                radiusIndicator.remove();
                radiusIndicator = null;
            }

            radiusIndicator = map.addCircle(new CircleOptions()
                    .center(currLatLng)
                    .radius(searchRadius)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE));
        }

        String placesRequestString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lastLocation.getLatitude() +
                "," +
                lastLocation.getLongitude() +
                "&radius=" +
                searchRadius +
                "&type=restaurant&key=" +
                getResources().getString(R.string.google_maps_key);

        URL placesRequestUrl = null;
        try {
            placesRequestUrl = new URL(placesRequestString);
        } catch (Exception e) {
            Log.d(classLogTag, "Failed to create URL: " + e.getMessage());
        }

        try {
            connection = (HttpsURLConnection) placesRequestUrl.openConnection();
        } catch (Exception e) {
            Log.d(classLogTag, "Failed to open connection: " + e.getMessage());
        }

        StringBuilder responseBuilder = new StringBuilder();
        new AsyncTask<StringBuilder, Void, String>() {
            @Override
            protected String doInBackground(StringBuilder... rB) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null)
                        rB[0].append(inputLine);
                } catch (IOException e) {
                    Log.d(classLogTag, "IOException: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e(classLogTag, "Failed to read HTTPS response: " + e.getMessage());
                    e.printStackTrace();
                }
                return rB[0].toString();
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray results = jsonResult.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject currResult = results.getJSONObject(i);
                        LatLng currLocation = new LatLng(currResult.getJSONObject("geometry").getJSONObject("location").getDouble("lat"), currResult.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                        map.addMarker(new MarkerOptions().position(currLocation).title(currResult.getString("name")));
                    }
                } catch (JSONException e) {
                    Log.d(classLogTag, "Failed to parse JSON response.");
                    e.printStackTrace();
                }
            }
        }.execute(responseBuilder);
    }
}
