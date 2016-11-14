package com.example.intel.hospital;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Intel on 11/06/2016.
 */
public class Admin extends Activity {

    RecyclerView mRecycleView;
    android.support.v7.widget.RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DBHelper dbHelper;

    Button bDoctor, bLabTech, bCashier, bAdmin;
    String utype = "Doctor";
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        Initialize();
    }


    public void DisplayData(final String usertype) {

        Log.d("Admin", "FetchMessages Method");

        String url = "http://ksl.esy.es/usummer_school/uuser_details.php";

        Log.d("Admin", "URL : " + url);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Admin", "Response : " + response);

                try {
                    JSONObject mainobject = new JSONObject(response);

                    boolean SUCCESS_TAG = mainobject.getBoolean("success");

                    if (SUCCESS_TAG) {

                        JSONArray messageArray = mainobject.getJSONArray("messageData");
                        Log.d("MainActivity", "Message : " + messageArray.toString());


                        for (int i = 0; i < messageArray.length(); i++) {

                            JSONObject currentRow = messageArray.getJSONObject(i);

                            int t_id = currentRow.getInt("temp_id");
                            String t_Name = currentRow.getString("temp_Name");
                            String t_UserName = currentRow.getString("temp_UserName");
                            String t_Password = currentRow.getString("temp_Password");
                            String t_Mobile = currentRow.getString("temp_Mobile");
                            String t_UserType = currentRow.getString("temp_UserType");

                            Log.d("MainActivity", " " + t_id + " " + t_UserName + " " + t_Password + " " + t_Mobile + " " + t_UserType);


                            MessageModel temp = new MessageModel(t_id, t_Name, t_UserName, t_Password, t_Mobile,t_UserType);
                            dbHelper.insertMessage(temp);
                            //arrayList.add(temp);
                        }

                        mAdapter = new MessageAdapter(dbHelper.readAllMessages(), getApplicationContext());
                        mRecycleView.setAdapter(mAdapter);

                    } else {
                        Toast.makeText(Admin.this, "FAIL", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Admin", "Eeeeerrrrrroooooorrrrr");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("UserType",usertype);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "req_admin");
    }


    public void Initialize() {

        mRecycleView = (RecyclerView)findViewById(R.id.my_recycle_view);

        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);

        dbHelper = new DBHelper(getApplicationContext());

       mAdapter = new MessageAdapter(dbHelper.readAllMessages(),getApplicationContext());
       mRecycleView.setAdapter(mAdapter);

        bDoctor = (Button) findViewById(R.id.bDoctor);
        bLabTech = (Button) findViewById(R.id.bLabTech);
        bCashier = (Button) findViewById(R.id.bCashier);
        bAdmin = (Button) findViewById(R.id.bAdmin);

        bDoctor.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(Admin.this, R.raw.click_one).start();

                utype = "Doctor";
               DisplayData(utype);

            }
        });

        bCashier.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer.create(Admin.this, R.raw.click_one).start();

                utype = "Cashier";
                DisplayData(utype);
            }
        });

        bLabTech.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer.create(Admin.this, R.raw.click_one).start();

                utype = "Lab Tech";
               DisplayData(utype);
            }
        });

        bAdmin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer.create(Admin.this, R.raw.click_one).start();

                utype = "Admin";
                DisplayData(utype);
            }
        });

    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
