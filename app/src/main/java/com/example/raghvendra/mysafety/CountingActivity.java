package com.example.raghvendra.mysafety;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class CountingActivity extends AppCompatActivity {
    private TextView mTextview;
   
    private  String fetchedtime;
    public static long Time_IN_MILLIS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);
        mTextview=findViewById(R.id.textView1);

        Intent display_intent=getIntent();
        if(display_intent.hasExtra(Intent.EXTRA_TEXT)){
          fetchedtime=display_intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        String[] splitStr = fetchedtime.trim().split("\\s+");
        if(splitStr[1].equals("hour")){
            int time_in_hours=Integer.parseInt(splitStr[0]);
            Time_IN_MILLIS = TimeUnit.HOURS.toMillis(time_in_hours);
            if(!splitStr[2].equals("") && splitStr[2]!=null) {
                int time_in_miniutes = Integer.parseInt(splitStr[2]);
                Time_IN_MILLIS+=TimeUnit.MINUTES.toMillis(time_in_miniutes);

            }


        }
        else {

            int time_in_miniutes = Integer.parseInt(splitStr[0]);

            Time_IN_MILLIS = TimeUnit.MINUTES.toMillis(time_in_miniutes);
        }
       new CountDownTimer(Time_IN_MILLIS,1000){


           @Override
           public void onTick(long millisUntilFinished) {
               long seconds=(millisUntilFinished/1000)%60;
               long minutes=(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))%60;
               mTextview.setText(""
                       +TimeUnit.MILLISECONDS.toHours(millisUntilFinished)+":"+minutes+":"+seconds);

           }



           @Override
           public void onFinish() {
     mTextview.setText("Done");
           }
       }.start();

    }
}
