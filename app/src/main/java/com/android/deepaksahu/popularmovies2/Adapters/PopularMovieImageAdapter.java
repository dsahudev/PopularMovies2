package com.android.deepaksahu.popularmovies2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.deepaksahu.popularmovies2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.Inflater;


public class PopularMovieImageAdapter extends BaseAdapter {
    Context mContext;
    int total_items;
    String[] imageUrls;
    int imageViewid;
     Inflater inflater;
    JSONObject jsonObject;
    JSONArray  jsonArray;
//    String[] movieIds;
    public PopularMovieImageAdapter(Context context, String jsonString,int imageViewid) {
        mContext = context;

        this.imageViewid = imageViewid;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        total_items = jsonArray.length();

    }

    @Override
    public int getCount() {

        return total_items;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootview;
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
////            imageView = new ImageView(mContext);
////            imageView = (ImageView)
////            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
////            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
////            imageView.setPadding(8, 8, 8, 8);
//
//        } else {
//            imageView = (ImageView) convertView;
//        }
//        Picasso.with(mContext)
//                .load("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSIvEQuRBGVdiMOXTRLOSPZnOBTSetT-H9j8vi0n-NMgyo7zEWcgt_0TBEUNw")
//                .placeholder(android.support.design.R.drawable.abc_ic_clear_mtrl_alpha)
//                .into(imageView);
//
//        return imageView;
//
//  }
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootview = View.inflate(mContext,R.layout.imagelayout_popular_movie,null);

        String imageUrl = "http://image.tmdb.org/t/p/w185";
        try {
            JSONObject obj = jsonArray.getJSONObject(position);
            imageUrl = imageUrl + obj.getString("poster_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            URL imageURL = new URL(imageUrl);
            ImageView imageView = (ImageView)rootview.findViewById(R.id.imageview_popularmovie);
            Picasso.with(mContext)
                    .load(imageUrl)
                    .placeholder(android.support.design.R.drawable.abc_ic_clear_mtrl_alpha)
                    .into(imageView);

        } catch (MalformedURLException e) {
            e.printStackTrace();

        }

        return  rootview;
    }
}
