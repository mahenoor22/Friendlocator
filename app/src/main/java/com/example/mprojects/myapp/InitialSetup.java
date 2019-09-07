package com.example.mprojects.myapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;


public class InitialSetup extends Activity {

    int j = 0;
    Boolean PG;//PermissionGranted
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accessing_contacts_final);
        if (j==0){
            if (ActivityCompat.checkSelfPermission(this , Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                PG = false;
                ActivityCompat.requestPermissions(InitialSetup.this , new String[]{Manifest.permission.READ_CONTACTS},10);
            }
            else{
                PG = true;
            }

        }else{
            PG = true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setContentView(R.layout.activity_mainactivity);
    }

    public void whnSkipClicked(View view) {

        setContentView(R.layout.activity_mainactivity);
    }

    public void whnNextClicked(View view) {
        setContentView(R.layout.activity_mainactivity);
    }
}
