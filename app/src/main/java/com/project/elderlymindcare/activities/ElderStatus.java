package com.project.elderlymindcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.elderlymindcare.MeasureClass;
import com.project.elderlymindcare.R;
import com.project.elderlymindcare.ScoreClass;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Objects;

public class ElderStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_status);
        String uid=getIntent().getStringExtra("uid");





        final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
        DatabaseReference ref11 = database11.getReference().child("Status").child(uid).child("mypoints");
        try {
            ref11.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ScoreClass scoreClass = dataSnapshot.getValue(ScoreClass.class);
                    String score= Objects.requireNonNull(scoreClass).getScore();
                    String Tscore=scoreClass.getTotal();
                    int myscore=Integer.parseInt(score);
                    int total=Integer.parseInt(Tscore);

                    PieChart chart = findViewById(R.id.pie_chart);

                    chart.addPieSlice(new PieModel("Elder Health", myscore, Color.parseColor("#03fc84")));
                    chart.addPieSlice(new PieModel("Total Count", total, Color.parseColor("#fc2803")));

                    chart.startAnimation();



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });










            Query query = FirebaseDatabase.getInstance().getReference().child("Measurements").child(uid);
            FirebaseListOptions<MeasureClass> options = new FirebaseListOptions.Builder<MeasureClass>()
                    .setLayout(R.layout.custom_elder_status)
                    .setLifecycleOwner(ElderStatus.this)
                    .setQuery(query, MeasureClass.class)
                    .build();
            FirebaseListAdapter adapter = new FirebaseListAdapter(options) {
                @Override
                protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                    TextView date = v.findViewById(R.id.date);
                    TextView pressure = v.findViewById(R.id.pressure);
                    TextView weight = v.findViewById(R.id.weight);
                    TextView sugar=v.findViewById(R.id.sugar);
                    MeasureClass mClass = (MeasureClass) model;
                    date.setText(mClass.getDate());
                    pressure.setText(mClass.getPressure());
                    weight.setText(mClass.getWeight());
                    sugar.setText(mClass.getSugar());


                }

            };
            ListView listView = findViewById(R.id.listview);
            listView.setAdapter(adapter);
        }catch (NullPointerException e){
            setContentView(R.layout.error);
        }

    }


}