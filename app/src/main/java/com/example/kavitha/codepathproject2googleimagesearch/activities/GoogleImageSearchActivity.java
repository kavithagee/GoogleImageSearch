package com.example.kavitha.codepathproject2googleimagesearch.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.example.kavitha.codepathproject2googleimagesearch.adapters.ImageResultArrayAdapter;
import com.example.kavitha.codepathproject2googleimagesearch.models.ImageResult;
import com.example.kavitha.codepathproject2googleimagesearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoogleImageSearchActivity extends AppCompatActivity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    StaggeredGridView gvResults;
    ArrayList<ImageResult> imageResults;
    ImageResultArrayAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_image_search);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        imageResults = new ArrayList<ImageResult>();
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);

        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        gvResults.setAdapter(imageAdapter);
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
                // perform query here
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                AsyncHttpClient httpClient = new AsyncHttpClient();
                httpClient.addHeader("Accept-Encoding", "identity");
                httpClient.get("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q="+ query, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            JSONArray imageJSONResults = response.getJSONObject("responseData").getJSONArray("results");
                            imageAdapter.clear();
                            imageAdapter.addAll(ImageResult.fromJSONArray(imageJSONResults));

                        }
                        catch(JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.v("KAVITHA","FAILURE " + responseString);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Log.v("KAVITHA ","search clicked!!! " + item.toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onLoadMoreItems() {
        ;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ;
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        ;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        ;
    }
}
