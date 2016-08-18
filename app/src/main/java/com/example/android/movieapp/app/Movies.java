package com.example.android.movieapp.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bunabeino on 14/08/2016.
 */
public class Movies implements Parcelable
{
    String movieName;
    String image; // drawable reference id
    String overview;
    String date;
    String rate;

    public Movies(String image, String movieName, String overview, String date, String rate)
    {
        this.movieName = movieName;
        this.image = image;
        this.overview = overview;
        this.date = date;
        this.rate = rate;
    }

    private Movies(Parcel in){
        movieName = in.readString();
        image = in.readString();
        overview = in.readString();
        date = in.readString();
        rate = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieName);
        parcel.writeString(image);
        parcel.writeString(overview);
        parcel.writeString(date);
        parcel.writeString(rate);
    }

    public final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>()
    {
        @Override
        public Movies createFromParcel(Parcel parcel)
        {
            return new Movies(parcel);
        }

        @Override
        public Movies[] newArray(int i) {
            return new Movies[i];
        }
    };
}
