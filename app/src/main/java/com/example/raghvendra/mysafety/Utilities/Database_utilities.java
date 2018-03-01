package com.example.raghvendra.mysafety.Utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.raghvendra.mysafety.ContactsACT.ContactsActivity;
import com.example.raghvendra.mysafety.Data.DetailsContract;
import com.example.raghvendra.mysafety.Data.GroupdetailsDBHelper;

/**
 * Created by raghvendra on 28/2/18.
 */

public class Database_utilities  {
    private static SQLiteDatabase mDb;
    private static Cursor mCursor;
    private static String[] contacts;

    public static String [] getContactsList(Context context){


        GroupdetailsDBHelper dbHelper = new GroupdetailsDBHelper(context);
        mDb=dbHelper.getReadableDatabase();
        mCursor = mDb.query(DetailsContract.GroupDetails.TABLE_NAME,
               new String[]{ DetailsContract.GroupDetails.PHONE_NO}, null, null, null, null, null);

        if(mCursor.getCount()!=0) {
            int UsernameIndex = mCursor.getColumnIndex(DetailsContract.GroupDetails.PHONE_NO);
            int count = mCursor.getCount();
            contacts = new String[count];
            for (int i = 0; i < count; i++) {
                mCursor.moveToPosition(i);

                contacts[i] = mCursor.getString(UsernameIndex);


            }


            return contacts;
        }
        else {

            return null;
        }
    }


}
