package com.example.kavitha.codepathproject2googleimagesearch.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.example.kavitha.codepathproject2googleimagesearch.R;
import com.example.kavitha.codepathproject2googleimagesearch.adapters.ImageResultArrayAdapter;
import com.example.kavitha.codepathproject2googleimagesearch.fragments.SettingsDialog;
import com.example.kavitha.codepathproject2googleimagesearch.models.ImageResult;
import com.example.kavitha.codepathproject2googleimagesearch.models.SettingsData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoogleImageSearchActivity extends AppCompatActivity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, SettingsDialog.Listener {

    StaggeredGridView gvResults;
    ArrayList<ImageResult> imageResults;
    ImageResultArrayAdapter imageAdapter;
    String searchQuery;
    float logicalDensity;
    private int visibleThreshold = 3;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;
    private int itemCount = 0;
    private int itemsPerPage = 8;
    private SettingsData settingSData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_image_search);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        if (settingSData == null) {
            settingSData = new SettingsData("", "", "", "");
        }
        imageResults = new ArrayList<ImageResult>();
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);

        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        gvResults.setAdapter(imageAdapter);
        gvResults.setOnScrollListener(this);


//        get conversion value
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        logicalDensity = metrics.density;

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                ImageResult image = imageResults.get(position);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_google_image_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!isNetworkAvailable()){
                    Toast.makeText(GoogleImageSearchActivity.this, "No Network, Try Later", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (searchQuery != null && searchQuery.equalsIgnoreCase(query) && loading) {
                    return true;
                }
                searchQuery = query;
                resetScreen();
                loading = true;
                fetchImages(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem settingMenu = menu.findItem(R.id.action_settings);
        settingMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentManager fm = GoogleImageSearchActivity.this.getFragmentManager();
                SettingsDialog settingsDialog = new SettingsDialog();
                Bundle args = new Bundle();
                args.putParcelable("settingsData", settingSData);
                settingsDialog.setArguments(args);
                settingsDialog.setListener(GoogleImageSearchActivity.this);
                settingsDialog.show(fm, "settings_dialog");
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void resetScreen() {
        imageAdapter.clear();
        this.currentPage = 0;
        this.previousTotalItemCount = 0;
        this.loading = true;
        this.startingPageIndex = 0;
        this.itemCount = 0;
        gvResults.setOnScrollListener(this);
    }

    public boolean fetchImages(int totalItems) {

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Accept-Encoding", "identity");
        if(totalItems >= 64) {
            gvResults.setOnScrollListener(null);
            return false;
        }
        itemCount = totalItems;

        ArrayList<String> extraFilters = new ArrayList<String>();
        if (settingSData.getImageSize() != "") {
            extraFilters.add("&imgsz=" + settingSData.getImageSize());
        }
        if (settingSData.getImageType() != "") {
            extraFilters.add("&imgtype=" + settingSData.getImageType());
        }
        if (settingSData.getImageColor() != "") {
            extraFilters.add("&imgc=" + settingSData.getImageColor());
        }
        if (settingSData.getSearchWebsite() != "") {
            extraFilters.add("&as_sitesearch=" + settingSData.getSearchWebsite());
        }

        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz="+ itemsPerPage +"&q="+ searchQuery + "&start=" + itemCount;

        for (int i = 0; i < extraFilters.size(); i++) {
            url += extraFilters.get(i);
        }

        httpClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
//                    ERROR CHECKING FOR JSON DATA
                    if (!response.isNull("responseData")) {
                        JSONArray imageJSONResults = response.getJSONObject("responseData").getJSONArray("results");
                        imageAdapter.addAll(ImageResult.fromJSONArray(imageJSONResults, logicalDensity));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onLoadMore(int totalItemCount) {
        fetchImages(totalItemCount);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
            onLoadMore(totalItemCount);
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        ;
    }

    @Override
    public void returnData(SettingsData updatedSettings) {
        settingSData = updatedSettings;
        imageAdapter.clear();
        resetScreen();
        if (searchQuery != null && searchQuery.length() > 0) {
            fetchImages(0);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
