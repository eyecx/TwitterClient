package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edmundye on 2/24/15.
 */
public abstract class TweetsListFragment extends Fragment {
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    protected ListView lvTweets;
    protected long currentMinId;
    protected TwitterClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
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

    public void insertTweet(Tweet t, int position) {
        aTweets.insert(t, position);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        client = TwitterApplication.getRestClient();
        loadMoreTweets(0);
    }

    protected abstract void loadMoreTweets(long minId);


    protected void addTweetsAndSetMinID (JSONArray json) {
        ArrayList<Tweet> allTweets = Tweet.fromJSONArray(json);
        addAll(allTweets);
        currentMinId = Tweet.getMinId(allTweets);
    }

    public void addAll(List<Tweet>tweets) {
        aTweets.addAll(tweets);
    }
}
