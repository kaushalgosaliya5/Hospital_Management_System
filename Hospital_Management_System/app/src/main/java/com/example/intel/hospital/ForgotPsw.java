package com.example.intel.hospital;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Intel on 11/06/2016.
 */
public class ForgotPsw extends Activity {

    Button Fhome;
    Button FSubmit;
    EditText Uname;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpsw);

        Initialize();

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
        Uname.setText("");
        Uname.requestFocus();
    }

    public void Initialize() {

        Uname = (EditText) findViewById(R.id.etFMobile);
        Fhome = (Button) findViewById(R.id.btnFHome);
        FSubmit = (Button) findViewById(R.id.btnFSubmit);

        Fhome.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer.create(ForgotPsw.this, R.raw.click_one).start();
                Intent i = new Intent(ForgotPsw.this, MainActivity.class);
                startActivity(i);
            }
        });


        FSubmit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(ForgotPsw.this, R.raw.click_one).start();

                if (Uname.getText().toString().equals("")) {
                    Uname.requestFocus();

                    String toSpeak = "Enter User Name";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(ForgotPsw.this, toSpeak, Toast.LENGTH_SHORT).show();
                    return;
                }


                String uname = Uname.getText().toString();
                ForgotMethod(uname);
            }
        });

    }

    public void ForgotMethod(final String username) {


        Log.d("ForgotPsw", "Registration Method");

        String url = "http://ksl.esy.es/usummer_school/uforgotpsw.php";

        Log.d("ForgotPsw", "URL : " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("ForgotPsw", "Response : " + response);

                try {
                    JSONObject mainObject = new JSONObject(response);

                    boolean SUCCESS_TAG = mainObject.getBoolean("success");

                    if (SUCCESS_TAG) {


                        String Name = mainObject.getString("Name");
                        String UserName = mainObject.getString("UserName");
                        String Password = mainObject.getString("Password");
                        String Mobile = mainObject.getString("Mobile");
                        String UserTyPe = mainObject.getString("UserType");


                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(Mobile, null, " Hi " + UserTyPe + " \n Dear :: " + Name + "\n UserName is :: " +
                                UserName + " \n Password Is :: " + Password, null, null);


                        String toSpeak = "Forgot Password Send";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(ForgotPsw.this, toSpeak, Toast.LENGTH_SHORT).show();

                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Intent i = new Intent(ForgotPsw.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {

                        Uname.setText("");
                        Uname.requestFocus();

                        String toSpeak = "User Name Is Wrong";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(ForgotPsw.this, toSpeak, Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Login", "Eeeeerrrrrroooooorrrrr");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("UserName", username);
                params.put("Password", username);

                Log.d("Login", username + "ksl");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "req_Login");
    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
