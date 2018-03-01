package com.example.raghvendra.mysafety.ContactsACT;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.raghvendra.mysafety.Data.DetailsContract;
import com.example.raghvendra.mysafety.Data.GroupdetailsDBHelper;
import com.example.raghvendra.mysafety.FormActivity;
import com.example.raghvendra.mysafety.R;

public class ContactForm extends AppCompatActivity {

    private SQLiteDatabase mDb;
    private EditText mEdittext1;
    private EditText mEdittext2;
    private Button mButton;

    private  ContentValues mContentvalues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);
        GroupdetailsDBHelper dbHelper=new GroupdetailsDBHelper(this);
         mDb=dbHelper.getWritableDatabase();
        mEdittext1=findViewById(R.id.editText2);
        mEdittext2=findViewById(R.id.edit_phone_number3);
        mButton=findViewById(R.id.submit_area4);


             mButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               long checkID;

                                               if (mEdittext1.getText().length() != 0 && mEdittext2.getText().length() != 0) {


                                                   checkID = addDetails(mEdittext1.getText().toString().trim(), mEdittext2.getText().toString().trim());

                                                   Intent submitActivity = new Intent(ContactForm.this, ContactsActivity.class);

                                                   Toast.makeText(ContactForm.this, "ROW ID" + checkID, Toast.LENGTH_LONG).show();
                                                   startActivity(submitActivity);


                                               } else {

                                                   return;
                                               }
                                           }
                                       }
            );







    }


    private long addDetails(String name,String phone){
        mContentvalues=new ContentValues();
        mContentvalues.put(DetailsContract.GroupDetails.USER_NAME,name);
        mContentvalues.put(DetailsContract.GroupDetails.PHONE_NO,phone);


        return  mDb.insert(DetailsContract.GroupDetails.TABLE_NAME,null,mContentvalues) ;
    }






}
