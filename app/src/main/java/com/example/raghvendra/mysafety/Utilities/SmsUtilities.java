package com.example.raghvendra.mysafety.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by raghvendra on 28/2/18.
 */

public class SmsUtilities {
    private static String message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    public static void sendSMSMessage(Context context){
        String[] phone_nos;

        phone_nos=Database_utilities.getContactsList(context);

        message = "Hi This is my safety";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);



            return;
        }

        int count=phone_nos.length;
        for(int i=0;i<count;i++) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone_nos[i], null, message, null, null);

        }

        Toast.makeText(context, "SMS sent.",

                Toast.LENGTH_LONG).show();

    }
}
