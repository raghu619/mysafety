package com.example.raghvendra.mysafety.Data;

import android.provider.BaseColumns;

/**
 * Created by raghvendra on 14/2/18.
 */

public class DetailsContract {



    public static final class DetailsEntry implements BaseColumns{


        public static  final String TABLE_NAME="details";
        public static  final String USER_NAME="name";
        public static  final  String EMAIL_ADD="email";
        public static  final String  ADDRESS="Address";
        public static  final  String PHONE_NO="phone";




    }


    public static final class GroupDetails implements BaseColumns{
        public static  final String TABLE_NAME="groups";
        public static  final String USER_NAME="name";
        public static  final  String PHONE_NO="phone";



    }
}
