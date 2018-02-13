package com.example.raghvendra.mysafety;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;
    private FirebaseAuth mFirebseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressBar mProgressBar;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotoStorageReference;
    private ImageButton mImageButton;
    private TextView mTextView;
    private ImageView mImageView;
    private GoogleApiClient mGoogleApiClient;
    private  Button mButton;
    private Button mButton2;
    private  String latitude;
    private  String longitude;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;


    private LocationRequest mLocationRequest;
    String UserId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        mImageView=findViewById(R.id.imageButton3);
        mButton=findViewById(R.id.button2);
        mButton2=findViewById(R.id.button3);
        mTextView=findViewById(R.id.textView);
        mFirebseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mFirebaseStorage=FirebaseStorage.getInstance();


        mPhotoStorageReference=mFirebaseStorage.getReference().child("Adhar_photos");
        mMessagesDatabaseReference=mFirebaseDatabase.getReference().child("DATA");


        mGoogleApiClient =new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mProgressBar.setVisibility(View.INVISIBLE);
       // mTextView.setText(""+Current_Location.getLatitude()+" "+Current_Location.getLongitude());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.connect();

            }
        });
        mButton2 =findViewById(R.id.button3);
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mGoogleApiClient!=null){

                    Current_Location current_location=new Current_Location(String.valueOf(longitude),String.valueOf(latitude),UserId);

                    mMessagesDatabaseReference.push().setValue(current_location);
                    Toast.makeText(MainActivity.this,"Successfully uploaded",Toast.LENGTH_LONG).show();

                }
            }
        });


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
              intent.setType("image/jpeg");
              intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
               startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
            }
        });





        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){

                   UserId =user.getEmail();
                    Toast.makeText(MainActivity.this,"You're now signed in. Welcome to My Safety app .",Toast.LENGTH_SHORT).show();
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
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();


            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
        else if (requestCode==RC_PHOTO_PICKER && resultCode==RESULT_OK){


            Uri selectedImageUri=data.getData();
            StorageReference Photoref=mPhotoStorageReference.child(selectedImageUri.getLastPathSegment());
            Photoref.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    Log.v(TAG,"SuccessFully_Added"+downloadUrl.toString());

                }
            });
            Toast.makeText(this,"Adhar Card Photo SuccessFully added",Toast.LENGTH_SHORT).show();
        }


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
        //mGoogleApiClient.disconnect();
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
         switch (item.getItemId()){
             case R.id.sign_out_menu:
                 AuthUI.getInstance().signOut(MainActivity.this);
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

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
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
        mTextView.setText(Double.toString(location.getLatitude())+"\n"+Double.toString(location.getLongitude()));
        latitude=Double.toString(location.getLatitude());
        longitude=Double.toString(location.getLongitude());
    }



//    public  class LoctionFinder  extends AsyncTask<GoogleApiClient,Void,String[]> implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,LocationListener{
//        public String latitude;
//        public String longitude;


//        @Override
//        protected String[] doInBackground(GoogleApiClient... googleApiClients) {
//            mGoogleApiClient.connect();
//            String mlatitude;
//            String mlongitude;
//            mlatitude=latitude;
//            mlongitude=longitude;
//            String value[]={mlatitude,mlongitude};
//            return value;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            super.onPreExecute();
//        }
//
//
//
//        @Override
//        protected void onPostExecute(String[] strings) {
//            mTextView.setText(""+strings[0]+"  "+strings[1]);
//            super.onPostExecute(strings);
//        }
//
//        @Override
//        public void onConnected(@Nullable Bundle bundle) {
//            mLocationRequest = LocationRequest.create();
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            mLocationRequest.setInterval(10);
//            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //public void onRequestPermissionsResult(int requestCode,  String[] permissions,
//                //                                 int[] grantResults){
//
//                // }
//
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) MainActivity.this);
//
//
//        }
//
//        @Override
//        public void onConnectionSuspended(int i) {
//
//        }
//
//
//
//        @Override
//        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//              latitude=Double.toString(location.getLatitude());
//              longitude=Double.toString(location.getLongitude());
//        }
//    }
//



}
