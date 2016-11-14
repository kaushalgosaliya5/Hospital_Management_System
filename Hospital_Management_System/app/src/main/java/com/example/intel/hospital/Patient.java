package com.example.intel.hospital;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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
 * Created by Intel on 12/06/2016.
 */
public class Patient extends Activity {

    EditText Pname,Paddress,Page,Pmobile;
    Spinner Pbranch,Pdoctor;
    RadioButton rMale,rFemale;
    Button PClear,PSubmit,PHome;
    TextToSpeech t1;

    String pname,paddress,page,pmobile,pgender,pbranch,pdoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Initialize();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


        PClear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(Patient.this, R.raw.click_one).start();

                Pname.setText("");
                Paddress.setText("");
                Page.setText("");
                Pmobile.setText("");
                rFemale.setChecked(false);
                rMale.setChecked(true);
                Pname.requestFocus();
            }
        });

        PHome.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(Patient.this, R.raw.click_one).start();

                Intent i = new Intent(Patient.this,MainActivity.class);
                startActivity(i);

            }
        });


        PSubmit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(Patient.this, R.raw.click_one).start();
                CheckDetails();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Pname.setText("");
        Paddress.setText("");
        Page.setText("");
        Pmobile.setText("");

        InsertDoctor();
    }

    public void InsertDoctor(){

            Log.d("Patients", "Registration Method");

            String url = "http://ksl.esy.es/usummer_school/udoctor.php";

            Log.d("Login", "URL : " + url);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.d("MainActivity","Response : " + response);


                    ArrayList<String> al = new ArrayList<String>();

                    try {
                        JSONObject mainObject = new JSONObject(response);

                        boolean SUCCESS_TAG = mainObject.getBoolean("success");


                        if(SUCCESS_TAG) {

                            JSONArray messageArray = mainObject.getJSONArray("messageData");

                            for (int i = 0; i < messageArray.length(); i++) {

                                JSONObject currentRow = messageArray.getJSONObject(i);

                                String t_Name = currentRow.getString("temp_Name");
                                al.add(t_Name);
                            }
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (Patient.this, android.R.layout.simple_spinner_item,al);
                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);
                        Pdoctor.setAdapter(dataAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("Login","Eeeeerrrrrroooooorrrrr");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    String usertype = "Doctor";
                    params.put("UserType",usertype);

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest,"req_Patients");
    }

    public void CheckDetails(){

        if(Pname.getText().toString().equals("")){
            Pname.requestFocus();return;
        } if(Paddress.getText().toString().equals("")){
            Paddress.requestFocus();return;
        } if(Page.getText().toString().equals("")){
            Page.requestFocus();return;
        } if(Pmobile.getText().toString().equals("")){
            Pmobile.requestFocus();return;
        }

        Integer n = Integer.parseInt(Page.getText().toString());
        if(n <= 0 || n >= 110){
            Page.setText("");
            Page.requestFocus();

            String toSpeak = "Age Must Between 0 to 110";
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(Patient.this,toSpeak,Toast.LENGTH_SHORT).show();
            return;
        }

        if(Pmobile.getText().toString().length() > 10 || Pmobile.getText().toString().length() < 10){
            Pmobile.setText("");
            Pmobile.requestFocus();

            String toSpeak = "Mobile Number Is Wrong";
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(Patient.this,toSpeak,Toast.LENGTH_SHORT).show();

            return;
        }

        InsertValue();
    }


    public void Initialize(){

        Pname = (EditText)findViewById(R.id.etPName);
        Paddress = (EditText)findViewById(R.id.etPAddress);
        Page = (EditText)findViewById(R.id.etPAge);
        Pmobile = (EditText)findViewById(R.id.etPMobile);

        rMale = (RadioButton)findViewById(R.id.rMale);
        rFemale =(RadioButton)findViewById(R.id.rFemale);

        Pbranch = (Spinner)findViewById(R.id.PBranch);
        Pdoctor = (Spinner)findViewById(R.id.PDoctor);

        PHome = (Button)findViewById(R.id.btnPHome);
        PSubmit = (Button)findViewById(R.id.btnPSubmit);
        PClear = (Button)findViewById(R.id.btnPClear);


        ArrayList<String> al = new ArrayList<String>();
        al.add("Dermatology");
        al.add("General Surgery");
        al.add("Neurology");
        al.add("Ophthalmology");
        al.add("Paediatrics");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,al);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        Pbranch.setAdapter(dataAdapter);
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    public void InsertValue(){

        pname = Pname.getText().toString();
        paddress = Paddress.getText().toString();
        page = Page.getText().toString();
        pmobile = Pmobile.getText().toString();
        pbranch = Pbranch.getSelectedItem().toString();
        pdoctor = Pdoctor.getSelectedItem().toString();

        if(rFemale.isChecked()){
            pgender = "Female";
        }else{
            pgender = "Male";
        }

            String url = "http://ksl.esy.es/usummer_school/upatients.php";

            Log.d("Patients", "URL : " + url);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.d("Patients", "Response : " + response);

                    try {
                        JSONObject mainObject = new JSONObject(response);

                        boolean SUCCESS_TAG = mainObject.getBoolean("success");

                        if (SUCCESS_TAG) {
                            String toSpeak = "Registeration Success Fully";
                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            Toast.makeText(Patient.this, toSpeak, Toast.LENGTH_LONG).show();

                            Intent i = new Intent(Patient.this, Cashier.class);
                            startActivity(i);
                            finish();
                        } else {
                            String toSpeak = "Error";
                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            Toast.makeText(Patient.this, toSpeak, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("Register", "Eeeeerrrrrroooooorrrrr");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("Name", pname);
                    params.put("Gender", pgender);
                    params.put("Address", paddress);
                    params.put("Age", page);
                    params.put("Mobile", pmobile);
                    params.put("Branch", pbranch);
                    params.put("Doctor", pdoctor);

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest, "req_patients");
    }
}
