package com.example.raghvendra.mysafety;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;

import com.example.raghvendra.mysafety.Utilities.NotificationUtils;

/**
 * Created by raghvendra on 24/2/18.
 */

public class SmsListener extends BroadcastReceiver {
    private String mMessage;
    private String mCompareString="Police officer";
    private String mtime;
    int length;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {

        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){


            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                mMessage = smsMessage.getMessageBody();
//
            }




           if(mMessage.contains(mCompareString)) {
               String[] splitStr = mMessage.trim().split("\\s+");
               length = splitStr.length;
               mtime = splitStr[length-2];
               mtime+=" "+splitStr[length-1];

               if(mMessage.contains("hour")){

                   String[] splitStr1 = mMessage.trim().split("\\s+");
                   length = splitStr1.length;
                   mtime=splitStr1[length-4];
                   mtime+=" "+splitStr1[length-3];
                   mtime += " "+splitStr1[length-2];
                   mtime+=" "+splitStr[length-1];

               }

               NotificationUtils.remindUserBecauseCharging(context, mMessage,mtime);
           }
        }
    }
}
