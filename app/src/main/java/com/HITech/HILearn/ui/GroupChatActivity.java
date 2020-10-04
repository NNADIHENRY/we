package com.HITech.HILearn.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.HITech.HILearn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton sendMessageBtn;
    private EditText userMessageIn;
    private ScrollView mScrollView;
    private TextView displaySendName;
private Dialog dialog;
    ScrollView scrollView;
MainActivity mainActivity = new MainActivity();
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, groupNameRef, groupMessageKeyRef;

    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;
private  String  textName, phoneNum, textEmail;
ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
profile = findViewById(R.id.ivProfile);

          textName = (user.getDisplayName());
          phoneNum = (user.getPhoneNumber());
         textEmail = (user.getEmail());
        currentGroupName = getIntent().getExtras().get("groupName").toString().trim();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        groupNameRef = FirebaseDatabase.getInstance().getReference().child("Group").child(currentGroupName);
//        profile.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.jamb_icon));


        initializeFields();

        getUserInfo();

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageIntoDatabase();

                userMessageIn.setText("");

                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        groupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    displayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    displayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void onBackPressed() {
        backIntent();
    }

    public void backIntent() {

       Intent intent = new Intent(this, DiscussionActivity.class);
        startActivity(intent);
    }
    private void initializeFields() {

//        mToolbar = (Toolbar) findViewById(R.id.group_chat_bar_layout);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle(currentGroupName);
Toolbar toolbar = findViewById(R.id.group_chat_bar_layout);
        toolbar.setNavigationOnClickListener(v -> backIntent());

        TextView name = findViewById(R.id.name);
        name.setText(currentGroupName);
        if(currentGroupName.contains("JAMB")){
            profile.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.jamb_icon));
        }else if(currentGroupName.contains("NECO")){
            profile.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.neco_icon));
        }else{
            profile.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.waec_icon));
        }
        dialog = new ProgressDialog(GroupChatActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Loading... please wait");
        dialog.show();

        sendMessageBtn = (ImageButton) findViewById(R.id.send_message_button);
        userMessageIn = (EditText) findViewById(R.id.input_grp_message);

        displaySendName = (TextView) findViewById(R.id.group_chat);
//if(displaySendName.length() <= 1){
//    dialog.show();
//    return;
//}else if (displaySendName.length() >= 1){
//    dialog.dismiss();
//}
        mScrollView = (ScrollView) findViewById(R.id.my_scroll_view);
    }
    private void getUserInfo() {
        usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void saveMessageIntoDatabase() {
        String message =userMessageIn.getText().toString();
        String messageKey = groupNameRef.push().getKey();

        if (TextUtils.isEmpty(message)){
            Toast.makeText(this, "pls type message first", Toast.LENGTH_SHORT).show();
        }else{
            Calendar callForDate= Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(callForDate.getTime());

            Calendar callForTime= Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(callForDate.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupNameRef.updateChildren(groupMessageKey);

            groupMessageKeyRef = groupNameRef.child(messageKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            if (textName.length() <= 0 && phoneNum.length() > 0) {
                messageInfoMap.put("name", phoneNum);
//            messageInfoMap.put("email", textEmail);
                messageInfoMap.put("name", phoneNum);
                messageInfoMap.put("message", message);
                messageInfoMap.put("date", currentDate);
                messageInfoMap.put("time", currentTime);
                messageInfoMap.put("time", currentTime);


            }else {
                messageInfoMap.put("name", textName);
//            messageInfoMap.put("email", textEmail);
                messageInfoMap.put("name", textName);
                messageInfoMap.put("message", message);
                messageInfoMap.put("date", currentDate);
                messageInfoMap.put("time", currentTime);
                messageInfoMap.put("time", currentTime);
            }
            groupMessageKeyRef.updateChildren(messageInfoMap);


        }
    }
    private void displayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator =  dataSnapshot.getChildren().iterator();

        while (iterator.hasNext()){
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((String) ((DataSnapshot)iterator.next()).getValue()).toUpperCase();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();
//if(textName == chatName) {
//    displaySendName.setGravity(Gravity.RIGHT);
//    displaySendName.append(chatName + ":\n" + chatMessage + "\n" + chatTime +
//            "  " + chatDate + "\n\n");
//    dialog.dismiss();
//}else {

            displaySendName.append(chatName + ":\n" + chatMessage + "\n" + chatTime +
            "  " + chatDate + "\n\n");
            dialog.dismiss();
//}


            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

}
