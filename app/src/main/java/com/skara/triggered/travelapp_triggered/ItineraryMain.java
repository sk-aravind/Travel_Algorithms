package com.skara.triggered.travelapp_triggered;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItineraryMain extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    final Context c = this;
    List<HomeScreen.Destination> dest_list;
    String list_stringified;
    String val_firebase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new myFragmentPagerAdapter(getSupportFragmentManager(),
                ItineraryMain.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Get phone number
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        final String mPhoneNumber = tMgr.getLine1Number();
        System.out.println(mPhoneNumber);


        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
        } else {
//            mUserId = mFirebaseUser.getUid();

            final Button button = (Button) findViewById(R.id.addButton);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mPhoneNumber == null){


                    }
                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                    View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialong_box, null);
                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                    alertDialogBuilderUserInput.setView(mView);

                    //getting list of data
                    StringBuilder sb = new StringBuilder();
                    dest_list = HomeScreen.iti_list;
                    for (HomeScreen.Destination i : dest_list) {
                        sb.append(i.id);
                    }

                    list_stringified = sb.toString();
                    char[] chars = list_stringified.toCharArray();
                    Arrays.sort(chars);
                    final String sorted = new String(chars);


                    final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                    alertDialogBuilderUserInput
                            .setCancelable(false)
                            .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
//                                    mDatabase.child("users").child("travelplans").child(userInputDialogEditText.getText().toString()).setValue(sorted);
//                                    mDatabase.child("users").child("phone").child(userInputDialogEditText.getText().toString()).setValue(mPhoneNumber);

                                    Query queryRef = mDatabase.child("users").child("travelplans").orderByValue().equalTo(sorted);
                                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                // TODO: handle the post
                                                val_firebase = postSnapshot.getKey();
                                                System.out.println(val_firebase);

                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Getting Post failed, log a message
                                            // ...
                                        }
                                    });


                                    new SweetAlertDialog(c, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success!")
                                            .setContentText("Would you like to travel with a friend and save some money?")
                                            .setConfirmText("Yes")
                                            .setCancelText("No")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    System.out.println(val_firebase+" -firebase");
                                                    System.out.println(userInputDialogEditText.getText().toString()+" -local");

                                                    if (val_firebase == null || val_firebase == userInputDialogEditText.getText().toString()) {
                                                        sDialog
                                                                .setTitleText("Oops...")
                                                                .setContentText("Looks like there aren't any matches at the moment!")
                                                                .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                                        mDatabase.child("users").child("travelplans").child(userInputDialogEditText.getText().toString()).setValue(sorted);
                                                        mDatabase.child("users").child("phone").child(userInputDialogEditText.getText().toString()).setValue(mPhoneNumber);

                                                    }else {
                                                        sDialog
                                                                .setTitleText("Found a Match")
                                                                .setContentText("Send " + val_firebase + " a message")
                                                                .setConfirmText("SMS")
                                                                .setCustomImage(R.drawable.haram)
                                                                .setConfirmClickListener(null)
                                                                .changeAlertType(SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                                                        mDatabase.child("users").child("travelplans").child(userInputDialogEditText.getText().toString()).setValue(sorted);
                                                        mDatabase.child("users").child("phone").child(userInputDialogEditText.getText().toString()).setValue(mPhoneNumber);
                                                       }
                                                }
                                            })
                                            .show();
                                }
                            })

                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
                                            dialogBox.cancel();
                                            mDatabase.child("users").child("travelplans").child(userInputDialogEditText.getText().toString()).setValue(sorted);
                                            mDatabase.child("users").child("phone").child(userInputDialogEditText.getText().toString()).setValue(mPhoneNumber);
                                        }
                                    });

                    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();

                }
            });


        }
    }



}

