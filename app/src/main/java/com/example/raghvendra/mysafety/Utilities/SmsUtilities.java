package com.example.raghvendra.mysafety.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;


import com.example.raghvendra.mysafety.MainActivity;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by raghvendra on 28/2/18.
 */

public class SmsUtilities {
    private static String message;
    private static boolean flag=true;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;


    static   String address="";
    static ArrayList<String>  phones2;
    private  static FirebaseUser user;

    public static void  sendSMSMessage(Context context,double latitude,double longitude) {
        ArrayList<String> phone_nos;

       user=MainActivity.getUserdetails();

        phone_nos = Database_utilities.getContactsList(context);
         phones2= MainActivity.getpolicecontacts();


        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            address = addressList.get(0).getLocality() + ",";
            address += addressList.get(0).getSubLocality() + ",";
            address += addressList.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }


        message = "Need help For "+user.getDisplayName()+" From " + address;

        int count;
        if(phone_nos !=null)
              count = phone_nos.size();
        else{
            count=0;
        }
        int count2=0;
        if(count>0){
            try {
                 count2 = phones2.size();
            }
            catch (Exception e){

                Toast.makeText(context,"Failed to fetch the Police data Make sure that Internet is on",Toast.LENGTH_LONG).show();



            }
            for(int j=0;j<count2;j++)
                      phone_nos.add( phones2.get(j));

            count=phone_nos.size();
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);


            }

            for (int i = 0; i < count; i++) {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone_nos.get(i), null, message, null, null);
                //smsManager.sendTextMessage(phones2.get(i),null,message,null,null);
            }

            Toast.makeText(context, "SMS sent. and Successfully uploaded your request",

                    Toast.LENGTH_LONG).show();

        }

        else {

            Toast.makeText(context, "Add numbers to Contacts List", Toast.LENGTH_LONG).show();
            try {

                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);


                }

                int count1 = phones2.size();
                for (int i = 0; i < count1; i++) {

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phones2.get(i), null, message, null, null);
                }


                Toast.makeText(context, "SMS sent. and Successfully uploaded your request",

                        Toast.LENGTH_LONG).show();

            }
            catch (Exception e){

                Toast.makeText(context,"Failed to fetch the Police data Make sure that Internet is on",Toast.LENGTH_LONG).show();

            }

        }

    }

    public  static String getAddress(){

        if(!address.equals("")){


            return  address;

        }

        else
        {

            return " failed to fetch address";
        }

    }




}
