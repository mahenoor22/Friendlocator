package com.example.mprojects.myapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.example.mprojects.myapp.ContactcallF.context;

public class ItemAdapter extends ArrayAdapter {

    List list = new ArrayList();
    static int position;
    SharedPreferences sp1=context.getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);
    final SharedPreferences.Editor e1=sp1.edit();
    public ItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Item object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        this.position=position;
        return list.get(position);
    }
    public void delete(){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "deletefriend.php").newBuilder();
        urlBuilder.addQueryParameter("phone", sp1.getString(Config.fPhone,"not found"));
        urlBuilder.addQueryParameter("id", sp1.getString(Config.ID,"not found"));
        Log.e("delete",sp1.getString(Config.fPhone,"not found")+" "+sp1.getString(Config.ID,"not found"));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            String msg=client.newCall(request).execute().body().string().trim();
            Log.e("deletemsg",msg);
            if(msg.equals("deleted")) {
                Toast.makeText(context, "Removed "+sp1.getString(Config.Fname,"not found")+" successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        final ItemHolder h;
        if (row == null) {
            LayoutInflater LI = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = LI.inflate(R.layout.item_layout, parent, false);
            h = new ItemHolder(row);
            h.txtname = row.findViewById(R.id.txtname);
            h.txtid = row.findViewById(R.id.txtID);
            h.txtphone = row.findViewById(R.id.txtphone);
            h.trackImg=row.findViewById(R.id.track);
            h.check=row.findViewById(R.id.check);
            row.setTag(h);
        } else {
            h = (ItemHolder) row.getTag();
        }
        final Item item = (Item) this.getItem(position);
        h.txtid.setText(item.getID());
        h.txtname.setText(item.getName());
        h.txtphone.setText(item.getPhone());
        h.check.setText(item.getCheck());
        if(h.check.getText().equals("1")) {
            h.trackImg.setVisibility(View.VISIBLE);
        }
        else{
            h.trackImg.setVisibility(View.INVISIBLE);
            }
        return row;

    }
    public void getSelectedItem(MenuItem item) {
        if(item.getItemId()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete Friend");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do You Want To Remove Your Friend?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            delete();
                            Log.e("del","hii");
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    static class ItemHolder implements  View.OnLongClickListener,View.OnCreateContextMenuListener{
        TextView txtname,txtid,txtphone,check;
        ImageView trackImg;
        SharedPreferences sp=context.getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);
        final SharedPreferences.Editor e=sp.edit();
        longclick l;

        public ItemHolder(View v) {
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String name = ((TextView) v.findViewById(R.id.txtname)).getText().toString();
                    String phone = ((TextView) v.findViewById(R.id.txtphone)).getText().toString();
                    e.putString(Config.Fname, name);
                    e.putString(Config.fPhone,phone);
                    e.commit();
                    return false;
                }
            });
            v.setOnCreateContextMenuListener(this);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView) v.findViewById(R.id.txtname)).getText().toString();
                    String phone = ((TextView) v.findViewById(R.id.txtphone)).getText().toString();
                    String check=((TextView) v.findViewById(R.id.check)).getText().toString();
                    Log.e("check",check);
                    e.putString(Config.fPhone, phone);
                    e.putString(Config.Fname,name);
                    Log.e("fphone",phone);
                    e.commit();
                    if(check.equals("1")) {
                        Intent i = new Intent(context, FriendsMapsActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    else {
                        Toast.makeText(context, "This user Doesnt have a location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,0,0,"Remove Friend");
        }


        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
