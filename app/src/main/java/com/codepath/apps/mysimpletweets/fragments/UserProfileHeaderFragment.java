package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by edmundye on 2/27/15.
 */
public class UserProfileHeaderFragment extends Fragment {
    private TwitterClient client;
    public static UserProfileHeaderFragment newInstance(String screenName) {
        UserProfileHeaderFragment userFragment = new UserProfileHeaderFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_profile_header, parent, false);
        client = TwitterApplication.getRestClient();
        String screenName = getArguments().getString("screen_name");
        client.getUserInfo(screenName, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User u = User.fromJSON(response);
                populateProfileHeader(u, v);
            }
        });
        return v;
    }

    private void populateProfileHeader(User user, View v) {
        TextView tvName = (TextView) v.findViewById(R.id.tvFullName);
        TextView tvTagline = (TextView) v.findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) v.findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) v.findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + "Followers");
        tvFollowing.setText(user.getFriendsCount() + "Following");
        Picasso.with(v.getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
    }
}
