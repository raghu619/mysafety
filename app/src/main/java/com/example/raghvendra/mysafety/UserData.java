package com.example.raghvendra.mysafety;

/**
 * Created by raghvendra on 15/3/18.
 */

public class UserData {

    private     String mUsername;
    private String mEmail;
    private String mPhone_no;
    private   String mAddress;
    private String mAdharId;
    UserData(){


    }

    UserData( String Username, String Email, String Phone_no,String AdharId, String Address){


        this.mUsername=Username;
        this.mEmail=Email;
        this.mPhone_no=Phone_no;
        this.mAddress=Address;
        this.mAdharId=AdharId;

    }



    public String getUsername(){

        return mUsername;
    }
    public String getEmail(){

        return mEmail;
    }
    public String getPhone_no(){

        return  mPhone_no;
    }
    public String getAdharId(){
        return mAdharId;
    }
    public String getAddress(){
        return mAddress;
    }

    public void  setUsername(String Username)
    {
        this.mUsername=Username;

    }
    public  void setEmail(String Email){

        this.mEmail=Email;
    }

    public  void setPhone_no(String Phone_no){
        this.mPhone_no=Phone_no;
    }

    public void setAdharId(String AdharId){

        this.mAdharId=AdharId;
    }
    public void setAddress(String Address){

        this.mAddress=Address;
    }

}
