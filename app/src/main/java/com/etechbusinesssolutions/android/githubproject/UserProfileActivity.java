package com.etechbusinesssolutions.android.githubproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    public static final String LOG_TAG = UserProfileActivity.class.getName();//TODO: Remove redundant code

    /**
     * SwipeRefreshView
     */
    private SwipeRefreshLayout mySwipeRefreshLayout;

    private TextView profileUrlTextView;

    private ImageView userImage;

    private TextView usernameTextView;

    private String username = null;
    private String image = null;
    private String profile_url = null;

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
        username = extras.getString("username");
        image = extras.getString("image");
        profile_url = extras.getString("profile_url");

        // Update profile views user information
        Log.i(LOG_TAG, "Test: UpdateViews() to be called ...");
        updateViews();

        // Create a new FloatingActionButton Class
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);

        // Set a click event on the FAB
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create an Intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("text/plain");

                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer" + " " + "@" + username + "\n" + profile_url);
                shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, profile_url);

                // Start the intent
                startActivity(Intent.createChooser(shareIntent, "Share with"));
                //Toast.makeText(UserProfileActivity.this, "FAB caller", Toast.LENGTH_SHORT).show();
            }
        });

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        userPageRefreshAction();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_swipe, menu);
        return true;
    }

    public void userPageRefreshAction() {

        // Get a reference to the ConnectivityManager to check state of network connectivity
        Log.i(LOG_TAG, "TEST: Connectivity Manager Instance created from user profile activity..."); // TODO: Remove
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //check internet connection
        Log.i(LOG_TAG, "TEST: Internet connection checked from user profile activity...");
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();


        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

            updateViews();

        } else {


            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();

        }

    }

    public void updateViews(){

        Log.i(LOG_TAG, "Test: UpdateViews() called ...");

        if (username != null) {
            // Set the username TextView
            usernameTextView = findViewById(R.id.username);
            usernameTextView.setText(username);
            Log.i(LOG_TAG, "Test: profile username added ...");//TODO: Remove redundant code
        }

        if (image != null) {
            Log.i(LOG_TAG, "Test: image url" + " " + image); //TODO: Remove redundant code
            // Set the user image using Picasso library
            userImage = findViewById(R.id.main_image);
            Picasso.with(this).load(image)
                    .resize(144, 144)
                    .transform(new ImageTrans_CircleTransform())
                    .into(userImage);
            Log.i(LOG_TAG, "Test: Profile Image added ..."); //TODO: Remove

        }


        if (profile_url != null) {
            // Set the username TextView
            profileUrlTextView = findViewById(R.id.profile_url);
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
    }
}
