package com.example.raghvendra.mysafety;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.raghvendra.mysafety.Data.DetailsContract;
import com.example.raghvendra.mysafety.Data.PersonDetailsDBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormActivity extends AppCompatActivity {


    private EditText mEdittext1;
    private FirebaseUser user;
    private SQLiteDatabase mDb;

    private EditText mEdittext2;
    private EditText mEdittext3;
    private EditText mEdittext4;
    private EditText mEdittext5;
    private Button mButton;
    private ContentValues mContentvalues;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseUser mFirebaseuser;
    private FirebaseAuth mFirebaseAuth;
  private   String Username;
     private String Email;
    private String Phone_no;
    private String Address;
    private String AdharId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form);
        mFirebaseAuth= FirebaseAuth.getInstance();
        mFirebaseuser=mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mMessagesDatabaseReference=mFirebaseDatabase.getReference().child("USER_DATA");
        PersonDetailsDBHelper dbHelper=new PersonDetailsDBHelper(this);
        mDb=dbHelper.getWritableDatabase();



        mEdittext5=findViewById(R.id.edit_Adhar_id);
        mEdittext1=findViewById(R.id.editText);
        mEdittext2=findViewById(R.id.email);
        mEdittext3=findViewById(R.id.edit_phone_number);
        mEdittext4=findViewById(R.id.edit_query);
        mButton=findViewById(R.id.submit_area);
        user=MainActivity.getUserdetails();
        mEdittext1.setText(user.getDisplayName());
        mEdittext2.setText(user.getEmail());




                mButton.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   long checkID;
                                                   if (mEdittext3.getText().length() != 0 && mEdittext4.getText().length() != 0 && mEdittext5.getText().length()!=0) {

                                                       checkID = addDetails(mEdittext3.getText().toString().trim()
                                                               , mEdittext4.getText().toString().trim(),
                                                               mEdittext1.getText().toString().trim(), mEdittext2.getText().toString().trim());
                                                       Username=mEdittext1.getText().toString().trim();
                                                       Email=mEdittext2.getText().toString().trim();
                                                       Phone_no=mEdittext3.getText().toString().trim();
                                                       AdharId=mEdittext5.getText().toString().trim();
                                                       Address=mEdittext4.getText().toString().trim();


                                                       UserData info=new UserData(Username,Email,Phone_no,AdharId,Address);
                                                          mMessagesDatabaseReference.child(mFirebaseAuth.getUid()).setValue(info);
                                                       Toast.makeText(FormActivity.this, "Information Stored Sucessfully" + checkID, Toast.LENGTH_LONG).show();
                                                       mDb.close();
                                                       finish();
                                                       Intent k=new Intent(FormActivity.this,MainActivity.class);
                                                       startActivity(k);


                                                   }
                                                   else {
                                                       Toast.makeText(FormActivity.this,"Fill all Details",Toast.LENGTH_LONG).show();
                                                       return;
                                                   }
                                               }
                                           }
                );









                }



    private long addDetails(String phone,String Address,String name,String email){
        mContentvalues=new ContentValues();
        mContentvalues.put(DetailsContract.DetailsEntry.USER_NAME,name);
        mContentvalues.put(DetailsContract.DetailsEntry.EMAIL_ADD,email);
        mContentvalues.put(DetailsContract.DetailsEntry.PHONE_NO,phone);
        mContentvalues.put(DetailsContract.DetailsEntry.ADDRESS,Address);

      return  mDb.insert(DetailsContract.DetailsEntry.TABLE_NAME,null,mContentvalues) ;
    }
}
