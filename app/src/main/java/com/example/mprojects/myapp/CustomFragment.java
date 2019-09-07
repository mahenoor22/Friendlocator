package com.example.mprojects.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomFragment extends Fragment {

    EditText message;
    Button change ;
    SharedPreferences sp;
    SharedPreferences.Editor e;
    public static String Message = "SAVE ME. I might be in distress. If I don't pick your call then I might be in a real trouble.  ";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom , container , false);
        sp=getContext().getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);
        e=sp.edit();
        message = view.findViewById(R.id.message1);
        change = view.findViewById(R.id.Change);
        if(sp.getBoolean(Config.msgchg,false)){
            message.setText(sp.getString(Config.msg,Message));
        }
        else{
            message.setText(Message);
        }
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message = message.getText().toString();
                e.putBoolean(Config.msgchg,true);
                e.putString(Config.msg,Message);
                e.commit();
                Toast.makeText(getContext() , "Message Changed Successfully",Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }
}
