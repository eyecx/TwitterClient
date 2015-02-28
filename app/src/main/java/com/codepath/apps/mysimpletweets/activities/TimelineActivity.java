package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    private final int REQUEST_CODE = 42;
    private TwitterClient client;
    private TweetsPagerAdapter paTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ACEE")));
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        paTweets = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(paTweets);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
        client = TwitterApplication.getRestClient();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String tweet = data.getExtras().getString("tweet");
            client.tweet(tweet, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    paTweets.insertTweet(Tweet.fromJSON(json), 0);
                }
            });
        }
    }

    public boolean composeTweet (MenuItem item){
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
        return true;
    }

    public void onProfileView (MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = { "Home", "Mentions" };
        private HomeTimelineFragment fragmentHomeTimeline;
        private MentionsTimelineFragment fragmentMentionsTimeline;

        // Adapter gets the manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
            fragmentHomeTimeline = new HomeTimelineFragment();
            fragmentMentionsTimeline = new MentionsTimelineFragment();
        }

        // The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return fragmentHomeTimeline;
            } else if (position == 1) {
                return fragmentMentionsTimeline;
            } else {
                return null;
            }
        }

        // return tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public void insertTweet (Tweet t, int position) {
            fragmentHomeTimeline.insertTweet(t,position);
        }

        // how many fragments
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
