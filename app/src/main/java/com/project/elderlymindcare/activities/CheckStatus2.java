package com.project.elderlymindcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.project.elderlymindcare.R;

public class CheckStatus2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status2);
        String myId=getIntent().getStringExtra("myId");

        String count=getIntent().getStringExtra("count");

        Button yes=findViewById(R.id.yesbutton);
        Button no=findViewById(R.id.nobutton);

        no.setOnClickListener(view -> {
            Intent intent = new Intent(CheckStatus2.this, CheckStatus3.class);
            intent.putExtra("myId",myId);
            intent.putExtra("count", String.valueOf(Integer.parseInt(count)));
            startActivity(intent);
            finish();
        });

        yes.setOnClickListener(view -> {
            Intent intent = new Intent(CheckStatus2.this, CheckStatus3.class);
            intent.putExtra("myId",myId);
            intent.putExtra("count", String.valueOf(Integer.parseInt(count)+1));
            startActivity(intent);
            finish();
        });
    }
}