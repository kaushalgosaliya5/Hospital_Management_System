package com.example.intel.hospital;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;


public class Doctor extends Activity {

    RecyclerView mRecycleViewD;
    android.support.v7.widget.RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DBHelperD dbHelperD;

    Button btnP;

    TextToSpeech t1;

    String uname="";

    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uname = extras.getString("stuff");
        }

        Initialization();
        DisplayData();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uname = extras.getString("stuff");
        }

        mAdapter = new MessageAdapter(dbHelperD.readAllMessages(), getApplicationContext());
        mRecycleViewD.setAdapter(mAdapter);

        DisplayData();
    }

    public void Initialization(){

        mRecycleViewD = (RecyclerView)findViewById(R.id.my_recycle_view_doctor);
        mRecycleViewD.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleViewD.setLayoutManager(mLayoutManager);
        dbHelperD = new DBHelperD(getApplicationContext());


        btnP = (Button)findViewById(R.id.btnPatients);

        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer.create(Doctor.this, R.raw.click_one).start();

                Intent i = new Intent(Doctor.this,PatientData.class);
                Bundle bundle = new Bundle();
                bundle.putString("stuff1", uname);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    public void DisplayData() {

        Log.d("Admin", "FetchMessages Method");

        String url = "http://ksl.esy.es/usummer_school/udoctor_patients.php";

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

                            int t_id = currentRow.getInt("temp_Id");
                            String t_Name = currentRow.getString("temp_Name");
                            String t_Gender = currentRow.getString("temp_Gender");
                            String t_Age = currentRow.getString("temp_Age");
                            String t_Branch = currentRow.getString("temp_Branch");
                            String t_Doctor = currentRow.getString("temp_Doctor");

                            MessageModel temp = new MessageModel(t_id, t_Name, t_Gender, t_Age, t_Branch,t_Doctor);
                            dbHelperD.insertMessage(temp,t_Doctor);
                        }

                        mAdapter = new MessageAdapter(dbHelperD.readAllMessages(), getApplicationContext());
                        mRecycleViewD.setAdapter(mAdapter);

                    } else {
                        Toast.makeText(Doctor.this, "FAIL", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Doctor", "Eeeeerrrrrroooooorrrrr");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("UserName",uname);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "req_Doctor");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(Doctor.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}

