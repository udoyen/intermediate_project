package com.etechbusinesssolutions.android.githubproject;

/**
 * Created by george on 8/20/17.
 */

public class Github {

    private String mUserName;
    // user image url
    private String mUserImageUrl;
    // user profile url
    private String mUserProfileUrl; //TODO: add code for user profile page


    /**
     *
     * @param vUserName string username
     * @param vUserImageUrl string image url
     */
    public Github(String vUserName, String vUserImageUrl){

        mUserName = vUserName;
        mUserImageUrl = vUserImageUrl;
    }

    /**
     *
     * @return username of github user
     */
    public String getmUserName() {
        return mUserName;
    }

    /**
     *
     * @return user image url of github user
     */
    public String getmUserImageUrl() {
        return mUserImageUrl;
    }
}
