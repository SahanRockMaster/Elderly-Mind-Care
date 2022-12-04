package com.project.elderlymindcare.activities;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.project.elderlymindcare.CallLogs;
import com.project.elderlymindcare.DialModel;
import com.project.elderlymindcare.adapters.UsersAdapter;
import com.project.elderlymindcare.models.User;
import com.project.elderlymindcare.R;
import com.project.elderlymindcare.listener.UsersListener;
import com.project.elderlymindcare.utilities.Constants;
import com.project.elderlymindcare.utilities.PreferenceManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements UsersListener {


    protected static final int RESULT_SPEECH = 1;
    private static final String CHANNEL_ID = "reminder";
    private PreferenceManager preferenceManager;
    private List<User> users;
    private UsersAdapter usersAdapter;
    private TextView textErrorMessage,type,uid,textView,spokenText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageConference,speak;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(getApplicationContext());


        //Reminder notification

        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        Query ref1 = database1.getReference().child(preferenceManager.getString(Constants.KEY_USER_ID)).child("Dialled").orderByKey().limitToFirst(1);
        ref1.addChildEventListener(new ChildEventListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CallLogs callLogs = snapshot.getValue(CallLogs.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "reminder";
                    String description = "Reminder for Calling";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                    channel.setDescription(description);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
                //Check the diallers
                Query ref2 = database1.getReference().child(preferenceManager.getString(Constants.KEY_USER_ID)).child("Dialled").orderByKey().limitToLast(1);
                ref2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        CallLogs callLogs2 = snapshot.getValue(CallLogs.class);
                        if (!Objects.requireNonNull(callLogs).getName().equals(Objects.requireNonNull(callLogs2).getName())){
                            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_meet_app)
                                    .setContentTitle("Call Reminder")
                                    .setContentText("Check whether you have contacted "+callLogs.getName()+" recently")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                            notificationManager.notify(1, builder1.build());
                        }else {
                            Query ref3 = database1.getReference().child(preferenceManager.getString(Constants.KEY_USER_ID)).child("Dialled").orderByKey().limitToFirst(2);
                            ref3.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    CallLogs callLogs3 = snapshot.getValue(CallLogs.class);
                                    if (!Objects.requireNonNull(callLogs3).getName().equals(callLogs2.getName())){
                                        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_meet_app)
                                                .setContentTitle("Call Reminder")
                                                .setContentText("Check whether you have contacted "+callLogs3.getName()+" recently")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                                        notificationManager.notify(1, builder1.build());
                                    }
                                    else {
                                        Query ref4 = database1.getReference().child(preferenceManager.getString(Constants.KEY_USER_ID)).child("Dialled").orderByKey().limitToFirst(3);
                                        ref4.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                CallLogs callLogs4 = snapshot.getValue(CallLogs.class);
                                                NotificationCompat.Builder builder1 = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                                                        .setSmallIcon(R.drawable.ic_meet_app)
                                                        .setContentTitle("Call Reminder")
                                                        .setContentText("Check whether you have contacted "+ Objects.requireNonNull(callLogs4).getName()+" recently")
                                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                                                notificationManager.notify(1, builder1.build());
                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            }

                                            @Override
                                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Finish Checking




            }



            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











        preferenceManager = new PreferenceManager(getApplicationContext());

        imageConference = findViewById(R.id.imageConference);
        type=findViewById(R.id.type);
        uid=findViewById(R.id.uid);
        speak=findViewById(R.id.speak);
        textView = findViewById(R.id.textTitle);
        spokenText=findViewById(R.id.spokenText);



        textView.setText(String.format(
                "%s %s",
                preferenceManager.getString(Constants.KEY_FIRST_NAME),
                preferenceManager.getString(Constants.KEY_LAST_NAME)
        ));
        type.setText(
                preferenceManager.getString(Constants.KEY_ACCOUNT_TYPE)
        );
        uid.setText(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );

        findViewById(R.id.textSignOut).setOnClickListener(view -> signOut());

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
            if (s !=null){
                sendFCMTokenToDatabase(s);
            }
        });

        RecyclerView usersRecyclerview = findViewById(R.id.recyclerViewUsers);
        textErrorMessage = findViewById(R.id.textErrorMessage);

        //loading all users to recycler view

        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users, this);
        usersRecyclerview.setAdapter(usersAdapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getUsers);

        getUsers();


        speak.setOnLongClickListener(view -> {
            speak.setColorFilter(MainActivity.this.getResources().getColor(R.color.colorAccent));
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
            try {
                startActivityForResult(intent, RESULT_SPEECH);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "An error occured", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return false;
        });




        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.page_2:
                    Intent intent=new Intent(MainActivity.this,CallLog.class);
                    startActivity(intent);
                    break;
                case R.id.page_3:
                    if (type.getText().toString().equals("Elder")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("How do you want to continue?");
                        builder.setTitle("Choose Destination");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Clinic", (dialog, which) -> {
                            Intent intent1=new Intent(MainActivity.this,CheckStatus.class);
                            intent1.putExtra("myId",uid.getText().toString());
                            startActivity(intent1);
                        });

                        builder.setNegativeButton("Measurements", (dialog, which) -> {
                            Intent intent1=new Intent(MainActivity.this,Measurements.class);
                            intent1.putExtra("myId",uid.getText().toString());
                            startActivity(intent1);
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else {
                        Intent intent1=new Intent(MainActivity.this,ListOfElders.class);
                        intent1.putExtra("myId",uid.getText().toString());
                        startActivity(intent1);
                    }


                    break;
                case R.id.page_1:
                    Intent intent1=new Intent(MainActivity.this,FrequentList.class);
                    startActivity(intent1);
                    break;



            }

            return false;
        });















    }

    @SuppressLint("NotifyDataSetChanged")
    private void getUsers() {
        swipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    swipeRefreshLayout.setRefreshing(false);
                    String myUsersId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        users.clear();
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                            if (myUsersId.equals(documentSnapshot.getId())) {
                                continue;
                            }

                            User user = new User();
                            user.firstName  = documentSnapshot.getString(Constants.KEY_FIRST_NAME);
                            user.lastName   = documentSnapshot.getString(Constants.KEY_LAST_NAME);
                            user.email      = documentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token      = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.type       = documentSnapshot.getString(Constants.KEY_ACCOUNT_TYPE);
                            user.reason="Answer my call";
                            //Passes recognized text
                            user.spokenText = spokenText.getText().toString();
                            user.phone_number=documentSnapshot.getString(Constants.KEY_NUMBER);
                            //Passing user id of the calling person
                            user.uid2       = documentSnapshot.getId();
                            //Passing my UserId
                            user.uid       =  uid.getText().toString();
                            //Passing my username
                            user.myname    =  textView.getText().toString();
                            //Passing my user type
                            user.mytype    = type.getText().toString();
                            users.add(user);
                        }

                        if (users.size() > 0) {
                            usersAdapter.notifyDataSetChanged();
                        } else {
                            textErrorMessage.setText(String.format("%s", "No users available"));
                            textErrorMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textErrorMessage.setText(String.format("%s", "No users available"));
                        textErrorMessage.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void sendFCMTokenToDatabase(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to send token: "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void signOut() {
        Toast.makeText(this, "Signing Out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.clearPreferences();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String mod = text.get(0).toLowerCase(Locale.ROOT);
                    spokenText.setText(mod);
                    getUsers();
                    if (mod.equals("I am not fine".toLowerCase(Locale.ROOT))){
                        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        database.collection(Constants.KEY_COLLECTION_USERS)
                                .get()
                                .addOnCompleteListener(task -> {
                                    swipeRefreshLayout.setRefreshing(false);
                                    String myUsersId = preferenceManager.getString(Constants.KEY_USER_ID);
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        users.clear();
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            if (myUsersId.equals(documentSnapshot.getId())) {
                                                continue;
                                            }
                                            int a=0;
                                            while (a<100){
                                                User user = new User();
                                                user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME);
                                                user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME);
                                                user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                                                user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                                user.type = documentSnapshot.getString(Constants.KEY_ACCOUNT_TYPE);
                                                user.reason="I am not feeling well";
                                                //Passes recognized text
                                                user.spokenText = spokenText.getText().toString();
                                                user.phone_number = documentSnapshot.getString(Constants.KEY_NUMBER);
                                                //Passing user id of the calling person
                                                user.uid2 = documentSnapshot.getId();
                                                //Passing my UserId
                                                user.uid = uid.getText().toString();
                                                //Passing my username
                                                user.myname = textView.getText().toString();
                                                //Passing my user type
                                                user.mytype = type.getText().toString();
                                                users.add(user);
                                                if(Objects.requireNonNull(documentSnapshot.getString(Constants.KEY_ACCOUNT_TYPE)).equals("Caregiver")){
                                                    initiateAudioMeeting(user);
                                                    break;
                                                }else {
                                                    a++;
                                                }


                                            }



                                        }
                                    }
                                });
                    }else if (!mod.equals("I am not fine".toLowerCase(Locale.ROOT))){
                        try {
                            final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                            DatabaseReference ref1 = database1.getReference().child("Dial").child(mod);
                            ref1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DialModel dialModel = dataSnapshot.getValue(DialModel.class);
                                    User user = new User();
                                    try {
                                        user.firstName = Objects.requireNonNull(dialModel).getName();
                                        user.lastName = "";
                                        user.token = dialModel.getToken();
                                        user.reason="I am not feeling well";
                                        user.uid = uid.getText().toString();
                                        user.myname = textView.getText().toString();
                                        user.reason="Need to call";
                                        user.phone_number=dialModel.getNumber();
                                        users.add(user);
                                        initiateAudioMeeting(user);
                                    }
                                    catch (NullPointerException nullPointerException ){
                                        Toast.makeText(MainActivity.this, "No user is available on that name", Toast.LENGTH_SHORT).show();
                                    }






                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });
                        }catch (Exception e){
                            Toast.makeText(this, "Dialler is offline at the moment", Toast.LENGTH_SHORT).show();
                        }

                    }

                    }

                }
        }


    @Override
    public void initiateVideoMeeting(User user) {


        if (user.token == null || user.token.trim().isEmpty()) {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            SmsManager sms=SmsManager.getDefault();
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
            sms.sendTextMessage(user.phone_number, null, "Data Off", pi,null);
            Toast.makeText(this, user.firstName+ " " +user.lastName+ " is not available for meeting", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "video");
            startActivity(intent);        }
    }

    //Audio calling

    @Override
    public void initiateAudioMeeting(User user) {
        if (user.token == null || user.token.trim().isEmpty()) {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(user.phone_number, null, "Data Off", pi,null);
            Toast.makeText(this, user.firstName+ " " +user.lastName+ " is offline at the moment", Toast.LENGTH_SHORT).show();
        } else {


            Intent intent = new Intent(getApplicationContext(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "audio");
            startActivity(intent);
        }
    }

    @Override
    public void onMultipleUsersAction(Boolean isMultipleUsersSelected) {
        if (isMultipleUsersSelected) {
            imageConference.setVisibility(View.VISIBLE);
            imageConference.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), OutgoingInvitationActivity.class);
                intent.putExtra("selectedUsers", new Gson().toJson(usersAdapter.getSelectedUsers()));
                intent.putExtra("type", "video");
                intent.putExtra("isMultiple", true);
                startActivity(intent);
            });
        } else {
            imageConference.setVisibility(View.GONE);
        }
    }






}