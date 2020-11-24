package com.HITech.HILearn.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.HITech.HILearn.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DiscussionActivity extends AppCompatActivity {
//mtoolbar is for the tool bar which will be appearing ontop of the screen.
    private Toolbar mToolbar;
    private ViewPager myviewPager;
    private TabLayout myTabLayout;
    private  TabsAccessorAdapter myTabsAccessorAdapter;
    private AdView mAdView;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    Handler addHandler = new Handler();
    ProgressBar progress_bar;
    private DatabaseReference rootRef;
    private Dialog dialog;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        for (int i = 0; i<100;i++) {
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();

            mAdView.loadAd(adRequest);
        }
//        for(int i= 0; i<1; i++){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.dismiss();
//            addHandler.postDelayed(addRunnable, 5);
//        }
//        progressDialog.dismiss();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();


        Toolbar toolbar = findViewById(R.id.main_app_bar);
        toolbar.setNavigationOnClickListener(v -> backIntent());
//
//        mToolbar = (Toolbar) findViewById(R.id.main_app_bar);
//        setSupportActionBar(mToolbar);
        ImageView image = findViewById(R.id.ivProfile);
        TextView name = findViewById(R.id.name);
        name.setText("HILEARN CHAT ROOM");
//        getSupportActionBar().setTitle("HILEARN CHAT ROOM");
//        dialog = new ProgressDialog(DiscussionActivity.this);
//        dialog.setCancelable(false);
//        dialog.setTitle("Loading... please wait");
//        dialog.show();

        coordinatorLayout = findViewById(R.id.coordinator);
if (!isconnected()){
    Snackbar snackBar = Snackbar .make(coordinatorLayout, "No Network Connection...... please On your Data", Snackbar.LENGTH_INDEFINITE) .setAction("", new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    });
    snackBar.setActionTextColor(Color.BLUE);
    View snackBarView = snackBar.getView();

    snackBar.show();
}else {




    myviewPager = (ViewPager) findViewById(R.id.mains_tabs_pager);
    myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
    myviewPager.setAdapter(myTabsAccessorAdapter);

    myTabLayout = (TabLayout) findViewById(R.id.mains_tabs);
    myTabLayout.setupWithViewPager(myviewPager);
    ImageView profile = findViewById(R.id.ivProfile);
    profile.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.ic_launcher));
//    if((myviewPager.toString().length() <= 0)){
//        dialog.dismiss();
//    }
}

//if((myviewPager.toString().length()<=2)){
//    progressDialog.show();
//        }else{
////    progressDialog.dismiss();
//        }
//        dialog.dismiss();
//        progressDialog.dismiss();
    }
    public void onBackPressed() {
        backIntent();
    }

    public void backIntent() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
//If there is no user then it sends the user to the login page.
        if(currentUser == null){
            sendUserToLoginActivity();
        }else{
            verifyUserExistence();


        }
    }

    private void verifyUserExistence() {
        //gets the user id.
        final String currentUserId = currentUser.getUid();
//references the firebase database
        rootRef = FirebaseDatabase.getInstance().getReference();

//this creates a child in the database Users and regs the users with there unique userId.
        rootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //then if it is created already it toasts welcome.
                if((dataSnapshot.child("name").exists())){
                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                dialog.dismiss();

            }

        });
//        dialog.dismiss();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

//        if(item.getItemId() == R.id.main_logout_options);{
//          mAuth.signOut();
////          sendUserToLoginActivity();
//          sendUserToLoginActivity();
//        }
//
//       if(item.getItemId() == R.id.main_find_friends_options);{
//            sendUserToSettingsActivity();
//        }
//
//        if(item.getItemId() == R.id.main_settings_options);{
//
//        }
//        if(item.getItemId() == R.id.main_create_group_options);{
//            requestNewGroup();
//
//        }
        switch (item.getItemId()){
//            case R.id.main_find_friends_options:
//          sendUserToFindFriendsActivity();
//                Toast.makeText(this, "main friends", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.main_create_group_options:
//                requestNewGroup();
//                Toast.makeText(this, "create group", Toast.LENGTH_SHORT).show();
//                return true;

            case R.id.main_logout_options:
                mAuth.signOut();
                sendUserToLoginActivity();
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                return true;
        }
        return  true;
    }

    private void requestNewGroup() {
        //brings up an alert dialoog for the user to enter the group name.
        AlertDialog.Builder builder = new AlertDialog.Builder(
                DiscussionActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name :");

        final EditText groupNameField = new EditText(getApplicationContext());
        groupNameField.setHint("e.g hilearn");
        builder.setView(groupNameField);

        builder.setPositiveButton("create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName)){
                    groupNameField.setError("");
                    Toast.makeText(getApplicationContext(), "Pls write your group name...", Toast.LENGTH_SHORT).show();
                }
                else{
                    createNewGroup(groupName);

                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

             dialogInterface.cancel();

            }
        });
        builder.show();

    }

    private void createNewGroup(final String groupName) {

        rootRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(
                new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), groupName +
                            "group is created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserToLoginActivity() {
        Intent loginintent = new Intent(DiscussionActivity.this, AuthenticationActivity.class);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginintent);
        finish();
    }

//    private void showDialog() {
//        // For showing
//        KLoadingSpin a = findViewById(R.id.KLoadingSpin);
//        a.startAnimation();
//        a.setIsVisible(true);
//
//
//
//    }
//    private void dismisDialog() {
//        // For hiding
//        KLoadingSpin a = findViewById(R.id.KLoadingSpin);
//        a.stopAnimation();


//    }
    public boolean isconnected(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else{
            connected = false;
        }
        return connected;
    }

}
