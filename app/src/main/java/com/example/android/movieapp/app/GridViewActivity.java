package com.example.android.movieapp.app;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Bunabeino on 14/08/2016.
 */
public class GridViewActivity extends ArrayAdapter<Movies>
{
    private static final String LOG_TAG = GridViewActivity.class.getSimpleName();

    private List<Movies> movieNames;

    public GridViewActivity(Activity context, int resource, List<Movies> movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter , the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies);
        movieNames = movies;
    }

    public void setData(List<Movies> data)
    {
        clear();
        for (Movies movie : data)
        {
            add(movie);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /// Gets the Movies object from the ArrayAdapter at the appropriate position
        Movies movies = getItem(position);

        ImageView iconView;
        TextView title;

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
              R.layout.gridview_layout, parent, false);
        }

        iconView = (ImageView) convertView.findViewById(R.id.gridview_image);
        title = (TextView) convertView.findViewById(R.id.gridview_title);
        title.setText(movies.movieName);
        Log.d(LOG_TAG, " getView() imagePath: " + movies);

        Picasso.with(getContext())
                .load(movies.image)
                .into(iconView);

        return convertView;

    }
}
