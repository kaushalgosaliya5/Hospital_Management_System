package com.example.intel.hospital;

/**
 * Created by Intel on 13/06/2016.
 */
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


public class LabTech extends Activity {

    RecyclerView mRecycleViewL;
    android.support.v7.widget.RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DBHelperL dbHelperL;

    TextToSpeech t1;

    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labtech);


        mRecycleViewL = (RecyclerView)findViewById(R.id.my_recycle_view_labtest);
        mRecycleViewL.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleViewL.setLayoutManager(mLayoutManager);

        dbHelperL = new DBHelperL(getApplicationContext());

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

         DisplayData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new MessageAdapter(dbHelperL.readAllMessages(), getApplicationContext());
        mRecycleViewL.setAdapter(mAdapter);
    }

    public void DisplayData() {

        Log.d("Admin", "FetchMessages Method");

        String url = "http://ksl.esy.es/usummer_school/ulab_detail.php";

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
                        Log.d("LabTech", "Message : " + messageArray.toString());


                        for (int i = 0; i < messageArray.length(); i++) {

                            JSONObject currentRow = messageArray.getJSONObject(i);

                            int t_id = currentRow.getInt("temp_id");
                            String t_Name = currentRow.getString("temp_Name");
                            String t_Gender = currentRow.getString("temp_Gender");
                            String t_Doctor = currentRow.getString("temp_Doctor");
                            String t_Prescription = currentRow.getString("temp_Prescription");
                            String t_LabTest = currentRow.getString("temp_LabTest");

                             MessageModel temp = new MessageModel(t_id, t_Name, t_Gender, t_Doctor, t_Prescription,t_LabTest);
                             dbHelperL.insertMessage(temp);
                            //arrayList.add(temp);
                        }

                    } else {
                        String toSpeak = "Error";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(LabTech.this, toSpeak, Toast.LENGTH_SHORT).show();
                    }

                   mAdapter = new MessageAdapter(dbHelperL.readAllMessages(), getApplicationContext());
                    mRecycleViewL.setAdapter(mAdapter);

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
        AppController.getInstance().addToRequestQueue(stringRequest, "req_lab_test");
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
