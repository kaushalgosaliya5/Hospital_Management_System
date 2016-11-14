package com.example.intel.hospital;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import java.util.Locale;
import java.util.Map;

public class PatientDetail extends Activity {

    RecyclerView mRecycleView;
    android.support.v7.widget.RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DBHelperP dbHelperP;

    TextToSpeech t1;

    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientdetail);

        mRecycleView = (RecyclerView) findViewById(R.id.my_recycle_view_patients);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        dbHelperP = new DBHelperP(getApplicationContext());

        DisplayData();

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new MessageAdapter(dbHelperP.readAllMessages(), getApplicationContext());
        mRecycleView.setAdapter(mAdapter);
    }

    public void DisplayData() {

        Log.d("Admin", "FetchMessages Method");

        String url = "http://ksl.esy.es/usummer_school/upatientsdetails.php";

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


                             MessageModel temp = new MessageModel(t_id, t_Name, t_Gender, t_Doctor, t_Branch,t_Age);
                           dbHelperP.insertMessage(temp);
                            //arrayList.add(temp);
                        }

                        mAdapter = new MessageAdapter(dbHelperP.readAllMessages(), getApplicationContext());
                        mRecycleView.setAdapter(mAdapter);

                    } else {
                        String toSpeak = "Error";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(PatientDetail.this, toSpeak, Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "req_patients_details");
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

}
