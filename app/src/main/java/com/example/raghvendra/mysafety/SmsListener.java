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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {

        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){


            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                mMessage = smsMessage.getMessageBody();

            }

           if(mMessage.contains(mCompareString))
               NotificationUtils.remindUserBecauseCharging(context,mMessage);
        }
    }
}
