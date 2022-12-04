package com.project.elderlymindcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.elderlymindcare.DialModel;
import com.project.elderlymindcare.ElderClass;
import com.project.elderlymindcare.R;
import com.project.elderlymindcare.ScoreClass;
import com.project.elderlymindcare.utilities.Constants;
import com.project.elderlymindcare.utilities.PreferenceManager;

import java.util.HashMap;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword,inputNumber;
    private MaterialButton buttonSignUp;
    private ProgressBar signUpProgress;
    private TextView type;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferenceManager = new PreferenceManager(getApplicationContext());
        signUpProgress    = findViewById(R.id.progressBarSignUp);

        findViewById(R.id.imgBack).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.textSignIn).setOnClickListener(view -> onBackPressed());

        inputFirstName       = findViewById(R.id.inputFirstName);
        inputLastName        = findViewById(R.id.inputLastName);
        inputEmail           = findViewById(R.id.inputEmail);
        inputPassword        = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonSignUp         = findViewById(R.id.buttonSignUp);
        type                 = findViewById(R.id.type);
        inputNumber          = findViewById(R.id.inputNumber);

        Spinner spinner = findViewById(R.id.spinner1);
        spinner.setSelection(1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                type.setText(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonSignUp.setOnClickListener(view -> {
            if (inputFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
            } else if (inputLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (inputEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
                Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
            } else if (inputPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            } else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
            } else if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                Toast.makeText(SignUpActivity.this, "Password & confirm password must be same", Toast.LENGTH_SHORT).show();
            }else if (inputNumber.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
            } else {
                signUp();
            }
        });
    }

    private void signUp() {
        buttonSignUp.setVisibility(View.INVISIBLE);
        signUpProgress.setVisibility(View.VISIBLE);

        //register

        FirebaseFirestore database    = FirebaseFirestore.getInstance();
        HashMap<String, Object> users = new HashMap<>();
        users.put(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString());
        users.put(Constants.KEY_LAST_NAME, inputLastName.getText().toString());
        users.put(Constants.KEY_EMAIL, inputEmail.getText().toString());
        users.put(Constants.KEY_PASSWORD, inputPassword.getText().toString());
        users.put(Constants.KEY_ACCOUNT_TYPE, type.getText().toString());
        users.put(Constants.KEY_NUMBER, inputNumber.getText().toString());
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(users)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString());
                    preferenceManager.putString(Constants.KEY_LAST_NAME, inputLastName.getText().toString());
                    preferenceManager.putString(Constants.KEY_NUMBER, inputNumber.getText().toString());
                    preferenceManager.putString(Constants.KEY_ACCOUNT_TYPE, type.getText().toString());
                    preferenceManager.putString(Constants.KEY_EMAIL, inputEmail.getText().toString());

                    DialModel dialModel=new DialModel();
                    dialModel.setToken(documentReference.getId());
                    dialModel.setNumber(inputNumber.getText().toString());
                    String name=String.format(
                            "%s %s",
                            inputFirstName.getText().toString(),
                            inputLastName.getText().toString());
                    dialModel.setName(name);
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Dial");
                    ref.child(name.toLowerCase(Locale.ROOT)).setValue(dialModel);

                    if (type.getText().toString().equals("Elder")){
                        ElderClass elderClass=new ElderClass();
                        elderClass.setName(inputFirstName.getText().toString()+" "+inputLastName.getText().toString());
                        elderClass.setEmail(inputEmail.getText().toString());
                        elderClass.setUid(documentReference.getId());
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Elders");
                        reference.push().setValue(elderClass);
                        ScoreClass scoreClass=new ScoreClass();
                        scoreClass.setScore("0");
                        scoreClass.setTotal("0");
                        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("Status").child("mypoints");
                        reference1.setValue(scoreClass);
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    buttonSignUp.setVisibility(View.VISIBLE);
                    signUpProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignUpActivity.this, "Error woi : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}