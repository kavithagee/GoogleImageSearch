package com.example.kavitha.codepathproject2googleimagesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.kavitha.codepathproject2googleimagesearch.R;
import com.example.kavitha.codepathproject2googleimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kavitha on 9/22/15.
 */
public class ImageResultArrayAdapter extends ArrayAdapter <ImageResult> {

    private static class ViewHolder {
        public ImageView ivImage;
    }

    public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult image = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater =  LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_image_result, parent, false);
            viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.ivResult);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);
//
        viewHolder.ivImage.getLayoutParams().height = image.getTbHeight();
        viewHolder.ivImage.getLayoutParams().width = image.getTbWidth();

        viewHolder.ivImage.requestLayout();
        Picasso.with(getContext()).load(image.getThumbUrl()).into(viewHolder.ivImage);

        return convertView;
    }
}
