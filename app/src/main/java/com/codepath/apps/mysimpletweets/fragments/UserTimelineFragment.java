package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by edmundye on 2/26/15.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        loadMoreTweets(0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                if (currentMinId > 0) {
                    loadMoreTweets(currentMinId);
                }
            }
        });
        return v;
    }

        public static UserTimelineFragment newInstance(String screenName) {
            UserTimelineFragment userFragment = new UserTimelineFragment();
            Bundle args = new Bundle();
            args.putString("screenName", screenName);
            userFragment.setArguments(args);
            return userFragment;
        }

    private void loadMoreTweets(long minId) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                addTweetsAndSetMinID(json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }

    private void addTweetsAndSetMinID (JSONArray json) {
        ArrayList<Tweet> allTweets = Tweet.fromJSONArray(json);
        addAll(allTweets);
        currentMinId = Tweet.getMinId(allTweets);
    }
}
