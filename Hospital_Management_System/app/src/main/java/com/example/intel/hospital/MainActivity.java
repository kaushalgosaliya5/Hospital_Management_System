package com.example.intel.hospital;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {

    Button Login, Registration,ForgotPsw;
    EditText UserName, Password;
    Spinner spinner;
    TextToSpeech t1;
    String uname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialization();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    public void Initialization(){

        Login = (Button) findViewById(R.id.btnLogin);
        Registration = (Button) findViewById(R.id.btnRegistration);
        ForgotPsw = (Button) findViewById(R.id.btnForgot);

        UserName = (EditText) findViewById(R.id.etUname);
        Password = (EditText) findViewById(R.id.etPsw);

        spinner = (Spinner)findViewById(R.id.spn);

        ArrayList<String> al = new ArrayList<String>();
        al.add("Doctor");
        al.add("Cashier");
        al.add("Lab Tech");
        al.add("Admin");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,al);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        ForgotPsw.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(MainActivity.this, R.raw.click_one).start();

                Intent i = new Intent(MainActivity.this,ForgotPsw.class);
                startActivity(i);
            }
        });

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer.create(MainActivity.this, R.raw.click_one).start();

                Intent i = new Intent(MainActivity.this, Registration.class);
                startActivity(i);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MediaPlayer.create(MainActivity.this, R.raw.click_one).start();

                if(UserName.getText().toString().equals("")){
                    String toSpeak = "Enter Login Details";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(MainActivity.this,toSpeak,Toast.LENGTH_SHORT).show();
                    UserName.requestFocus();
                    return;
                }
                if(Password.getText().toString().equals("")){
                    Password.requestFocus();
                    return;
                }

                String tmp_user = UserName.getText().toString();
                String tmp_password = Password.getText().toString();
                String tmp_usertype = spinner.getSelectedItem().toString();

                LogInMethod(tmp_user,tmp_password,tmp_usertype);
            }
        });
    }

    public void LogInMethod(final String username,final String password,final String usertype){


        Log.d("MainActivity", "Registration Method");

        String url = "http://ksl.esy.es/usummer_school/ulogin.php";

        Log.d("Login", "URL : " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("MainActivity","Response : " + response);

                try {
                    JSONObject mainObject = new JSONObject(response);

                    boolean SUCCESS_TAG = mainObject.getBoolean("success");

                    if(SUCCESS_TAG){

                        uname = UserName.getText().toString();

                        String s = spinner.getSelectedItem().toString();
                        Intent i;
                        if(s.equals("Doctor")){
                            i = new Intent(MainActivity.this,Doctor.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("stuff", uname);
                            i.putExtras(bundle);
                            startActivity(i);
                        }else if(s.equals("Cashier")){
                            i = new Intent(MainActivity.this,Cashier.class);
                            startActivity(i);
                        }else if(s.equals("Lab Tech")){
                            i = new Intent(MainActivity.this,LabTech.class);
                            startActivity(i);
                        }else if(s.equals("Admin")){
                            i = new Intent(MainActivity.this,Admin.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(MainActivity.this,"Select Spinner Value",Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        UserName.setText("");
                        Password.setText("");
                        UserName.requestFocus();

                        String toSpeak = "User Name And Password Is Wrong";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(MainActivity.this,toSpeak,Toast.LENGTH_SHORT).show();
                        return;
                    }

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

                params.put("UserName",username);
                params.put("Password",password);
                params.put("UserType",usertype);

                Log.d("Login",username + " " + password + " " + usertype);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest,"req_Login");
    }


    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserName.setText("");
        Password.setText("");
        UserName.requestFocus();

          ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

          if(cm.getActiveNetworkInfo() == null){
              String toSpeak = "Please Internet On";
              t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
              Toast.makeText(MainActivity.this,toSpeak,Toast.LENGTH_SHORT).show();
          }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
