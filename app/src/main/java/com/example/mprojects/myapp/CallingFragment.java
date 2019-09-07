package com.example.mprojects.myapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CallingFragment extends Fragment {

    int j=0;
    EditText e ;
    String num;
    Button Cop,Fire,Ambulance,Child,Women,Blood;
    private static final int REQUEST_CALL = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calling , container , false);

        Cop = view.findViewById(R.id.cop);
        Fire= view.findViewById(R.id.fire);
        Ambulance = view.findViewById(R.id.ambulance);
        Child = view.findViewById(R.id.child);
        Women = view.findViewById(R.id.women);
        Blood = view.findViewById(R.id.blood);

        Cop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "100";
                if (number.trim().length()>0){

                    if (ContextCompat.checkSelfPermission(getContext() ,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CALL_PHONE} ,REQUEST_CALL);

                    }else{

                        String dial = "tel:" + number ;
                        startActivity(new Intent(Intent.ACTION_CALL , Uri.parse(dial)));
                    }

                }

            }
        });

        Fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "101";
                if (number.trim().length()>0){

                    if (ContextCompat.checkSelfPermission(getContext() ,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CALL_PHONE} ,REQUEST_CALL);

                    }else{

                        String dial = "tel:" + number ;
                        startActivity(new Intent(Intent.ACTION_CALL , Uri.parse(dial)));
                    }

                }
            }
        });
        //num = ContactFragment.num;
        //e.setText(num);

        Ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "102";
                if (number.trim().length()>0){

                    if (ContextCompat.checkSelfPermission(getContext() ,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CALL_PHONE} ,REQUEST_CALL);

                    }else{

                        String dial = "tel:" + number ;
                        startActivity(new Intent(Intent.ACTION_CALL , Uri.parse(dial)));
                    }

                }
            }
        });

        Child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "1098";
                if (number.trim().length()>0){

                    if (ContextCompat.checkSelfPermission(getContext() ,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CALL_PHONE} ,REQUEST_CALL);

                    }else{

                        String dial = "tel:" + number ;
                        startActivity(new Intent(Intent.ACTION_CALL , Uri.parse(dial)));
                    }

                }
            }
        });

        Women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "181";
                if (number.trim().length()>0){

                    if (ContextCompat.checkSelfPermission(getContext() ,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CALL_PHONE} ,REQUEST_CALL);

                    }else{

                        String dial = "tel:" + number ;
                        startActivity(new Intent(Intent.ACTION_CALL , Uri.parse(dial)));
                    }

                }
            }
        });

        Blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "104";
                if (number.trim().length()>0){

                    if (ContextCompat.checkSelfPermission(getContext() ,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CALL_PHONE} ,REQUEST_CALL);

                    }else{

                        String dial = "tel:" + number ;
                        startActivity(new Intent(Intent.ACTION_CALL , Uri.parse(dial)));
                    }

                }
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {

            case REQUEST_CALL:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext() , "GRANTED" , Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(getContext() , "Permission Denied" , Toast.LENGTH_LONG).show();
                }
        }
    }
}
