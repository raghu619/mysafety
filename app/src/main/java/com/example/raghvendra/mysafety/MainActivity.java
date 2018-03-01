package com.example.raghvendra.mysafety;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.Query;

import java.util.Arrays;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener ,LocationListener{
    private static final String TAG=MainActivity.class.getName();
    public static final String ANONYMOUS="anonymous";
    private static final int LOCATION_REQUEST = 500;
    private static final int LOCATION_REQUEST1 = 501;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;
    private FirebaseAuth mFirebseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressBar mProgressBar;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotoStorageReference;
    private ImageButton mImageButton;
    private TextView mTextView;
    private TextView mTextView1;
    private ImageView mImageView;
    private GoogleApiClient mGoogleApiClient;
    private  Button mButton;
    private Button mButton2;
    private  String latitude;
    private  String longitude;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private SQLiteDatabase mDb;
    private Cursor mCursor;
    private  static FirebaseUser user;
    private LocationManager locationManager;

    private LocationRequest mLocationRequest;
    String UserId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
     //   mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
       mImageView=findViewById(R.id.imageView1);
//        mButton=findViewById(R.id.button2);
//        mButton2=findViewById(R.id.button3);
//        mTextView=findViewById(R.id.textView);
//        mTextView1=findViewById(R.id.textView2);
      mFirebseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mFirebaseStorage=FirebaseStorage.getInstance();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


       //mPhotoStorageReference=mFirebaseStorage.getReference().child("Adhar_photos");
        mMessagesDatabaseReference=mFirebaseDatabase.getReference().child("DATA");


        mGoogleApiClient =new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

       // mProgressBar.setVisibility(View.INVISIBLE);
        getLocationrequest();
        mGoogleApiClient.connect();
      // String test=getForm(UserId);


       // mTextView.setText(""+Current_Location.getLatitude()+" "+Current_Location.getLongitude());
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mGoogleApiClient.connect();
//                getLocationrequest();
//                               }
////
//
//
//
//            }
//        );

      //  mButton2 =findViewById(R.id.button3);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mGoogleApiClient!=null) {


                    if (longitude != null && latitude != null) {

                        Current_Location current_location = new Current_Location(String.valueOf(longitude), String.valueOf(latitude), UserId);

                        mMessagesDatabaseReference.push().setValue(current_location);
                        Toast.makeText(MainActivity.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                        SmsUtilities.sendSMSMessage(MainActivity.this);
                    }

                    else {

                        Toast.makeText(MainActivity.this, "Make sure that GPS is ON", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });


//        mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
//              intent.setType("image/jpeg");
//              intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
//               startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
//            }
//        });





        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 user=firebaseAuth.getCurrentUser();
                if(user!=null) {

                 UserId = user.getEmail();

                   String compare1=getForm(UserId);

                    if(compare1.equals("") ){

                   Intent intent=new Intent(MainActivity.this,FormActivity.class);
                   startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "You're now signed in. Welcome to My Safety app .", Toast.LENGTH_SHORT).show();
                }

//                    mCursor=getForm(UserId);
//                    mCursor.moveToFirst();
//                    String compare=mCursor.getString((mCursor.getColumnIndex(DetailsContract.DetailsEntry.EMAIL_ADD)));
//
//                    if (!compare.equals(UserId)) {
//
//                        Intent intent = new Intent(MainActivity.this, FormActivity.class);
//                        startActivity(intent);
//
//
//                    }
                }
                else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())).build(),
                                                                                            RC_SIGN_IN);


                }
            }
        };


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Sign in Successfully", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
//        else if (requestCode==RC_PHOTO_PICKER && resultCode==RESULT_OK){
//
//
//            Uri selectedImageUri=data.getData();
//            StorageReference Photoref=mPhotoStorageReference.child(selectedImageUri.getLastPathSegment());
//            Photoref.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
//                    Log.v(TAG,"SuccessFully_Added"+downloadUrl.toString());
//
//                }
//            });
//            Toast.makeText(this,"Adhar Card Photo SuccessFully added",Toast.LENGTH_SHORT).show();
//        }


    }

    @Override
    protected void onStart() {
      //  if(this. mGoogleApiClient != null){
        //    this. mGoogleApiClient.connect();
        //}


        super.onStart();
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
        //mTextView.setText(Double.toString(location.getLatitude())+"\n"+Double.toString(location.getLongitude()));
        latitude=Double.toString(location.getLatitude());
        longitude=Double.toString(location.getLongitude());
    }


    private String getForm(String UserId){
        PersonDetailsDBHelper dbHelper=new PersonDetailsDBHelper(MainActivity.this);
        mDb=dbHelper.getReadableDatabase();
        String compare="";
        mCursor=mDb.query(DetailsContract.DetailsEntry.TABLE_NAME,new String[]{DetailsContract.DetailsEntry.EMAIL_ADD},
                DetailsContract.DetailsEntry.EMAIL_ADD+" = ? ",new String[]{UserId},null,null,null);
       if(mCursor.getCount()>0){
           mCursor.moveToFirst();

            compare= mCursor.getString((mCursor.getColumnIndex(DetailsContract.DetailsEntry.EMAIL_ADD)));

             return  compare;
       }

       else {

           return compare;
       }


//        String compare = "";
//         if(mCursor.getCount()!=0) {
//             mCursor.moveToFirst();
//
//
//             compare = mCursor.getString((mCursor.getColumnIndex(DetailsContract.DetailsEntry.EMAIL_ADD)));
//             return compare;
//         }
//         else {
//
//
//             return compare;
//         }


    }


    public static  FirebaseUser getUserdetails(){


        return user;

    }


   public void getLocationrequest() {


       if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

               ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
               return;

               // TODO: Consider calling
               //    ActivityCompat#requestPermissions
               // here to request the missing permissions, and then overriding
               //public void onRequestPermissionsResult(int requestCode,  String[] permissions,
               //                                 int[] grantResults){

               // }

               // to handle the case where the user grants the permission. See the documentation
               // for ActivityCompat#requestPermissions for more details.

           }

           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, new android.location.LocationListener() {
               @Override
               public void onLocationChanged(Location location) {
                   latitude = Double.toString(location.getLatitude());
                   longitude = Double.toString(location.getLongitude());
               //    mTextView.setText("" + latitude + "  " + longitude);

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






}
