package com.etechbusinesssolutions.android.githubproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GithubActivity extends AppCompatActivity {

    public  static final String LOG_TAG = GithubActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.github_activity);
    }
}
