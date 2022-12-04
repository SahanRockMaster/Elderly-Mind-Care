package com.project.elderlymindcare;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.project.elderlymindcare.activities.CaptureMessageActivity;

public class SmsReceiver extends BroadcastReceiver {
    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        try{
            if (bundle != null){
                Object[] pdusObj=(Object[]) bundle.get("pdus");
                if (pdusObj != null){
                    for (Object aPdusObj : pdusObj){
                        SmsMessage currentMessage=getIncomingMessage(aPdusObj,bundle);
                        String senderNum=currentMessage.getDisplayOriginatingAddress();
                        String message=currentMessage.getDisplayMessageBody();
                        if (message.contains("Data Off")){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                Intent directIntent=new Intent(context, CaptureMessageActivity.class);
                                directIntent.putExtra(CaptureMessageActivity.EXTRA_SMS_SENDER,senderNum);
                                directIntent.putExtra(CaptureMessageActivity.EXTRA_SMS_message,message);

                                @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent=PendingIntent.getActivity(context,0,directIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                                pendingIntent.send();

                            }
                        }

                    }
                }

            }

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("ObsoleteSdkInt")
    private SmsMessage getIncomingMessage(Object object, Bundle bundle){
        SmsMessage currentSms;
        if (Build.VERSION.SDK_INT >=23){
            String format=bundle.getString("format");
            currentSms =SmsMessage.createFromPdu((byte[])object,format);
        }else {
            currentSms= SmsMessage.createFromPdu((byte[]) object);
        }
        return currentSms;
    }
}
