package com.example.migrenus.flirtapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener {
    private static String classLogTag = "MapActivity";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient; // used to determine our location
    private Location mCurrLocation;
    private int mSearchRadius = 500;
    private HttpsURLConnection mConnection;
    private Map<String, String> mMarkerIdToPlaceId;

    private TimerTask mLocationUpdaterTask;
    private Timer mLocationUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        ((TextView) findViewById(R.id.textViewOverlay)).setText(((User)getIntent().getSerializableExtra("user")).getPhoneNumber());

        mLocationUpdater = new Timer("Location updating task timer");
        mMarkerIdToPlaceId = new HashMap<>();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        mMarkerIdToPlaceId.clear();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // we stop the task so it doesn't
        // run in the background
        mLocationUpdaterTask.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationUpdaterTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateMap();
                    }
                });
                Log.i(classLogTag, "Map updated.");
            }
        };
        mLocationUpdater.schedule(mLocationUpdaterTask, 1000 * 15, 1000 * 10); // called every 10 seconds after 15 seconds
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // String name, String type, Location location, List<User> activeUsers, String placesApiId
        Place clickedPlace = new Place(marker.getTitle(), "restaurant", marker.getPosition().latitude, marker.getPosition().longitude, mMarkerIdToPlaceId.get(marker.getId()));

        Intent intent = new Intent(MapActivity.this, SelfieActivity.class);
        intent.putExtras(getIntent());
        intent.putExtra("place_info", clickedPlace);
        startActivity(intent);
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
        // we first check if a location was supplied
        // this was made so it could be used with a LocationListener
        // in an onLocationChanged method, but that idea was dropped
        /* if (varLocation.length > 0)
            mCurrLocation = varLocation[0];
        else */
        mCurrLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrLocation != null) {
            while (mMap == null); // effectively waiting for the mMap to
        } else {
            Log.e(classLogTag, "Location is null.");
            return;
        }

        // our request for the Google Places API
        String placesRequestString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                mCurrLocation.getLatitude() + "," + mCurrLocation.getLongitude() +
                "&radius=" + mSearchRadius +
                "&type=" + "restaurant" +
                "&key=" + getResources().getString(R.string.google_maps_key);

        // we make a URL from the request string
        URL placesRequestUrl = null;
        try {
            placesRequestUrl = new URL(placesRequestString);
        } catch (Exception e) {
            Log.d(classLogTag, "Failed to create URL: " + e.getMessage());
        }

        // set up an HTTPS connection to the API
        try {
            mConnection = (HttpsURLConnection) placesRequestUrl.openConnection();
        } catch (Exception e) {
            Log.d(classLogTag, "Failed to open connection: " + e.getMessage());
        }

        // since networking has to be done on a thread other
        // than the main one, we use an AsyncTask to retrieve
        // and parse the JSON response from the API
        StringBuilder responseBuilder = new StringBuilder();
        new AsyncTask<StringBuilder, Void, String>() {
            @Override
            protected String doInBackground(StringBuilder... rB) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));
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
                    // clear all markers, overlays and shapes from the map
                    mMap.clear();

                    // we add a marker at our current location
                    // and zoom in an appropriate amount
                    LatLng currLatLng = new LatLng(mCurrLocation.getLatitude(), mCurrLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currLatLng).title("Current location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));

                    // the circle is a radius indicator
                    // showing us the range of the query
                    mMap.addCircle(new CircleOptions()
                            .center(currLatLng)
                            .radius(mSearchRadius)
                            .strokeColor(Color.RED)
                            .fillColor(Color.BLUE)); // TODO: Set better colors for the radius indicator

                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray results = jsonResult.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject currResult = results.getJSONObject(i);
                        LatLng currLocation = new LatLng(currResult.getJSONObject("geometry").getJSONObject("location").getDouble("lat"), currResult.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                        Marker tempMarkerReference = mMap.addMarker(new MarkerOptions().position(currLocation).title(currResult.getString("name")));
                        if (!mMarkerIdToPlaceId.containsKey(tempMarkerReference.getId())) {
                            mMarkerIdToPlaceId.put(tempMarkerReference.getId(), currResult.getString("place_id"));
                        }
                    }
                } catch (JSONException e) {
                    Log.d(classLogTag, "Failed to parse JSON response.");
                    e.printStackTrace();
                }
            }
        }.execute(responseBuilder);
    }

}
