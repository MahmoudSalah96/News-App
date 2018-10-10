package com.example.wolverine.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private TextView mEmptyStateTextView;
    private static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search";
    private static final int News_LOADER_ID = 1;
    private NewsAdapter Adapter;
    private static final String API_KEY = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView NewsList = (ListView)findViewById(R.id.List);

        Adapter = new NewsAdapter(this,new ArrayList<News>());
        NewsList.setAdapter(Adapter);

        NewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = Adapter.getItem(position);

                Uri NewsUri = Uri.parse(currentNews.getmUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);

                startActivity(websiteIntent);
            }
        });

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        NewsList.setEmptyView(mEmptyStateTextView);

        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(News_LOADER_ID,null,this);
        }else{
            View loadingView = findViewById(R.id.loading_spinner);
            loadingView.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String pillar = sharedPrefs.getString(
                getString(R.string.settings_key),
                getString(R.string.pillar_default_value));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("show-tags","contributor");
        uriBuilder.appendQueryParameter("q",pillar );
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newses) {
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_news);
        Adapter.clear();

        if (newses != null && !newses.isEmpty()) {
            Adapter.addAll(newses);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
