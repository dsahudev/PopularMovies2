package com.android.deepaksahu.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailMovie extends AppCompatActivity {
    JSONObject movieDetailJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Intent intent = getIntent();
        String s = intent.getStringExtra("detailmovie");
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        try {

            movieDetailJson = new JSONObject(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tv = (TextView)findViewById(R.id.detailmovietv);
        tv.setText(movieDetailJson.toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}
