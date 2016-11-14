package com.example.intel.hospital;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Intel on 12/06/2016.
 */
public class PatientData extends Activity {


    Button Clear, Add;
    String uname;
    TextToSpeech t1;
    EditText pName;
    MultiAutoCompleteTextView pre, lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientdata);

        Initialize();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uname = extras.getString("stuff1");
        }

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        Clear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(PatientData.this, R.raw.click_one).start();

                pName.setText("");
                pre.setText("");
                lab.setText("");
                pName.requestFocus();
            }
        });

        Add.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(PatientData.this, R.raw.click_one).start();

                if(pName.getText().toString().equals("")){
                    pName.requestFocus();
                    return;
                }if(pre.getText().toString().equals("")){
                    pre.requestFocus();
                    return;
                }

                DisplayData();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        pName.setText("");
        pre.setText("");
        lab.setText("");
    }

    public void DisplayData() {

        Log.d("Admin", "FetchMessages Method");

        String url = "http://ksl.esy.es/usummer_school/upation_update.php";

        Log.d("Admin", "URL : " + url);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Admin", "Response : " + response);

                try {
                    JSONObject mainobject = new JSONObject(response);

                    boolean SUCCESS_TAG = mainobject.getBoolean("success");

                    if (SUCCESS_TAG) {
                        String toSpeak = "SUCCESS";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(PatientData.this, toSpeak, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(PatientData.this,Doctor.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("stuff", uname);
                        i.putExtras(bundle);
                        startActivity(i);

                    } else {
                        String toSpeak = "ERROR";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(PatientData.this, toSpeak, Toast.LENGTH_SHORT).show();
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

                String name = pName.getText().toString();
                String Prescription = pre.getText().toString() + "";
                String LabTest = lab.getText().toString() + "";

                params.put("Name",name);
                params.put("Prescription",Prescription);
                params.put("LabTest",LabTest);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "req_admin");
    }

    public void Initialize() {

        Clear = (Button) findViewById(R.id.btnPDClear);
        Add = (Button) findViewById(R.id.btnPDAdd);

        pName = (EditText) findViewById(R.id.etPatient);
        pre = (MultiAutoCompleteTextView) findViewById(R.id.mtPre);
        lab = (MultiAutoCompleteTextView) findViewById(R.id.mtLab);
    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(PatientData.this, Doctor.class);
        Bundle bundle = new Bundle();
        bundle.putString("stuff", uname);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }
}
