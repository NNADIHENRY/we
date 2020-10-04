package com.HITech.HILearn.ui;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.HITech.HILearn.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import com.HITech.HILearn.R;

/**
 * Created by nnenna on 12/2/18.
 */

public class help extends AppCompatActivity{

    private ImageView call, facebook, whatsapp, instagram, twitter;

    public help() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        whatsapp = findViewById(R.id.whatsapp);
        twitter = findViewById(R.id.twitter);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        call = findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = "+2349057749021";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
          public void onClick(View v) {
                Uri uri = Uri.parse("https://chat.whatsapp.com/BfJJp8AhkAUANn8P2jDLB7");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://web.facebook.com/hilearn01");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/hilearn01/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://twitter.com/hilearn01");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
    }
}