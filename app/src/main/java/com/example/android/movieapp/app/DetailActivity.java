package com.example.android.movieapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Bunabeino on 14/08/2016.
 */
public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail, new DetailFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {


        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private String title;
        private String overview;
        private String date;
        private String rate;
        private String image;

        public DetailFragment() {

            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId()== android.R.id.home)
            {
                getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {

            View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

            // The detail Activity called via intent.  Inspect the intent for movie data.
            Intent intent = getActivity().getIntent();
            Bundle extras = intent.getExtras();
            Log.v(LOG_TAG,"onCreateView() hasExtra: "+intent.hasExtra("title") + " & "+intent.hasExtra("overview"));

            if (intent != null && extras !=null)
            {
                title = intent.getStringExtra("title");
                ((TextView) rootView.findViewById(R.id.detail_title))
                        .setText(title);

                overview = intent.getStringExtra("overview");
                ((TextView) rootView.findViewById(R.id.detail_overview))
                        .setText(overview);

                date = intent.getStringExtra("date");
                ((TextView) rootView.findViewById(R.id.detail_date))
                        .setText(date);

                rate = intent.getStringExtra("rate");
                ((TextView) rootView.findViewById(R.id.detail_vote_average))
                        .setText(rate);

                ImageView iconView = (ImageView) rootView.findViewById(R.id.detail_image);;
                image = intent.getStringExtra("image");
                Log.v(LOG_TAG,"intent get() image: "+image);

                Picasso.with(getContext())
                        .load(image)
                        .into(iconView);
                Log.v(LOG_TAG,"end if ");
            }

            return rootView;
        }


    }
}
