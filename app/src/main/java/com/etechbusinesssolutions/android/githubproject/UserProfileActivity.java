package com.etechbusinesssolutions.android.githubproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    public static final String LOG_TAG = UserProfileActivity.class.getName();//TODO: Remove redundant code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle extras = getIntent().getExtras();
        Log.i(LOG_TAG, "Test: extras accessed ...");

        // Make sure extra actually captures something from the calling activity
        if (extras == null) {
            return;
        }

        // Pass the values from extras into variables
        String username = extras.getString("username");
        String image = extras.getString("image");
        String profile_url = extras.getString("profile_url");

        if (username != null) {
            // Set the username TextView
            TextView usernameTextView = (TextView) findViewById(R.id.username);//TODO: Remove redundant code
            usernameTextView.setText(username);
            Log.i(LOG_TAG, "Test: profile username added ...");//TODO: Remove redundant code
        }

        if (image != null) {
            Log.i(LOG_TAG, "Test: image url" + " " + image); //TODO: Remove redundant code
            // Set the user image using Picasso library
            ImageView userImage = (ImageView) findViewById(R.id.main_image); //TODO: Remove redundant code
            Picasso.with(this).load(image)
                    .resize(144, 144)
                    .transform(new ImageTrans_CircleTransform())
                    .into(userImage);
            Log.i(LOG_TAG, "Test: Profile Image added ..."); //TODO: Remove

        }


        if (profile_url != null) {
            // Set the username TextView
            TextView profileUrlTextView = (TextView) findViewById(R.id.profile_url);//TODO: Remove redundant code
            profileUrlTextView.setText(profile_url);

            Log.i(LOG_TAG, "Test: Profile url added ...");
        }
    }
}
