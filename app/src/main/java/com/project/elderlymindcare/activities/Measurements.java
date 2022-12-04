package com.project.elderlymindcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.elderlymindcare.MeasureClass;
import com.project.elderlymindcare.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Measurements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);

        String myId=getIntent().getStringExtra("myId");

        Button update=findViewById(R.id.update);
        TextInputLayout pressure,weight,sugar;
        pressure=findViewById(R.id.pressure);
        weight=findViewById(R.id.weight);
        sugar=findViewById(R.id.sugar);
        update.setOnClickListener(view -> {
            String sPressure= Objects.requireNonNull(pressure.getEditText()).getText().toString();
            String sWeight= Objects.requireNonNull(weight.getEditText()).getText().toString();
            String sSugar= Objects.requireNonNull(sugar.getEditText()).getText().toString();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            MeasureClass measureClass=new MeasureClass();
            if (sPressure.equals(null)){
                measureClass.setPressure("No Data");
            }else {
                measureClass.setPressure(sPressure);
            }

            if (sWeight.equals(null)){
                measureClass.setWeight("No Data");
            }else {
                measureClass.setWeight(sWeight);
            }

            if (sSugar.equals(null)){
                measureClass.setSugar("No Data");
            }else {
                measureClass.setSugar(sSugar);
            }
            measureClass.setDate(dtf.format(now));
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Measurements").child(myId);
            reference.push().setValue(measureClass);
            Intent intent=new Intent(Measurements.this,Done.class);
            intent.putExtra("myId",myId);
            startActivity(intent);
            finish();

        });
    }
}