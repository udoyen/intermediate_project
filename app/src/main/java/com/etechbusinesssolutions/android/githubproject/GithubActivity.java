package com.etechbusinesssolutions.android.githubproject;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class GithubActivity extends AppCompatActivity implements LoaderCallbacks<List<Github>> {

    public  static final String LOG_TAG = GithubActivity.class.getName();

    URL url;

    /**
     * Adapter for the list of Github users
     */
    private GithubAdapter mGithubAdapter;

    /**
     * URL for github users data from github API
     */
    private static final String GITHUB_LAGOS_USERS_URL = "https://api.github.com/search/users";

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Progressbar that is displayed before loader loads data
     */
    private ProgressBar progressBar;

    /**
     * Constant value for the github loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     *
     */
    private static final int GITHUB_LOADER_ID = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.github_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView githubListView = (ListView) findViewById(R.id.list);

        // Get the empty  Text view
        mEmptyStateTextView= (TextView) findViewById(R.id.empty);

        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a new adapter that takes an empty
        // list of github users as input
        mGithubAdapter = new GithubAdapter(this, new ArrayList<Github>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        githubListView.setAdapter(mGithubAdapter);

        //TODO: add activity intent for click event


        // Get a reference to the ConnectivityManager to check state of network connectivity
        Log.i(LOG_TAG, "TEST: Connectivity Manager Instance created ...");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //check internet connection
        Log.i(LOG_TAG, "TEST: Internet connection checked ...");
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();


        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){

            // Get a reference to the loader manager in order to interact with loaders
            Log.i(LOG_TAG, "TEST: Get the LoadManager being used ...");
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.i(LOG_TAG, "TEST: Calling initloader()...");
            loaderManager.initLoader(GITHUB_LOADER_ID, null, this);

        } else {

            // progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            Log.i(LOG_TAG, "TEST: progressbar made visible ...");
            progressBar.setVisibility(View.GONE);

            //if there's no data to show. display TextView to no internet connection
            mEmptyStateTextView.setText(R.string.no_internet_connection);

        }
    }



    @Override
    public Loader<List<Github>> onCreateLoader(int id, Bundle args) {

        //Create a new loader for the given URL
        Log.i(LOG_TAG, "TEST: onCreateLoader() called ...");

        Uri baseUri = Uri.parse(GITHUB_LAGOS_USERS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //uriBuilder.appendQueryParameter("access_token", "eeaee5ef796ddefc59bcaa17a3889987ba905470"); //TODO: Remove this line
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


        Log.i(LOG_TAG, "TEST: uriBuilder String" + uriBuilder.toString());

        return new GithubLoader(this, url.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Github>> loader, List<Github> data) {

        Log.i(LOG_TAG, "TEST: progressbar made invisible ...");
        progressBar.setVisibility(View.GONE);

        Log.i(LOG_TAG, "TEST: onLoadFinished() called ...");


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

        Log.i(LOG_TAG, "TEST: onLoadReset() called ...");
        //Loader reset, so we can clear out our existing data
        mGithubAdapter.clear();

    }
}
