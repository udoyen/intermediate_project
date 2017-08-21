package com.etechbusinesssolutions.android.githubproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
        final String username = extras.getString("username");
        String image = extras.getString("image");
        final String profile_url = extras.getString("profile_url");

        if (username != null) {
            // Set the username TextView
            TextView usernameTextView = findViewById(R.id.username);
            usernameTextView.setText(username);
            Log.i(LOG_TAG, "Test: profile username added ...");//TODO: Remove redundant code
        }

        if (image != null) {
            Log.i(LOG_TAG, "Test: image url" + " " + image); //TODO: Remove redundant code
            // Set the user image using Picasso library
            ImageView userImage = findViewById(R.id.main_image);
            Picasso.with(this).load(image)
                    .resize(144, 144)
                    .transform(new ImageTrans_CircleTransform())
                    .into(userImage);
            Log.i(LOG_TAG, "Test: Profile Image added ..."); //TODO: Remove

        }


        if (profile_url != null) {
            // Set the username TextView
            TextView profileUrlTextView = findViewById(R.id.profile_url);
            profileUrlTextView.setText(profile_url);

            Log.i(LOG_TAG, "Test: Profile url added ..."); //TODO: Remove

            /**
             * setup user profile click event
             */
            profileUrlTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Create an Intent for browser and parse in the URL of the
                    // user's profile
                    Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(profile_url));
                    startActivity(browser);

                }
            });
        }

        // Create a new FloatingActionButton Class
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);

        // Set a click event on the FAB
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create an Intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("text/plain");

                shareIntent.putExtra(Intent.EXTRA_TEXT, "@" + username + "" + profile_url);
                shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, profile_url);

                // Start the intent
                startActivity(Intent.createChooser(shareIntent, "Share with"));
                //Toast.makeText(UserProfileActivity.this, "FAB caller", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_swipe, menu);
        return true;
    }
}
