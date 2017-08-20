package com.etechbusinesssolutions.android.githubproject;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by george on 8/20/17.
 */

public class GithubAdapter  extends ArrayAdapter<Github> {

    private static final String LOG_TAG = GithubAdapter.class.getSimpleName();

    public GithubAdapter(Activity contect, ArrayList<Github> githubUsers){

         super(contect, 0, githubUsers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        }

        // Get the {@link Github} object located at this position in the list
        final Github currentGithubUser = getItem(position);

        // Find the login name TextView in the list_item.xml
        TextView loginNameTextView = (TextView) listItemView.findViewById(R.id.username);


        // Get the login name from the current Github object and
        // set this text on the TextView
        assert currentGithubUser != null;
        loginNameTextView.setText(currentGithubUser.getmUserName());

        // Find the image view in the list_item.xml
        ImageView userImageView = (ImageView) listItemView.findViewById(R.id.userImage);

        // Get the user image url from the current Github object and
        // set this on the ImageView using picasso library
        Picasso.with(getContext()).load(currentGithubUser.getmUserImageUrl()).into(userImageView);

        return listItemView;
    }
}
