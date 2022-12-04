package com.project.elderlymindcare.adapters;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.elderlymindcare.CallLogs;
import com.project.elderlymindcare.R;
import com.project.elderlymindcare.activities.MainActivity;
import com.project.elderlymindcare.listener.UsersListener;
import com.project.elderlymindcare.models.User;
import com.project.elderlymindcare.utilities.Constants;
import com.project.elderlymindcare.utilities.PreferenceManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private List<User> users;
    private UsersListener usersListener;
    private List<User> selectedUsers;
    private PreferenceManager preferenceManager;


    public UsersAdapter(List<User> users, UsersListener usersListener) {
        this.users = users;
        this.usersListener = usersListener;
        selectedUsers = new ArrayList<>();
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_user,
                        parent,
                        false
                )

        );

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textFirstChar, textUsername, textType,number,uid2;
        ImageView imageAudioMeeting, imageVideoMeeting, imageSelected;
        ConstraintLayout userContainer;


        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textFirstChar     = itemView.findViewById(R.id.textFirstChar);
            textUsername      = itemView.findViewById(R.id.textUserName);
            textType         = itemView.findViewById(R.id.textType);
            imageAudioMeeting = itemView.findViewById(R.id.imageAudioMeeting);
            imageVideoMeeting = itemView.findViewById(R.id.imageVideoMeeting);
            userContainer     = itemView.findViewById(R.id.userContainer);
            imageSelected     = itemView.findViewById(R.id.imageSelected);
            number            =itemView.findViewById(R.id.number);
            uid2              =itemView.findViewById(R.id.uid2);
        }

        void setUserData(User user) {
            textFirstChar.setText(user.firstName.substring(0, 1));
            textUsername.setText(String.format("%s %s", user.firstName, user.lastName));
            textType.setText(user.type);
            if (textUsername.getText().toString().contains(user.spokenText)){
                textType.setText(user.spokenText);
            }
            number.setText(user.phone_number);
            uid2.setText(user.uid2);
            imageAudioMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usersListener.initiateAudioMeeting(user);
                    CallLogs logs=new CallLogs();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                    LocalDateTime now = LocalDateTime.now();
                    logs.setName(textUsername.getText().toString());
                    logs.setCall_type("Dialled Audio Call");
                    logs.setTime(dtf.format(now));
                    logs.setUser_type(textType.getText().toString());

                    CallLogs logs1=new CallLogs();
                    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                    LocalDateTime now1 = LocalDateTime.now();
                    logs1.setName(user.myname);
                    logs1.setCall_type("Dialled Audio Call");
                    logs1.setTime(dtf1.format(now1));
                    logs1.setUser_type(user.mytype);

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(user.uid).child("Dialled");
                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child(uid2.getText().toString()).child("Incoming");
                    reference.push().setValue(logs);
                    reference1.push().setValue(logs1);
                }
            });
              imageVideoMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usersListener.initiateVideoMeeting(user);
                    CallLogs logs=new CallLogs();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                    LocalDateTime now = LocalDateTime.now();
                    logs.setName(textUsername.getText().toString());
                    logs.setCall_type("Dialled Video Call");
                    logs.setTime(dtf.format(now));
                    logs.setUser_type(textType.getText().toString());

                    CallLogs logs1=new CallLogs();
                    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                    LocalDateTime now1 = LocalDateTime.now();
                    logs1.setName(user.myname);
                    logs1.setCall_type("Dialled Video Call");
                    logs1.setTime(dtf1.format(now1));
                    logs1.setUser_type(user.mytype);

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(user.uid).child("Dialled");
                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child(uid2.getText().toString()).child("Incoming");
                    reference.push().setValue(logs);
                    reference1.push().setValue(logs1);
                }
            });


            userContainer.setOnLongClickListener(view -> {
                if (imageSelected.getVisibility() != View.VISIBLE) {
                    selectedUsers.add(user);
                    imageSelected.setVisibility(View.VISIBLE);
                    imageVideoMeeting.setVisibility(View.GONE);
                    imageAudioMeeting.setVisibility(View.GONE);
                    usersListener.onMultipleUsersAction(true);
                }
                return true;
            });

            userContainer.setOnClickListener(view -> {
                if (imageSelected.getVisibility() == View.VISIBLE) {
                    selectedUsers.remove(user);
                    imageSelected.setVisibility(View.GONE);
                    imageVideoMeeting.setVisibility(View.VISIBLE);
                    imageAudioMeeting.setVisibility(View.VISIBLE);
                    if (selectedUsers.size() == 0) {
                        usersListener.onMultipleUsersAction(false);
                    }
                } else {
                    if (selectedUsers.size() > 0) {
                        selectedUsers.add(user);
                        imageSelected.setVisibility(View.VISIBLE);
                        imageVideoMeeting.setVisibility(View.GONE);
                        imageAudioMeeting.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
