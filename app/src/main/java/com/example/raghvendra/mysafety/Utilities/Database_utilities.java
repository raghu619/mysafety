package com.example.raghvendra.mysafety.Utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.raghvendra.mysafety.ContactsACT.ContactsActivity;
import com.example.raghvendra.mysafety.Data.DetailsContract;
import com.example.raghvendra.mysafety.Data.GroupdetailsDBHelper;
import com.example.raghvendra.mysafety.Police_data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by raghvendra on 28/2/18.
 */

public class Database_utilities  {
    private static SQLiteDatabase mDb;
    private static Cursor mCursor;


     static  ArrayList<String> police_contacts;


    public static ArrayList<String>  getContactsList(Context context){

        police_contacts=new ArrayList<String>();


        GroupdetailsDBHelper dbHelper = new GroupdetailsDBHelper(context);
        mDb=dbHelper.getReadableDatabase();
        mCursor = mDb.query(DetailsContract.GroupDetails.TABLE_NAME,
               new String[]{ DetailsContract.GroupDetails.PHONE_NO}, null, null, null, null, null);

        if(mCursor.getCount()!=0) {
            int UsernameIndex = mCursor.getColumnIndex(DetailsContract.GroupDetails.PHONE_NO);
            int count = mCursor.getCount();

            for (int i = 0; i < count; i++) {
                mCursor.moveToPosition(i);
                 police_contacts.add(mCursor.getString(UsernameIndex));



            }


            return police_contacts;
        }
        else {

            return null;
        }
    }


}
