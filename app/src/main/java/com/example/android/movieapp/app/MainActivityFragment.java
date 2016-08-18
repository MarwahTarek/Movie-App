package com.example.android.movieapp.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


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


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final String POPULARITY = "popular";
    private static final String RATING = "top_rated";
    private static final String sortKey = "sort_setting";
    private static final String moviesKey = "movies";

    private GridViewActivity gridAdapter;
    protected String savedSort = POPULARITY;
    private ArrayList<Movies> movieList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(LOG_TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        // To handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.v(LOG_TAG,"onOptionsItemSelected");

        int id = item.getItemId();

        if (id == R.id.action_sort_by_popularity)
        {
            savedSort = POPULARITY;
            Log.d(LOG_TAG,"onOptionsSelecetd() savedSort:"+savedSort);
            updateMovies(savedSort);
           return true;
        }
        if (id == R.id.action_sort_by_rating)
        {
            savedSort = RATING;
            Log.d(LOG_TAG,"onOptionsSelecetd() savedSort:"+savedSort);
            updateMovies(savedSort);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(LOG_TAG,"onSaveInstanceState");

        super.onSaveInstanceState(outState);
        if (!savedSort.contentEquals(POPULARITY))
        {
            outState.putString(sortKey, savedSort);
            Log.v(LOG_TAG,"onSaveInstanceState() outstate: "+outState.getString(sortKey));

        }
        if (movieList != null)
        {
            outState.putParcelableArrayList(moviesKey, movieList);
        }
        Log.v(LOG_TAG,"onSaveInstanceState: "+savedSort);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.v(LOG_TAG,"onCreateView()");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridAdapter = new GridViewActivity(getActivity(),
                                            R.layout.gridview_layout,
                                             new ArrayList<Movies>());

        // Get a reference to the GridView, and attach the custom adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movies movie = gridAdapter.getItem(position);
                Log.v(LOG_TAG,"ItemClick() name: "+movie.movieName);
                Log.v(LOG_TAG,"ItemClick() overview: "+movie.overview);

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("title", movie.movieName)
                        .putExtra("overview", movie.overview)
                        .putExtra("date",movie.date)
                        .putExtra("rate",movie.rate)
                        .putExtra("image",movie.image)
                        ;
                startActivity(intent);
            }
        });

    //Check for any saved states
        if (savedInstanceState != null)
        {
            Log.d(LOG_TAG,"savedInstance != null");
            Log.v(LOG_TAG,"onCreate(): "+savedInstanceState.getString(sortKey));

            if (savedInstanceState.containsKey(sortKey))
            {
                savedSort = savedInstanceState.getString(sortKey);
                Log.d(LOG_TAG,"saved instance: "+savedSort);
                updateMovies(savedSort);
            }
            if (savedInstanceState.containsKey(moviesKey))
            {
                movieList = savedInstanceState.getParcelableArrayList(moviesKey);
                Log.d(LOG_TAG,"saved instance: "+movieList);
                gridAdapter.setData(movieList);
            }
            else
            {
                updateMovies(savedSort);
            }
        }
        else
        {
            Log.d(LOG_TAG,"onCreateView() go to update movies");
            //Log.v(LOG_TAG,"onCreate(): "+savedInstanceState.getString(sortKey));
            updateMovies(savedSort);

        }
        return rootView;
    }

    /*@Override
    public void  onStart()
    {
        Log.v(LOG_TAG,"onStart");
        super.onStart();

        FetchMovie Movie = new FetchMovie();
        Movie.execute(savedSort);
        Log.v(LOG_TAG,"finish onStart");
    }*/

    private void updateMovies(String sort_by)
    {
        Log.v(LOG_TAG,"updateMovies");

        new FetchMovie().execute(sort_by);
    }

    public class FetchMovie extends AsyncTask<String, Void, Movies[]>
    {

        private final String LOG_TAG = FetchMovie.class.getSimpleName();


        private Movies[] getMovieDataFromJson(String movieJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String movieResults = "results";
            final String moviePoster = "poster_path";
            final String movieTitle = "original_title";
            final String movieOverview = "overview";
            final String movieRates = "vote_average";
            final String movieDate = "release_date";


            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(movieResults);


            String [] posters = new String[movieArray.length()];
            String [] titles = new String[movieArray.length()];
            String [] overviews = new String[movieArray.length()];
            String [] dates = new String[movieArray.length()];
            String [] rate = new String[movieArray.length()];
            Movies [] movieObject = new Movies[movieArray.length()];


            final String imgPath = "http://image.tmdb.org/t/p/w185/";

            for(int i = 0; i < movieArray.length(); i++)
            {
                String title;
                String path;
                String overview;
                String rates;
                String date;

                // Get the JSON object representing the movie
                JSONObject movieData = movieArray.getJSONObject(i);
                path = movieData.getString(moviePoster);
                title = movieData.getString(movieTitle);
                overview = movieData.getString(movieOverview);
                rates = movieData.getString(movieRates);
                date = movieData.getString(movieDate);

                posters[i] = imgPath+path;
                titles[i] = title;
                overviews[i] = overview;
                rate[i] = rates;
                dates[i] = date;

                movieObject[i] = new Movies(imgPath+path, title , overview , date , rates);
            }


                Log.v(LOG_TAG, "getMovieDataFromJson() poster: "+posters.length);
                Log.v(LOG_TAG, "getMovieDataFromJson() overview: " + overviews.length);
                Log.v(LOG_TAG, "getMovieDataFromJson() Title: " + titles.length);
                Log.v(LOG_TAG, "getMovieDataFromJson() date: " + dates.length);
                Log.v(LOG_TAG, "getMovieDataFromJson() rate: " + rate.length);


            return movieObject;

        }


        @Override
        protected Movies[] doInBackground(String... params)
        {
            String sortType = params[0];

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                // Construct the URL for the moviedb query
                //String baseUrl = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String APID = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(sortType)
                        .appendQueryParameter(APID, BuildConfig.MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to MovieDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a lot easier if you print out the completed
                // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try
            {
                return getMovieDataFromJson(movieJsonStr);

            }catch (JSONException e)
            {
                Log.e(LOG_TAG,"Error getting data from JSON");
            }
            Log.v(LOG_TAG,"doInBackground() end");
            return null;
        }

        @Override
        protected void onPostExecute(Movies[] result)
        {
            Log.d(LOG_TAG,"onPost() no.Movie: "+result.length);

            if (result != null)
            {
                gridAdapter.clear();
                if(gridAdapter.isEmpty())
                {
                    gridAdapter.addAll(result);
                    Log.d(LOG_TAG,"onPost() add all end");
                }
            }
            else
            {
                Log.d(LOG_TAG,"onPost didn't update adapter");
            }
        }
    }
}