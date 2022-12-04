package com.project.elderlymindcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.project.elderlymindcare.R;

public class CaptureMessageActivity extends AppCompatActivity {

    private static final int SMS_REQUEST_CODE = 101;
    public static final String EXTRA_SMS_SENDER="extra_sms_sender";
    public static final String EXTRA_SMS_message="extra_sms_message";
    TextView tvSmsFrom,tvSmsContent;

    @Override
    protected void onStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CaptureMessageActivity.this.startActivity(new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY));
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_message);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},SMS_REQUEST_CODE);
        }
        tvSmsFrom=findViewById(R.id.tv_sms_from);
        tvSmsContent=findViewById(R.id.tv_sms_content);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!intent.hasExtra(EXTRA_SMS_SENDER)&& !intent.hasExtra(EXTRA_SMS_message)){
            return;
        }
        String sms_sender=intent.getExtras().getString(EXTRA_SMS_SENDER);
        String sms_content=intent.getExtras().getString(EXTRA_SMS_message);
        tvSmsFrom.setText(sms_sender);
        tvSmsContent.setText(sms_content);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==SMS_REQUEST_CODE){
            if (grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}