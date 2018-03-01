package com.example.raghvendra.mysafety.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by raghvendra on 27/2/18.
 */

public class GroupdetailsDBHelper extends SQLiteOpenHelper {

    public static  final String DATABASE_NAME="groups.db";
    public static  final int DATABASE_VERSION=1;

    public GroupdetailsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final  String SQL_CREATE_DETAILS_TABLE="CREATE TABLE "+DetailsContract.GroupDetails.TABLE_NAME+" ("+
                DetailsContract.GroupDetails._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DetailsContract.GroupDetails.PHONE_NO+" TEXT  NOT NULL, "+
                DetailsContract.GroupDetails.USER_NAME+" TEXT  NOT NULL"+");";


        db.execSQL(SQL_CREATE_DETAILS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DetailsContract.GroupDetails.TABLE_NAME);
        onCreate(db);
    }
}
