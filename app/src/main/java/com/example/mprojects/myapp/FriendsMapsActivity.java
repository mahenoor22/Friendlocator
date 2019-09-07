package com.example.mprojects.myapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendsMapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LocationListener locationListener;
    SharedPreferences sp;
    public List<String> dbloc;
    List<Address> addresses;
    String result;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sp = getSharedPreferences(Config.SPName, MODE_PRIVATE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getlocation();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getlocation();
                handler.postDelayed(this, SettingsFragment.set_time*1000);
                Log.e("Location","Updated");
                Toast.makeText(FriendsMapsActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
            }
        },SettingsFragment.set_time*1000);
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
      //  locationManager.removeUpdates(locationListener);
    }

    public void getlocation() {
        Log.e("f","hii");
        dbloc=new ArrayList<>();
        AndroidNetworking.initialize(getApplicationContext());
        Log.e("start","start");
        AndroidNetworking.get(Config.HOST+"getlatlon.php")
                .addQueryParameter("phone",sp.getString(Config.fPhone,"Not Found"))
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonobject= response;
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = jsonobject.getJSONArray("Latlon");
                            JSONObject JO = jsonArray.getJSONObject(0);
                            String lat = JO.getString("lat");
                            String log = JO.getString("long");
                            dbloc.add(lat + "-" + log);
                            Log.e("dbloc", "" + dbloc.get(0));
                            setmarker();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK)){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void setmarker(){
        String[] spl=dbloc.get(0).split("-");
        double latitude = Double.parseDouble(spl[0]);
        double longitude = Double.parseDouble(spl[1]);
        Log.e("dbloc1",""+dbloc.get(0));
        Log.e("latlngafter",spl[0]+""+spl[1]);
        //get the location name from latitude and longitude
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String result = addresses.get(0).getLocality()+":";
            result += addresses.get(0).getCountryName();

            LatLng latLng = new LatLng(latitude,longitude);
            if (marker != null){
                marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latLng).snippet(result).title(sp.getString(Config.Fname,"Not Found")));
                mMap.setMinZoomPreference(18);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            }
            else{
                marker = mMap.addMarker(new MarkerOptions().position(latLng).snippet(result).title(sp.getString(Config.Fname,"Not Found")));
                mMap.setMaxZoomPreference(18);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}