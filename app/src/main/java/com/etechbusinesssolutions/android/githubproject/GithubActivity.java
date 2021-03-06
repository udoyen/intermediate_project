package com.etechbusinesssolutions.android.githubproject;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class GithubActivity extends AppCompatActivity implements LoaderCallbacks<List<Github>> {

    // Used for logging
    public static final String LOG_TAG = GithubActivity.class.getName();


    /**
     * URL for github users data from github API
     */
    private static final String GITHUB_LAGOS_USERS_URL = "https://api.github.com/search/users";
    /**
     * Constant value for the github loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     */
    private static final int GITHUB_LOADER_ID = 1;
    // Used to setup UrlQuery String
    URL url = null;
    /**
     * Adapter for the list of Github users
     */
    private GithubAdapter mGithubAdapter;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    /**
     * Progressbar that is displayed before loader loads data
     */
    private ProgressBar progressBar;

    /**
     * SwipeRefreshLayout
     */
    private SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.github_activity);

        // Find a reference to the {@link ListView} in the layout
        final ListView githubListView = findViewById(R.id.list);

        // Get the empty  Text view
        mEmptyStateTextView = findViewById(R.id.empty);

        progressBar = findViewById(R.id.loading_spinner);

        // Create a new adapter that takes an empty
        // list of github users as input
        mGithubAdapter = new GithubAdapter(this, new ArrayList<Github>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        githubListView.setAdapter(mGithubAdapter);


        // Respond to click event on user item
        githubListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Get the current github user that was clicked
                Github currentGithubUser = mGithubAdapter.getItem(position);

                // Create a new intent to view user profile
                Intent userProfileIntent = new Intent(mGithubAdapter.getContext(), UserProfileActivity.class);

                // Parse in the username, image url and profile url into
                // the UserProfileActivity
                assert currentGithubUser != null;
                userProfileIntent.putExtra("username", currentGithubUser.getmUserName());
                userProfileIntent.putExtra("image", currentGithubUser.getmUserImageUrl());
                userProfileIntent.putExtra("profile_url", currentGithubUser.getmUserProfileUrl());

                // Send the intent to the user profile activity
                startActivity(userProfileIntent);
            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //check internet connection
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();


        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

            // Get a reference to the loader manager in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(GITHUB_LOADER_ID, null, this);

        } else {

            // progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);

            // Make sure the ListView is empty before displaying "No Internet Connection"
            if (mGithubAdapter.isEmpty()) {

                //if there's no data to show. display TextView to no internet connection
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            } else {

                //Display Toast message if Github Adapter is not empty
                Toast.makeText(mGithubAdapter.getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }


        }

        /*
          Use this code to prevent SwipeRefreshLayout from interfering with
          Scrolling in ListView
         */
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);

        githubListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (githubListView.getChildAt(0) != null) {

                    mySwipeRefreshLayout.setEnabled(githubListView.getFirstVisiblePosition() == 0 && githubListView.getChildAt(0).getTop() == 0);

                }
            }
        });

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userPageRefreshAction();
            }
        });


    }


    @Override
    public Loader<List<Github>> onCreateLoader(int id, Bundle args) {

        //Create a new loader for the given URL

        Uri baseUri = Uri.parse(GITHUB_LAGOS_USERS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", "location:lagos+language:java");
        uriBuilder.appendQueryParameter("page", "1");
        uriBuilder.appendQueryParameter("per_page", "100");


        try {
            try {
                // Used to prevent html encoding of query string
                url = new URL(URLDecoder.decode(uriBuilder.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return new GithubLoader(this, url.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Github>> loader, List<Github> data) {

        progressBar.setVisibility(View.GONE);


        // Update the UI with the result
        // Clear the adapter of previous earthquake data
        mGithubAdapter.clear();

        // If there is a valid list of {@link Earthquakes}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mGithubAdapter.addAll(data);
        } else {

            // Set empty state text to display "No users for location lagos found."
            mEmptyStateTextView.setText(R.string.empty_text);
            // Center the "No users for location lagos found" text
            mEmptyStateTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Github>> loader) {

        //Loader reset, so we can clear out our existing data
        mGithubAdapter.clear();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_swipe.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_swipe, menu);
        return true;
    }


    /*
     * Listen for option item selections so that we receive a notification
     * when the user requests a refresh by selecting the refresh action bar item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // Check if user triggered a refresh:
            case R.id.menu_refresh:

                // Signal SwipeRefreshLayout to start the progress indicator
                mySwipeRefreshLayout.setRefreshing(true);

                // Start the refresh background task.
                // This method calls setRefreshing(false) when it's finished.
                userPageRefreshAction();

                return true;
        }

        // User didn't trigger a refresh, let the superclass handle this action
        return super.onOptionsItemSelected(item);

    }

    public void userPageRefreshAction() {

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //check internet connection
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();


        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

            // Get a reference to the loader manager in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(GITHUB_LOADER_ID, null, this);

            // Remove "No Internet Connection" TextView on reconnecting
            // if Github Adapter was empty
            mEmptyStateTextView.setVisibility(View.INVISIBLE);

        } else {

            // Make sure the ListView is empty before displaying "No Internet Connection"
            if (mGithubAdapter.isEmpty()) {

                //if there's no data to show. display TextView to no internet connection
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            } else {

                //Display Toast message if Github Adapter is not empty
                Toast.makeText(mGithubAdapter.getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        }

        // Stop loading refreshing animation
        mySwipeRefreshLayout.setRefreshing(false);

    }
}
