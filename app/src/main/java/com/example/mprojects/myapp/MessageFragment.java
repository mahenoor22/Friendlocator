package com.example.mprojects.myapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.LOCATION_SERVICE;

public class MessageFragment extends Fragment {

   // ImageView sos;
    Button sos;
    SmsManager sms;
    Button o_f_d;
    String message ;
    String finalMessage = "";
    String defaultMessage ="SAVE ME. I might be in distress. If I don't pick your call then I might be in a real trouble.";
    ImageView iv;
    public static Location LOC;
    private LocationManager locationManager;
    private LocationListener locationListener;
    Handler handler = new Handler();
    SharedPreferences sp;
    SharedPreferences.Editor e;
    public List<String> friends;
    int j=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        sp=getActivity().getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);
        e=sp.edit();

        sms = SmsManager.getDefault();
        o_f_d = view.findViewById(R.id.out);
        iv=view.findViewById(R.id.iv2);
        sos = view.findViewById(R.id.sos_button);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("tag" , "Goes Inside The onLocationChanged");
                LOC = location;
                e.putString(Config.Flat,String.valueOf(location.getLatitude()));
                e.putString(Config.Flong,String.valueOf(location.getLongitude()));
                e.commit();
                message ="http://maps.google.com/maps?daddr=" + location.getLatitude() + "," + location.getLongitude() + "&amp;ll=";
                //tv.setText("http://maps.googleapis.com/maps/api/geocode/json?latlng="+location.getLatitude()+"," +location.getLongitude()+"&sensor=true" );
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},1);
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };



        getContacts(sp.getString(Config.ID,"Not Found"));
        sos.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                iv.setVisibility(View.INVISIBLE);
                Log.e("funct",""+friends.size());
                j=0;
                sos.setVisibility(View.INVISIBLE);
                o_f_d.setVisibility(View.VISIBLE);
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                final String formattedDate = df.format(c);
                if (CustomFragment.Message!=null)
                    defaultMessage = CustomFragment.Message;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0 , 0,locationListener );
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                finalMessage = CustomFragment.Message+"  "+formattedDate;

                Toast.makeText(getContext() , "Will Be Sending A SMS To Your Friends" , Toast.LENGTH_SHORT).show();
                for(int i=0;i<friends.size();i++) {
                    sms.sendTextMessage(friends.get(i), null, finalMessage, null, null);
                    Log.e("msg","sent");
                }
                Toast.makeText(getActivity(), "Message Sent to your Friends", Toast.LENGTH_SHORT).show();


                handler.postDelayed(new Runnable() {
                    public void run() {
                        //do something
                        if (j==0){
                            updateMessage();
                            Log.e("afterupdate",friends.get(0)+""+friends.size()+SettingsFragment.set_time*1000);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5,
                                    locationListener);
                            Log.d("OnCLick" , "The Value Of J is" +j);
                            handler.postDelayed(this, (SettingsFragment.set_time*1000));
                        }
                        else if (j==1){
                            sos.setVisibility(View.VISIBLE);
                            o_f_d.setVisibility(View.INVISIBLE);
                            return;
                        }
                    }
                }, (SettingsFragment.set_time*1000));


            }
        });

        o_f_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j=1;
                iv.setVisibility(View.VISIBLE);
                o_f_d.setVisibility(View.INVISIBLE);
                sos.setVisibility(View.VISIBLE);
            }

        });

        return view;
    }

    public void getContacts(String uid){
        friends=new ArrayList<String>();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "friends.php").newBuilder();
            urlBuilder.addQueryParameter("uid", uid);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    //Toast.makeText(Home.this, "Please Check ur internet", Toast.LENGTH_SHORT).show();
                    Log.e("Failure",e.getMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //txtInfo.setText(response.body().string());

                                try {
                                    String data = response.body().string();
                                    Log.e("viewfriends", data);
                                    JSONObject jsonobject = new JSONObject(data);
                                    JSONArray jsonArray = jsonobject.getJSONArray("Json");
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        JSONObject actor = jsonArray.getJSONObject(i);
                                        String name = actor.getString("phone");
                                        friends.add(name);
                                    }
                                    Log.e("contactarray",""+friends.size());

                                     } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMessage() {
        Log.e("insideUpdate",""+friends.size());
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"setlocation.php").newBuilder();
            urlBuilder.addQueryParameter("uid",sp.getString(Config.ID,"Not found"));
            urlBuilder.addQueryParameter("long",sp.getString(Config.Flong,"Notfound"));
            urlBuilder.addQueryParameter("lat", sp.getString(Config.Flat,"Not Found"));
            Log.e("ULATLNG",sp.getString(Config.Flat,"Not Found")+sp.getString(Config.Flong,"Not Found"));
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    Log.e("register",response.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String s1=response.body().string().trim();
                                Log.e("register",s1);
                                if(s1.equals("updated"))
                                {
                                    Log.e("register","inside uf");
                                    Toast.makeText(getActivity(), "Location Updated And Sent", Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                };
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        //return;
    }

}

