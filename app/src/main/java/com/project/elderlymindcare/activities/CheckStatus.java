package com.project.elderlymindcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.project.elderlymindcare.R;

import java.util.Calendar;

public class CheckStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status);

        String myId=getIntent().getStringExtra("myId");

        int MorningCount=1;
        int NightCount=1;

        SharedPreferences preferences= CheckStatus.this.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String smCount=preferences.getString("mcount", String.valueOf(MorningCount));
        String snCount=preferences.getString("ncount", String.valueOf(NightCount));



        Button measures=findViewById(R.id.measures);
        measures.setOnClickListener(view -> {
            Intent intent=new Intent(CheckStatus.this,Measurements.class);
            intent.putExtra("myId",myId);
            startActivity(intent);
        });



        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay < 12){
            if (snCount.equals("0")){
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("ncount", String.valueOf(1));
                editor.apply();
            }
            if (smCount.equals("0")){
                LinearLayout done=findViewById(R.id.done);
                done.setVisibility(View.GONE);
                Button yes=findViewById(R.id.yesbutton);
                Button no=findViewById(R.id.nobutton);

                no.setOnClickListener(view -> {
                    Intent intent = new Intent(CheckStatus.this, CheckStatus2.class);
                    intent.putExtra("count", "0");
                    intent.putExtra("myId",myId);
                    startActivity(intent);
                    finish();
                });

                yes.setOnClickListener(view -> {
                    Intent intent = new Intent(CheckStatus.this, CheckStatus2.class);
                    intent.putExtra("count", "1");
                    intent.putExtra("myId",myId);
                    startActivity(intent);
                    finish();
                });
            }else if (smCount.equals("1")){
                LinearLayout done=findViewById(R.id.done);
                done.setVisibility(View.GONE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("mcount", String.valueOf(0));
                editor.apply();

                //do things here

                Button yes=findViewById(R.id.yesbutton);
                Button no=findViewById(R.id.nobutton);

                no.setOnClickListener(view -> {
                    Intent intent = new Intent(CheckStatus.this, CheckStatus2.class);
                    intent.putExtra("count", "0");
                    intent.putExtra("myId",myId);
                    startActivity(intent);
                    finish();
                });

                yes.setOnClickListener(view -> {
                    Intent intent = new Intent(CheckStatus.this, CheckStatus2.class);
                    intent.putExtra("count", "1");
                    intent.putExtra("myId",myId);
                    startActivity(intent);
                    finish();
                });
            }

        }else if(timeOfDay >= 20){
            if (smCount.equals("0")){
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("mcount", String.valueOf(1));
                editor.apply();
            }
            if (snCount.equals("0")){
                LinearLayout done=findViewById(R.id.done);
                done.setVisibility(View.VISIBLE);
            }else if (snCount.equals("1")){
                LinearLayout done=findViewById(R.id.done);
                done.setVisibility(View.GONE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("ncount", String.valueOf(0));
                editor.apply();
                Button yes = findViewById(R.id.yesbutton);
                Button no = findViewById(R.id.nobutton);

                no.setOnClickListener(view -> {

                    Intent intent = new Intent(CheckStatus.this, CheckStatus2.class);
                    intent.putExtra("count", "0");
                    intent.putExtra("myId",myId);
                    startActivity(intent);
                    finish();
                });
                yes.setOnClickListener(view -> {
                    Intent intent = new Intent(CheckStatus.this, CheckStatus2.class);
                    intent.putExtra("count", "1");
                    intent.putExtra("myId",myId);
                    startActivity(intent);
                    finish();
                });
            }

        }else {
            //delete this later if not needed
            LinearLayout done = findViewById(R.id.done);
            done.setVisibility(View.VISIBLE);
            Button yes=findViewById(R.id.yesbutton);
            Button no=findViewById(R.id.nobutton);

            no.setOnClickListener(view -> {
                Intent intent = new Intent(CheckStatus.this, CheckStatus2.class);
                intent.putExtra("count", "0");
                intent.putExtra("myId",myId);
                startActivity(intent);
                finish();
            });

            yes.setOnClickListener(view -> {
                Intent intent = new Intent(CheckStatus.this, CheckStatus2.class);
                intent.putExtra("count", "1");
                intent.putExtra("myId",myId);
                startActivity(intent);
                finish();
            });

        }
    }
}