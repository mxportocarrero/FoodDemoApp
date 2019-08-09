package com.example.fooddemoapp.Models;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class CustomUser {
    // Budget Unique ID
    private String mUserUid = "";

    // User First name
    private String mUserFirstName = "";

    // User Last name
    private String mUserLastName = "";

    // User Phone number // por ahora solo seran celulares de peru
    private String mUserPhoneNumber = "";

    // User Email
    private String mUserEmail = "";

    // User Profile Image
    private String mUserImageUrl = "";

    // User address // Solo Arequipa
    private String mUserAddress = "";

    // Constructores personalizados

    public CustomUser(){

    }

    public CustomUser(FirebaseUser user){
        this.mUserUid = user.getUid();
        this.mUserEmail = user.getEmail();
        this.mUserFirstName = user.getDisplayName();
    }

    // Constructores por defecto


    public CustomUser(String mUserUid, String mUserFirstName, String mUserLastName, String mUserPhoneNumber, String mUserEmail, String mUserImageUrl, String mUserAddress) {
        this.mUserUid = mUserUid;
        this.mUserFirstName = mUserFirstName;
        this.mUserLastName = mUserLastName;
        this.mUserPhoneNumber = mUserPhoneNumber;
        this.mUserEmail = mUserEmail;
        this.mUserImageUrl = mUserImageUrl;
        this.mUserAddress = mUserAddress;
    }

    public String getmUserUid() {
        return mUserUid;
    }

    public String getmUserFirstName() {
        return mUserFirstName;
    }

    public String getmUserLastName() {
        return mUserLastName;
    }

    public String getmUserPhoneNumber() {
        return mUserPhoneNumber;
    }

    public String getmUserEmail() {
        return mUserEmail;
    }

    public String getmUserImageUrl() {
        return mUserImageUrl;
    }

    public String getmUserAddress() {
        return mUserAddress;
    }
}
