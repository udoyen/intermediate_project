package com.etechbusinesssolutions.android.githubproject;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static com.etechbusinesssolutions.android.githubproject.GithubActivity.LOG_TAG;


/**
 * Created by george on 8/20/17.
 */

public class GithubLoader extends AsyncTaskLoader<List<Github>> {

    private String mUrl;

    public GithubLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "Test: onStarting() called ...");
        forceLoad();
    }

    @Override
    public List<Github> loadInBackground() {
        Log.i(LOG_TAG, "Test: loadInBackground() called ...");
        // Create an Github list and return it here
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }

        List<Github> result = GithubQueryUtils.fetchGithubData(mUrl); //TODO: Remove redundant code

        return result;

    }
}
