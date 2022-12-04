package com.project.elderlymindcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.elderlymindcare.ElderClass;
import com.project.elderlymindcare.R;

public class ListOfElders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_elders);
        Query query = FirebaseDatabase.getInstance().getReference().child("Elders");
        FirebaseListOptions<ElderClass> options = new FirebaseListOptions.Builder<ElderClass>()
                .setLayout(R.layout.item_container_user)
                .setLifecycleOwner(ListOfElders.this)
                .setQuery(query, ElderClass.class)
                .build();
        FirebaseListAdapter adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView textUserName = v.findViewById(R.id.textUserName);
                TextView textType = v.findViewById(R.id.textType);
                TextView textFirstChar = v.findViewById(R.id.textFirstChar);
                ImageView imageAudioMeeting=v.findViewById(R.id.imageAudioMeeting);
                ImageView imageVideoMeeting=v.findViewById(R.id.imageVideoMeeting);
                ConstraintLayout userContainer=v.findViewById(R.id.userContainer);
                ElderClass lib = (ElderClass) model;
                textUserName.setText(lib.getName());
                textType.setText(lib.getEmail());
                char firstChar = lib.getName().charAt(0);
                textFirstChar.setText(String.valueOf(firstChar));
                imageAudioMeeting.setVisibility(View.INVISIBLE);
                imageVideoMeeting.setVisibility(View.INVISIBLE);


                userContainer.setOnClickListener(view -> {
                    String uid = lib.getUid();
                    Intent intent = new Intent(ListOfElders.this, ElderStatus.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);

                });


            }

        };
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

    }
}