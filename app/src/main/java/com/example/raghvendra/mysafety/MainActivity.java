package com.example.raghvendra.mysafety;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raghvendra.mysafety.ContactsACT.ContactsActivity;
import com.example.raghvendra.mysafety.Data.DetailsContract;
import com.example.raghvendra.mysafety.Data.PersonDetailsDBHelper;
import com.example.raghvendra.mysafety.Utilities.SmsUtilities;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.Query;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener ,LocationListener{
    private static final String TAG=MainActivity.class.getName();

    private static final int LOCATION_REQUEST = 500;
    private static final int LOCATION_REQUEST1 = 501;
    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth mFirebseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseStorage mFirebaseStorage;
    private  AsyncTask Back;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private int counter=0;


    private ImageView mImageView;
    private GoogleApiClient mGoogleApiClient;
    private  boolean flag;


    private  String latitude;
    private  String longitude;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private SQLiteDatabase mDb;
    private Cursor mCursor;
    private  static FirebaseUser user;
    private LocationManager locationManager;
    private String Phone_no;
    private String Username;
    private static ArrayList<String> Police_contacts;

    private LocationRequest mLocationRequest;
    String UserId ="";
    private  String address="";
    private String date="";
    String key="";
boolean flag2=true;
    private  String status="Not Accepted";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("Police_contacts")) {
                Police_contacts = savedInstanceState.getStringArrayList("Police_contacts");
            }
    }

            mImageView=findViewById(R.id.imageView1);

      mFirebseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mFirebaseStorage=FirebaseStorage.getInstance();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        mMessagesDatabaseReference=mFirebaseDatabase.getReference();

        Back=new AsyncTask() {


            @Override
            protected Object doInBackground(Object[] objects) {
                Police_contacts=get_police_contacts();
                return  Police_contacts;


            }


        };




        mGoogleApiClient =new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        getLocationrequest();
      mGoogleApiClient.connect();
       try {
           Back.execute();

       }
       catch (Exception e){

           Toast.makeText(MainActivity.this,"Failed to fetch the Police data Make sure that Internet is on",Toast.LENGTH_LONG).show();
       }

           mImageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {


                   ConnectivityManager connMgr=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                   NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                 if(networkInfo != null && networkInfo.isConnected()) {
                     if (Police_contacts != null) {

                         if (mGoogleApiClient != null) {


                             if (longitude != null && latitude != null) {
                                 Double slatitude = new Double(latitude);
                                 Double slongitude = new Double(longitude);

                                 if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                     ActivityCompat.requestPermissions((Activity) MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);


                                 }


                                 flag = SmsUtilities.sendSMSMessage(MainActivity.this, slatitude, slongitude);
                                 if (flag = true) {
                                     address = SmsUtilities.getAddress();

                                     SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, KK:mm");
                                     date = df.format(Calendar.getInstance().getTime());


                                     Current_Location current_location = new Current_Location(String.valueOf(longitude), String.valueOf(latitude), UserId, address, date, Phone_no, Username, key, status);

                                     mMessagesDatabaseReference.child("DATA").child(key).setValue(current_location);

                                 } else {
                                     Toast.makeText(MainActivity.this, "Make sure that your Device is connected to Internet ", Toast.LENGTH_LONG).show();

                                 }

                             } else {

                                 Toast.makeText(MainActivity.this, "Make sure that Your Device GPS is ON", Toast.LENGTH_LONG).show();

                             }
                         }
                     }
                     else {

                         Toast.makeText(MainActivity.this,  " Police Data is not loaded Make sure that your Device is connected to Internet And Press the Refresh button", Toast.LENGTH_LONG).show();

                     }
                 }
                 else {

                     Toast.makeText(MainActivity.this, "Make sure that your Device is connected to Internet ", Toast.LENGTH_LONG).show();
                 }

               }
           });










        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 user=firebaseAuth.getCurrentUser();
                if(user!=null) {
                    key=user.getUid();
                 Username=user.getDisplayName();
                 UserId = user.getEmail();

                   String compare1=getForm(UserId);

                    if(compare1.equals("") ){

                   Intent intent=new Intent(MainActivity.this,FormActivity.class);
                   startActivity(intent);
                }
                else {
                        if(flag2){
                              Toast.makeText(MainActivity.this, "You're now signed in. Welcome to My Safety app .", Toast.LENGTH_SHORT).show();

                        flag2=false;
                        }
                }

             }
                else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())).setTheme(R.style.LoginTheme).setLogo(R.mipmap.ic_launcherlogo).build(),
                                                                                            RC_SIGN_IN);


                }
            }
        };


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putStringArrayList("Police_contacts",Police_contacts);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {


                     Toast.makeText(this, "Sign in Successful", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled Make your is Device is Connected to active Network", Toast.LENGTH_SHORT).show();
                finish();

            }

        }


    }

    @Override
    protected void onStart() {
       if(this. mGoogleApiClient != null){
            this. mGoogleApiClient.connect();
        }


        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!flag2){

            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null){

            mFirebseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context=MainActivity.this;
         switch (item.getItemId()){
             case R.id.sign_out_menu:
                 AuthUI.getInstance().signOut(MainActivity.this);
                 return true;

             case  R.id.contacts_group:
                 Intent contactsActivity=new Intent(context, ContactsActivity.class);
                 startActivity(contactsActivity);
                 return true;

             case R.id.refresh:
                 Intent intent=new Intent(getIntent());
                 startActivity(intent);

                 return true;


             default:
                 return super.onOptionsItemSelected(item);
         }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST1);
            return;


        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=Double.toString(location.getLatitude());
        longitude=Double.toString(location.getLongitude());
    }


    private String getForm(String UserId){
        PersonDetailsDBHelper dbHelper=new PersonDetailsDBHelper(MainActivity.this);
        mDb=dbHelper.getReadableDatabase();
        String compare="";
        mCursor=mDb.query(DetailsContract.DetailsEntry.TABLE_NAME,new String[]{DetailsContract.DetailsEntry.EMAIL_ADD,DetailsContract.DetailsEntry.PHONE_NO},
                DetailsContract.DetailsEntry.EMAIL_ADD+" = ? ",new String[]{UserId},null,null,null);
       if(mCursor.getCount()>0){
           mCursor.moveToFirst();

            compare= mCursor.getString((mCursor.getColumnIndex(DetailsContract.DetailsEntry.EMAIL_ADD)));
              Phone_no=mCursor.getString(mCursor.getColumnIndex(DetailsContract.DetailsEntry.PHONE_NO));
             return  compare;
       }

       else {

           return compare;
       }





    }


    public static  FirebaseUser getUserdetails(){


        return user;

    }


   public void getLocationrequest() {


       if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

               ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
               return;



           }

           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, new android.location.LocationListener() {
               @Override
               public void onLocationChanged(Location location) {
                   latitude = Double.toString(location.getLatitude());
                   longitude = Double.toString(location.getLongitude());


               }

               @Override
               public void onStatusChanged(String provider, int status, Bundle extras) {

               }

               @Override
               public void onProviderEnabled(String provider) {

               }

               @Override
               public void onProviderDisabled(String provider) {

               }
           });


       }
   }


   public static ArrayList<String> getpolicecontacts(){




return Police_contacts;
   }



  private  ArrayList<String>  get_police_contacts(){

      mMessagesDatabaseReference=mFirebaseDatabase.getReference();
      mMessagesDatabaseReference.child("POLICE_DATA").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              Police_contacts = new ArrayList<String>();

              for (DataSnapshot ds : dataSnapshot.getChildren()) {
                  Police_data data = ds.getValue(Police_data.class);

                  Police_contacts.add(data.getMphone_no());


              }

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });


return  Police_contacts;
  }


}
