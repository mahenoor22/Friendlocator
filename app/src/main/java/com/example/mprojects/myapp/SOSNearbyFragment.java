package com.example.mprojects.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SOSNearbyFragment extends Fragment {

    Button SOS1,SOS2,SOS3,SOS4;
    double lat,lon;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sos , container , false);

        SOS1 = view.findViewById(R.id.sos1);
        SOS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String url="https://www.google.co.in/maps/search/nearby+hospitals";
               Intent i=new Intent(Intent.ACTION_VIEW);
               i.setData(Uri.parse(url));
               startActivity(i);
                   /* lat = MessageFragment.LOC.getLatitude();
                    lon = MessageFragment.LOC.getLongitude();
                    goToUrl( "http://maps.google.com/maps?q=hospital&mrt=yp&sll="+lat+","+lon+"&output=kml");*/
            }
        });

        SOS2 = view.findViewById(R.id.sos2);
        SOS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://www.google.co.in/maps/search/nearby+police+stations";
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        SOS3 = view.findViewById(R.id.sos3);
        SOS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://www.google.co.in/maps/search/nearby+blood+banks";
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                }
        });


        SOS4 = view.findViewById(R.id.sos4);
        SOS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://www.google.co.in/maps/search/nearby+ATM";
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return view;
    }
}
