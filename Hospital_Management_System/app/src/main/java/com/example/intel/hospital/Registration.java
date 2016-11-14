package com.example.intel.hospital;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appdatasearch.RegisterSectionInfo;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Registration extends Activity {

    EditText Rname, Runame, Rpsw, Rcpsw, Rmobile;
    Spinner RuserType;

    Button Rsubmit, Rclear, Rhome;

    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Initializetion();


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

        Rname.setText("");
        Runame.setText("");
        Rpsw.setText("");
        Rcpsw.setText("");
        Rmobile.setText("");
        Rname.requestFocus();
    }

    public void Initializetion() {

        Rname = (EditText) findViewById(R.id.etRName);
        Runame = (EditText) findViewById(R.id.etRUname);
        Rpsw = (EditText) findViewById(R.id.etRPsw);
        Rcpsw = (EditText) findViewById(R.id.etRCPsw);
        Rmobile = (EditText) findViewById(R.id.etRMobile);
        RuserType = (Spinner) findViewById(R.id.Rspinner);

        Rsubmit = (Button) findViewById(R.id.btnRSubmit);
        Rclear = (Button) findViewById(R.id.btnRClear);
        Rhome = (Button) findViewById(R.id.btnRHome);


        Rhome.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(Registration.this, R.raw.click_one).start();

                Intent i = new Intent(Registration.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        Rclear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(Registration.this, R.raw.click_one).start();

                Rname.setText("");
                Runame.setText("");
                Rpsw.setText("");
                Rcpsw.setText("");
                Rmobile.setText("");
                Rname.requestFocus();
            }
        });


        Rsubmit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer.create(Registration.this, R.raw.click_one).start();
                RestartWork();
            }
        });

        SpinnerDetails();
    }


    public void SpinnerDetails() {

        ArrayList<String> al = new ArrayList<String>();
        al.add("Doctor");
        al.add("Cashier");
        al.add("Lab Tech");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, al);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        RuserType.setAdapter(dataAdapter);
    }

    public void RestartWork() {

        if (Rname.getText().toString().equals("")) {
            Rname.requestFocus();
            return;
        }
        if (Runame.getText().toString().equals("")) {
            Runame.requestFocus();
            return;
        }
        if (Rpsw.getText().toString().equals("")) {
            Rpsw.requestFocus();
            return;
        }
        if (Rcpsw.getText().toString().equals("")) {
            Rcpsw.requestFocus();
            return;
        }
        if (Rmobile.getText().toString().equals("")) {
            Rmobile.requestFocus();
            return;
        }

        if(Rpsw.getText().toString().length() <= 6){
            Rpsw.setText("");
            Rcpsw.setText("");
            Rpsw.requestFocus();

            String toSpeak = "More Then 6 Character In Password";
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(Registration.this,toSpeak, Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Rcpsw.getText().toString().equals(Rpsw.getText().toString())){
            Rpsw.setText("");
            Rcpsw.setText("");
            Rpsw.requestFocus();

            String toSpeak = "Password Is Miss Match";
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(Registration.this,toSpeak, Toast.LENGTH_SHORT).show();
            return;
        }


        if(Rmobile.getText().toString().length() > 10 || Rmobile.getText().toString().length() < 10){
            Rmobile.setText("");
            Rmobile.requestFocus();

            String toSpeak = "Mobile Number Is Wrong";
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(Registration.this,toSpeak,Toast.LENGTH_SHORT).show();
            return;
        }

        String tmp_name = Rname.getText().toString();
        String tmp_username = Runame.getText().toString();
        String tmp_password = Rpsw.getText().toString();
        String tmp_mobile = Rmobile.getText().toString();
        String tmp_userType = RuserType.getSelectedItem().toString();

        Log.d("Registration", "Button Clicked");

        RegistrationMethod(tmp_name, tmp_username, tmp_password, tmp_mobile, tmp_userType);
    }


    public void RegistrationMethod(final String name, final String username, final String password, final String mobile, final String usertype) {

        Log.d("Registration", "Registration Method");

        String url = "http://ksl.esy.es/usummer_school/uregistration.php";

        Log.d("Registration", "URL : " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("Registration", "Response : " + response);

                try {
                    JSONObject mainObject = new JSONObject(response);

                    boolean SUCCESS_TAG = mainObject.getBoolean("success");

                    if (SUCCESS_TAG) {
                        String toSpeak = "Registeration Success Fully";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(Registration.this,toSpeak, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(Registration.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        String toSpeak = "Error";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(Registration.this,toSpeak, Toast.LENGTH_LONG).show();
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

                params.put("Name", name);
                params.put("UserName", username);
                params.put("Password", password);
                params.put("Mobile", mobile);
                params.put("UserType", usertype);

                Log.d("Registration", name + " " + username + " " + password + " " + mobile + " " + usertype);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "req_register");

    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

}

