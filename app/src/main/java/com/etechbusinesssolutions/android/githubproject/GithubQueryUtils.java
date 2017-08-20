package com.etechbusinesssolutions.android.githubproject;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.etechbusinesssolutions.android.githubproject.GithubActivity.LOG_TAG;

/**
 * Created by george on 8/20/17.
 */

public class GithubQueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link GithubQueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private GithubQueryUtils(){

    }

    private static List<Github> extractFeatureFromJson(String githubJSON){

        // if the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(githubJSON)){
            return null;
        }

        // Create empty ArrayList that we can start adding github user
        // to
        List<Github> githubUsers = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try{

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(githubJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of users
            JSONArray githubArray = baseJsonResponse.getJSONArray("items");

            // For each github user in the githubArray, create an {@link Github} object
            for (int i = 0; i < githubArray.length(); i++){

                // Get a single user at the position i within the list of earthquakes
                JSONObject currentGithubUser = githubArray.getJSONObject(i);

                // For a given github user, extract the JSONObject associated with the
                // key called "items", which represents a list of all user's information
                // for that user
                String username = currentGithubUser.getString("login");
                String imageUrl = currentGithubUser.getString("avatar_url");
                String profileUrl = currentGithubUser.getString("html_url");


                // Create a new {@link Github} object with the user login name, image
                // and profile url
                Github githubuser = new Github(username, imageUrl); //TODO: add profile url

                // Add the new {@link Github} to the list of githubusers.
                githubUsers.add(githubuser);



            }
        } catch (JSONException e){

            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("GithubQueryUtils", "Problem parsing the earthquake JSON results", e);

        }


        // Return the list of earthquakes
        return githubUsers;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";

        // If the url is null, then return early.
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }

         return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static List<Github> fetchGithubData(String requestUrl){

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i(LOG_TAG, "TEST: fetchEarthQuakeData() called ...");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i(LOG_TAG, "TEST: Url used to request data " + url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Github}
        List<Github> github = extractFeatureFromJson(jsonResponse); // TODO: Remove redundant code

        // Return the list of {@link Github}
        return github;
    }


}
