package com.example.mprojects.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

    EditText time;
    String value ;
    public static int set_time = 30;
    Button change;
    SharedPreferences sp;
    SharedPreferences.Editor e;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings , container , false);
        sp=getContext().getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);
        e=sp.edit();
        time = view.findViewById(R.id.time);
        change = view.findViewById(R.id.change_time);
        if(sp.getBoolean(Config.timechg,false)){
            time.setText(sp.getString(Config.time,String.valueOf(set_time)));
        }
        else{
            time.setText(set_time+" seconds");
        }
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=time.getText().toString();
                set_time =Integer.parseInt(value.split(" ")[0].trim());
                Log.e("time",set_time+"");
                e.putBoolean(Config.timechg,true);
                e.putString(Config.time,time.getText().toString());
                e.commit();
                Toast.makeText(getContext() , "Time Changed Successfully",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
