package com.project.elderlymindcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.elderlymindcare.R;
import com.project.elderlymindcare.ScoreClass;

import java.util.Objects;

public class CheckStatus3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status3);

        String count=getIntent().getStringExtra("count");
        String myId=getIntent().getStringExtra("myId");

        Button yes=findViewById(R.id.yesbutton);
        Button no=findViewById(R.id.nobutton);

        no.setOnClickListener(view -> {
            final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
            DatabaseReference ref11 = database11.getReference().child("Status").child(myId).child("mypoints");
            ref11.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        ScoreClass scoreClass = dataSnapshot.getValue(ScoreClass.class);
                        String score= Objects.requireNonNull(scoreClass).getScore();
                        String Tscore=scoreClass.getTotal();
                        int newScore=Integer.parseInt(score)+Integer.parseInt(count);
                        int newScore2=Integer.parseInt(Tscore)+3;
                        String FinalCount=String.valueOf(newScore);
                        String FinalCount2=String.valueOf(newScore2);
                        ScoreClass scoreclass=new ScoreClass();
                        scoreclass.setScore(FinalCount);
                        scoreclass.setTotal(FinalCount2);
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Status").child(myId);
                        reference.child("mypoints").setValue(scoreclass).addOnSuccessListener(unused -> Toast.makeText(CheckStatus3.this, "Done", Toast.LENGTH_SHORT).show());


                    }catch (Exception e){
                        ScoreClass score=new ScoreClass();
                        score.setScore(count);
                        score.setTotal("3");
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Status").child(myId);
                        reference.child("mypoints").setValue(score).addOnSuccessListener(unused -> Toast.makeText(CheckStatus3.this, "Done", Toast.LENGTH_SHORT).show());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Intent intent = new Intent(CheckStatus3.this, Done.class);
            intent.putExtra("myId",myId);
            intent.putExtra("count", String.valueOf(Integer.parseInt(count)));
            startActivity(intent);
            finish();
        });

        yes.setOnClickListener(view -> {
            Intent intent = new Intent(CheckStatus3.this, Done.class);
            intent.putExtra("myId",myId);
            intent.putExtra("count", String.valueOf(Integer.parseInt(count)+1));
            startActivity(intent);
            finish();
            final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
            DatabaseReference ref11 = database11.getReference().child("Status").child(myId).child("mypoints");
            ref11.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        ScoreClass scoreClass = dataSnapshot.getValue(ScoreClass.class);
                        String score= Objects.requireNonNull(scoreClass).getScore();
                        String Tscore=scoreClass.getTotal();
                        int newScore=Integer.parseInt(score)+Integer.parseInt(count);
                        int newScore2=Integer.parseInt(Tscore)+3;
                        String FinalCount=String.valueOf(newScore);
                        String FinalCount2=String.valueOf(newScore2);
                        ScoreClass scoreclass=new ScoreClass();
                        scoreclass.setScore(FinalCount);
                        scoreclass.setTotal(FinalCount2);
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Status").child(myId);
                        reference.child("mypoints").setValue(scoreclass).addOnSuccessListener(unused -> Toast.makeText(CheckStatus3.this, "Done", Toast.LENGTH_SHORT).show());


                    }catch (Exception e){
                        ScoreClass score=new ScoreClass();
                        score.setScore(count);
                        score.setTotal("3");
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Status").child(myId);
                        reference.child("mypoints").setValue(score).addOnSuccessListener(unused -> Toast.makeText(CheckStatus3.this, "Done", Toast.LENGTH_SHORT).show());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });


    }
}