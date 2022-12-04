package com.project.elderlymindcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.elderlymindcare.CallLogs;
import com.project.elderlymindcare.R;
import com.project.elderlymindcare.utilities.Constants;
import com.project.elderlymindcare.utilities.PreferenceManager;

public class CallLog extends AppCompatActivity {
    FirebaseListAdapter adapter,adapter1;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);

        LinearLayout incoming=findViewById(R.id.incoming);
        LinearLayout outgoing=findViewById(R.id.outgoing);

        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());


        //Incoming Call Listview


        Query query = FirebaseDatabase.getInstance().getReference().child(preferenceManager.getString(Constants.KEY_USER_ID)).child("Incoming");
        FirebaseListOptions<CallLogs> options = new FirebaseListOptions.Builder<CallLogs>()
                .setLayout(R.layout.item_container_user)
                .setLifecycleOwner(CallLog.this)
                .setQuery(query, CallLogs.class)
                .build();
        adapter =new FirebaseListAdapter(options) {
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
                    imageAudioMeeting.setImageDrawable(ContextCompat.getDrawable(CallLog.this, R.drawable.ic_audio));
                    imageAudioMeeting.setColorFilter(ContextCompat.getColor(CallLog.this, R.color.blue_tint));
                }if (call_type.equals("Incoming Video Call")){
                    imageAudioMeeting.setVisibility(View.VISIBLE);
                    imageVideoMeeting.setVisibility(View.GONE);
                    imageAudioMeeting.setImageDrawable(ContextCompat.getDrawable(CallLog.this, R.drawable.ic_video));
                    imageAudioMeeting.setColorFilter(ContextCompat.getColor(CallLog.this, R.color.blue_tint));
                }


            }
        };
        ListView listView = findViewById(R.id.incoming_listview);
        listView.setAdapter(adapter);


        //Outgoing Call Listview


        Query query1 = FirebaseDatabase.getInstance().getReference().child(preferenceManager.getString(Constants.KEY_USER_ID)).child("Dialled");
        FirebaseListOptions<CallLogs> options1 = new FirebaseListOptions.Builder<CallLogs>()
                .setLayout(R.layout.item_container_user)
                .setLifecycleOwner(CallLog.this)
                .setQuery(query1, CallLogs.class)
                .build();
        adapter1 =new FirebaseListAdapter(options1) {
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
                if (call_type.equals("Dialled Audio Call")){
                    imageAudioMeeting.setVisibility(View.VISIBLE);
                    imageVideoMeeting.setVisibility(View.GONE);
                }if (call_type.equals("Dialled Video Call")){
                    imageAudioMeeting.setVisibility(View.GONE);
                    imageVideoMeeting.setVisibility(View.VISIBLE);
                }
                





            }
        };
        ListView listView1 = findViewById(R.id.outgoing_listview);
        listView1.setAdapter(adapter1);



        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.incoming:
                    incoming.setVisibility(View.VISIBLE);
                    outgoing.setVisibility(View.GONE);
                    item.setEnabled(true);

                    break;
                case R.id.outgoing:
                    incoming.setVisibility(View.GONE);
                    outgoing.setVisibility(View.VISIBLE);
                    item.setEnabled(true);
                    break;


                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return false;
        });

    }
}