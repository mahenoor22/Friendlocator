package com.example.mprojects.myapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ContactcallF extends Fragment{

    FloatingActionButton fb;
    private final int REQUEST_CODE=99;
    String name , num;
    String contact_name;
    SharedPreferences sp;
    SharedPreferences.Editor e;
    ListView lvitem;
    ItemAdapter adapter;
    Item mItem;
    SwipeRefreshLayout pullToRefresh;
    static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactcall, container, false);

        context=getContext();

        sp = getContext().getSharedPreferences(Config.SPName, Context.MODE_PRIVATE);
        e = sp.edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        lvitem = view.findViewById(R.id.list);
        adapter = new ItemAdapter(getActivity(), R.layout.item_layout);


        pullToRefresh=view.findViewById(R.id.pullToRefresh);
        listview();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                FragmentTransaction fragmenttransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmenttransaction.replace(R.id.fragment_container, new ContactcallF()).addToBackStack("HomeFragment");
                fragmenttransaction.commit();
            }
        });
        pullToRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fb = view.findViewById(R.id.addpeople);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        return view;
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri contactData = data.getData();
                        Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                        if (c.moveToFirst()) {
                            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                            String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                            if (Integer.valueOf(hasNumber) == 1) {
                                Cursor numbers = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                                try {
                                    while (numbers.moveToNext()) {
                                        num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        num = num.replaceAll(" ", "");
                                        //i++;
                                        if (num.startsWith("+")) {
                                            if (num.length() == 13) {
                                                num = num.substring(3);
                                                //   Toast.makeText(getContext(), "Number="+num[i], Toast.LENGTH_LONG).show();
                                                break;
                                            } else if (num.length() == 14) {
                                                num = num.substring(4);
                                                break;
                                            }
                                        }
                                    }
                                    if (num != null) {
                                        name = getContactName(getContext(), num);
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                }

                            } } } }
                            addfriend();
                Toast.makeText(getContext(), name+" selected ", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adapter.getSelectedItem(item);
        return super.onContextItemSelected(item);
    }

    private void listview() {
        pullToRefresh.setRefreshing(true);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "friends.php").newBuilder();
            urlBuilder.addQueryParameter("uid", sp.getString(Config.ID, "Not Found"));

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
                                try {
                                    String data = response.body().string();
                                    Log.e("viewfriends", data);
                                    JSONObject jsonobject = new JSONObject(data);
                                    JSONArray jsonArray = jsonobject.getJSONArray("Json");
                                    for (int count = 0;count < jsonArray.length();count++) {
                                        JSONObject JO = jsonArray.getJSONObject(count);
                                        mItem = new Item(JO.getString("fid"), JO.getString("name"), JO.getString("phone"),JO.getString("check"));
                                        adapter.add(mItem);
                                    }
                                    lvitem.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            pullToRefresh.setRefreshing(false);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addfriend(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"addfriend.php").newBuilder();
            urlBuilder.addQueryParameter("uid", sp.getString(Config.ID,"not found"));
            urlBuilder.addQueryParameter("name", name);
            urlBuilder.addQueryParameter("phone", num);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String s1=response.body().string().trim();
                                Log.e("addfriend",s1);
                                if(s1.equals("success"))
                                {
                                    Toast.makeText(getContext(), name+" added successfully...." , Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                };
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}