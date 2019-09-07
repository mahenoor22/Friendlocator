package com.example.mprojects.myapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int open_first_time = 0;
    String[] permissions = new String[]{Manifest.permission.READ_CONTACTS , Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.CALL_PHONE ,
            Manifest.permission.SEND_SMS};
    public static NavigationView navigationView ;
    String[] num;
    private DrawerLayout drawer;
    LocationManager locationManager;
    LocationListener locationListener;
    public static String message;
    Cursor c;
    int choice ;
    TextView h;
    SharedPreferences sp;
    SharedPreferences.Editor e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);

        askForAllPermisiions();
        startFindingLocation();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);
        e=sp.edit();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        h = (TextView)header.findViewById(R.id.headname);
        h.setText("Welcome "+sp.getString(Config.name,"not found"));

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawer , toolbar ,
                R.string.navigation_drawer_open , R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (sp.getBoolean(Config.STATUS,false)){
            FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.fragment_container, new MessageFragment()).addToBackStack("HomeFragment");
            fragmenttransaction.commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }

    }

    private void startFindingLocation() {

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                message = "http://maps.google.com/maps?daddr="+location.getLatitude()+","+location.getLongitude()+"&amp;ll=";
                //    tv.append("\n " + "http://maps.googleapis.com/maps/api/geocode/json?latlng="+location.getLatitude()+"," +location.getLongitude()+"&sensor=true" );
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

    }

    private void askForAllPermisiions() {

        if (ActivityCompat.checkSelfPermission(this  , Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this , Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this , Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this , new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.CALL_PHONE
            } , 10);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){

            case 10:
                Toast.makeText(this , "All Permiisions Granted" , Toast.LENGTH_SHORT);
                break;
        }

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.nav_about_us:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                TextView mymsg=new TextView(this);
                mymsg.setText("FRIEND LOCATOR is a simple app which helps you locate your friends on Google Map." +
                        " The user needs to add their friends through their contact list. If the user needs help when in danger " +
                        "it sends a sms to his friends by clicking a button and the msg is snd to the friends that are in your friend list. " +
                        " \n\nCreated By\nMahenoor Mansuri(Student)");
                mymsg.setGravity(Gravity.CENTER);
                mymsg.setTextSize(18);
                mymsg.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                builder.setTitle("About Us");
                builder.setIcon(R.mipmap.sos_round);
                builder.setView(mymsg)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                System.runFinalizersOnExit(true);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;



            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new MessageFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_message);
                break;

            case R.id.nav_call_feature:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new CallingFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_call_feature);
                break;

            case R.id.nav_contacts_view:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new ContactcallF()).commit();
                navigationView.setCheckedItem(R.id.nav_contacts_view);

                break;

            case R.id.nav_custom_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new CustomFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_custom_message);
                break;
            case R.id.nav_nearby_sos:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new SOSNearbyFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_nearby_sos);
                break;

            case R.id.nav_cry_out_loud:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new FragmentCry()).commit();
                navigationView.setCheckedItem(R.id.nav_cry_out_loud);
                break;

            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new SettingsFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_settings);
                break;
            case R.id.nav_My_Location:
                Intent i=new Intent(this,MapsActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("LOG OUT");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Do You Want To Log Out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                e.putBoolean(Config.STATUS,false);
                                e.commit();
                                Intent i=new Intent(MainActivity.this,Login.class);
                                startActivity(i);
                                Log.e("logout","hii");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert = builder.create();
                alert.show();

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
//        setNavigationViewCheckedItem();
        return true;
    }

}
