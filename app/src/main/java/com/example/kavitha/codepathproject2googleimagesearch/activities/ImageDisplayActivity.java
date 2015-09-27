package com.example.kavitha.codepathproject2googleimagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.kavitha.codepathproject2googleimagesearch.R;
import com.example.kavitha.codepathproject2googleimagesearch.models.ImageResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDisplayActivity extends AppCompatActivity {

    ImageView ivResult;
    ShareActionProvider shareActionProvider;
    long timestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        ImageResult imageResult = getIntent().getParcelableExtra("image");
        ivResult = (ImageView) findViewById(R.id.ivResult);
        timestamp = System.currentTimeMillis();
        setTitle(Html.fromHtml(imageResult.getTitle()));
        Picasso.with(this).load(imageResult.getFullUrl()).into(ivResult, new Callback() {
            @Override
            public void onSuccess() {
                getLocalBitmapUri(ivResult);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareActionProvider.setShareIntent(getDefaultIntent());
        return true;
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
//        Uri bmpUri = getLocalBitmapUri(ivResult);
        Uri bmpUri = getFileUrl();
        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        intent.setType("image/*");

        return intent;
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image" + timestamp + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private Uri getFileUrl() {
        File file =  new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "share_image" + timestamp + ".png");

        return Uri.fromFile(file);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
