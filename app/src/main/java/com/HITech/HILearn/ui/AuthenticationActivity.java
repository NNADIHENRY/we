package com.HITech.HILearn.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.HITech.HILearn.R;
import com.HITech.HILearn.calc.ScientificCal;
import com.HITech.HILearn.calc.StandardCal;
import com.HITech.HILearn.utils.Constant;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Collections;
import java.util.HashMap;


public class AuthenticationActivity extends AppCompatActivity {

    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;
    private static final int RC_SIGN_IN1 = 101;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private DatabaseReference rootRef;


    //Tag for the logs optional
    private static final String TAG = "HILEARN";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    //Declaration of EditText, Button, FirebaseAuth, String and SharedPreferences


    private String email_address, email_link,pending_email;


    private SharedPreferences pref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.setDefaultLanguage(this);
        setContentView(R.layout.activity_authentication);
        database = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        rootRef = database;

//first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.buttonPhoneAuth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPhoneLogin();
            }
        });
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
//        findViewById(R.id.emailsignin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDialog();
//            }
//        });


    }
    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN1) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
             //   showAlertDialog(user);
            } else {
                /**
                 *   Sign in failed. If response is null the user canceled the
                 *   sign-in flow using the back button. Otherwise check
                 *   response.getError().getErrorCode() and handle the error.
                 */
                Toast.makeText(getBaseContext(), "Phone Auth Failed", Toast.LENGTH_LONG).show();
            }
        }





        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(AuthenticationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getEmail()).
                                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(task.isSuccessful()){

                                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                                String currentUserId = mAuth.getCurrentUser().getUid().toString();

                                                rootRef.child("Users").child(currentUserId).push().setValue("");

                                                rootRef.child("Users").child(currentUserId).push().child("device_token")
                                                        .setValue(deviceToken);

                                                Log.d("pushed", "registered to database");

                                                String pho = user.getPhoneNumber();
                                                HashMap<String, String> profileMap = new HashMap<>();
                                                profileMap.put("phone", pho);
                                                rootRef.child("Users").child(currentUserId).setValue(profileMap);



                                            }
                                        }
                                    });




                            Toast.makeText(AuthenticationActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });


    }



    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void doPhoneLogin() {
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()))
                .setLogo(R.mipmap.ic_launcher)
                .build();
        startActivityForResult(intent, RC_SIGN_IN1);
    }
//
//private void email(){
//
//
//
//   }
//
//    //Implementing event handlers
//    public void onSignInClickedButton(String email) {
//
//        mAuth=FirebaseAuth.getInstance();
//
//        //Reading preferences if it?s already been set
//        pref=getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
//
//
//        //Checking for pending email?.
//        if(pending_email!=null){
//            Toast.makeText(getApplicationContext(), "wait for email link", Toast.LENGTH_LONG).show();
//
//            Log.d(TAG,"Getting Shared Preferences"+pending_email);
//        }
//
//        //Creating intent for catching the link
//        Intent intent = getIntent();
//        if (intent != null && intent.getData() != null) {
//            email_link = intent.getData().toString();
//            Log.d(TAG, "got an intent: " + email_link);
//
//            // Confirm the link is a sign-in with email link.
//
//        }
//
//
//
//
//
//
//
//        //Getting email address from the text field
//        email_address = email;
//
//        //Calling signInWithEmailLink using the FirebaseAuth instances with OnCompleteListener and OnComplete callback.
//        mAuth.signInWithEmailLink(email_address, email_link)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        // remove shared preferences, set everything back to default
//                        pending_email = null;
//                        SharedPreferences.Editor editor = pref.edit();
//
//                        editor.commit();
//
//
//                        //Checking for task
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "Successfully signed in with email link!", Toast.LENGTH_SHORT).show();
//                            AuthResult result = task.getResult();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Error signing in with email link", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//    private void showDialog() {
//        // Override active layout
//        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
//        View view = layoutInflater.inflate(R.layout.email    , null);
//        // AlertDialog used for pop-Ups
//        AlertDialog.Builder builder = new AlertDialog.Builder(AuthenticationActivity.this);
//        builder.setView(view);
//
//        // Used to link or get views in the dialogBox
//
//        builder.setCancelable(false)
////                positive button is used to indicate whether to save or update
//                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//
//                // Used to set Negative button to cancel
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialogBox, int id) {
//                        dialogBox.cancel();
//
//                    }
//                });
//
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//
//        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener
//                (new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Show toast message when no text is entered
//                        EditText email = alertDialog.findViewById(R.id.email);
//                        String Email = email.getText().toString().trim();
//                        onSignInClickedButton(Email);
//
//
//
//                        // check if user updating note
//
//
//                    }
//                });
//    }
//    public void onEmailClick(View view) {
//        email_address = email.getText().toString();
//        //Validation check
//        if (email_address.equals("")) {
//            Toast.makeText(getApplicationContext(), "Enter Email Address!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //Save Email Address using SharedPreference Editor.
//        SharedPreferences.Editor editor=pref.edit();
//        editor.putString(KEY_PENDING_EMAIL,email_address);
//        editor.commit();
//
//        //Set-up our ActionCodeSettings
//        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
//                .setAndroidPackageName(
//                        getPackageName(),
//                        false, /* install if not available? */
//                        null   /* minimum app version */)
//                .setHandleCodeInApp(true)
//                .setUrl("https://auth.example.com/emailSignInLink")
//                .build();
//
//        //Implement sendSignInLinkToEmail method to send the link to email address
//        mAuth.sendSignInLinkToEmail(email_address, actionCodeSettings)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
}
