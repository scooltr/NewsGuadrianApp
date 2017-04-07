package com.example.scooltr.guardianmaxnewsapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {


    /**
     * URL for news data from the GUADRIAN dataset
     */
    private static final String GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search?";

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */

    private static final int GUARDIAN_LOADER_ID = 1;

    /**
     * Adapter for the list of news
     */

    private NewsAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.news_list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}

        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        //Making an empty ListView in case the information wasn't loaded yet

        TextView textView = (TextView) findViewById(R.id.no_result);
        newsListView.setEmptyView(textView);

        //check if the application does have a connection to the internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        //If application does have connection to internet make loader inflate News List

        if (isConnected) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

            getLoaderManager().initLoader(GUARDIAN_LOADER_ID, null, this);

            /**
             * If application HAVE NO connection to internet set background to Empty State View and change it's description to
             * "R.string.no_network"
             */
        } else {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            textView.setText(R.string.no_network);
        }
    }
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String keyword = sharedPrefs.getString(
                getString(R.string.settings_keyword_key),
                getString(R.string.settings_keyword_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", "33c746d9-e32b-4838-9002-fc5964a011b2");
        uriBuilder.appendQueryParameter("q", keyword);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", "40");

        Log.v("Appending Parameters", uriBuilder.toString());

        return new NewsLoader(this, uriBuilder.toString());
    }

    // Loader reset, so we can clear out our existing data.

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        mAdapter.clear();
    }
    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, final List<News> news) {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        // Clear the adapter of previous news data
        mAdapter.clear();

        if (news == null || news.isEmpty()) {
            TextView textView = (TextView) findViewById(R.id.no_result);
            textView.setText(R.string.no_result);
            return;
        }

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the ListView to update.

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
            // Set an item click listener on the ListView, which sends an intent to a web browser
            // to open a website with more information about the selected news.

            ListView newsListView = (ListView) findViewById(R.id.news_list);
            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if (!isConnected) {
                        Toast.makeText(NewsActivity.this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String url = news.get(i).getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    // Create a new intent to view the news URI
                    intent.setData(Uri.parse(url));
                    // Send the intent to launch a new activity
                    startActivity(intent);
                }
            });
        }
    }

    /*
     * making a menu code
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //What's happening when user clicks on the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}