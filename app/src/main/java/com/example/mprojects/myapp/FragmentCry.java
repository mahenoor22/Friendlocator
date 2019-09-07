package com.example.mprojects.myapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentCry extends Fragment {

    Button Woman , Police ,Man ,stop;
    MediaPlayer player;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cry , container , false);

        Police = view.findViewById(R.id.police);
        Woman = view.findViewById(R.id.woman);
        stop=view.findViewById(R.id.stop);
        

        Police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		if(player!=null){
			player.stop();
		}
		player = MediaPlayer.create(getContext() ,R.raw.police);
		player.setLooping(true);
                player.start();
            }
        });
        Woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(player!=null){
                player.stop();
            }
            player = MediaPlayer.create(getContext() , R.raw.woman);
            player.setLooping(true);
            player.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    player.stop();
            }
        });
        return view ;
    }

}
