package com.project.elderlymindcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.elderlymindcare.CallLogs;
import com.project.elderlymindcare.R;
import com.project.elderlymindcare.utilities.Constants;
import com.project.elderlymindcare.utilities.PreferenceManager;

public class FrequentList extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequent_list);
        listView=findViewById(R.id.listview);

        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());



        Query query = FirebaseDatabase.getInstance().getReference().child(preferenceManager.getString(Constants.KEY_USER_ID)).child("Dialled").orderByKey().limitToLast(5);
        FirebaseListOptions<CallLogs> options = new FirebaseListOptions.Builder<CallLogs>()
                .setLayout(R.layout.item_container_user)
                .setLifecycleOwner(FrequentList.this)
                .setQuery(query, CallLogs.class)
                .build();
        FirebaseListAdapter adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView username=v.findViewById(R.id.textUserName);
                TextView type=v.findViewById(R.id.textType);
                TextView textFirstChar=v.findViewById(R.id.textFirstChar);
                ImageView imageAudioMeeting=v.findViewById(R.id.imageAudioMeeting);
                ImageView imageVideoMeeting=v.findViewById(R.id.imageVideoMeeting);
                CallLogs logs = (CallLogs) model;
                char firstChar = logs.getName().charAt(0);
                username.setText(logs.getName());
                type.setText(logs.getUser_type());
                textFirstChar.setText(String.valueOf(firstChar));
                String call_type=logs.getCall_type();
                if (call_type.equals("Incoming Audio Call")){
                    imageAudioMeeting.setVisibility(View.VISIBLE);
                    imageVideoMeeting.setVisibility(View.GONE);
                    imageAudioMeeting.setImageDrawable(ContextCompat.getDrawable(FrequentList.this, R.drawable.ic_audio));
                    imageAudioMeeting.setColorFilter(ContextCompat.getColor(FrequentList.this, R.color.blue_tint));
                }if (call_type.equals("Incoming Video Call")){
                    imageAudioMeeting.setVisibility(View.VISIBLE);
                    imageVideoMeeting.setVisibility(View.GONE);
                    imageAudioMeeting.setImageDrawable(ContextCompat.getDrawable(FrequentList.this, R.drawable.ic_video));
                    imageAudioMeeting.setColorFilter(ContextCompat.getColor(FrequentList.this, R.color.blue_tint));
                }


            }
        };
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
        
        
        

    }
}