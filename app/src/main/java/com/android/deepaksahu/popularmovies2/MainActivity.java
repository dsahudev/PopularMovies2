package com.android.deepaksahu.popularmovies2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.deepaksahu.popularmovies2.Adapters.PopularMovieImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private JSONObject jsonObject;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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



        PopularMovieDownloaderTask popularMovieDownloaderTask = new PopularMovieDownloaderTask(getApplicationContext(),jsonObject);
        popularMovieDownloaderTask.execute();


        gridlayout_filler();

    }

    private void gridlayout_filler() {

        gridView = (GridView) findViewById(R.id.main_screen_gridview);
        String[] abc = null;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    class PopularMovieDownloaderTask extends AsyncTask<String, Void, String> {

        private Context mContext;
        private String url;
        private String api_key;
        private TextView tv;

        public PopularMovieDownloaderTask(Context c,JSONObject jsonObject){
            url = "http://api.themoviedb.org/3/movie/popular?api_key=";
            api_key = "f2d3b9fb36380e6cb23caf1a605d8207";
//        this.tv = tv;

            mContext = c;
        }

        @Override
        protected String doInBackground(String... params) {



            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(this.url+api_key);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                int counter = 1;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                    Log.i("checker counter " + counter, line);
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return forecastJsonStr;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);

            JSONObject popularMovieJsonObj;
            JSONArray result_populatMovieJsonArray = null;
            ArrayList<String> imageUrls = new ArrayList<>();
            ArrayList<Integer> idsMovies = new ArrayList<>();
            final String imageUrlPrefix = "http://image.tmdb.org/t/p/w185";
            try {
                popularMovieJsonObj = new JSONObject(s);
                result_populatMovieJsonArray = popularMovieJsonObj.getJSONArray("results");
                int total_items = result_populatMovieJsonArray.length();
                for(int i=0;i<total_items;i++){
                    imageUrls.add(imageUrlPrefix+result_populatMovieJsonArray.getJSONObject(i).getString("poster_path"));
                    idsMovies.add(result_populatMovieJsonArray.getJSONObject(i).getInt("id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            PopularMovieImageAdapter popularMovieImageAdapter = new PopularMovieImageAdapter(getApplicationContext(),s,R.id.imageview_popularmovie);
            gridView.setAdapter(popularMovieImageAdapter);
            gridView.setOnItemClickListener(new GridViewOnClickListener(result_populatMovieJsonArray,getApplicationContext()));


        }

        class GridViewOnClickListener implements AdapterView.OnItemClickListener {
            JSONObject jsonObject;
            Context context;
            JSONArray jsonArray;
            public GridViewOnClickListener(JSONArray jsonArray,Context context){
                this.jsonArray = jsonArray;
                this.context = context;

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    jsonObject = jsonArray.getJSONObject(position);
                    Intent intent = new Intent(context,DetailMovie.class);
                    intent.putExtra("detailmovie",jsonObject.toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
