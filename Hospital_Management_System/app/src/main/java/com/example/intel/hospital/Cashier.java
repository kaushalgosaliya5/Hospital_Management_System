package com.example.intel.hospital;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Intel on 11/06/2016.
 */
public class Cashier extends Activity {

    Button btnP,btnPD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        Initialization();

        btnP.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer.create(Cashier.this, R.raw.click_one).start();

                try {
                    Intent i = new Intent(Cashier.this, Patient.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(Cashier.this," " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPD.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer.create(Cashier.this, R.raw.click_one).start();

                Intent i = new Intent(Cashier.this,PatientDetail.class);
                startActivity(i);

            }
        });
    }

    public void Initialization(){
        btnP = (Button)findViewById(R.id.btnP);
        btnPD = (Button)findViewById(R.id.btnPD);

    }

}
