package com.malekkbh.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RvAdopter extends RecyclerView.Adapter<RvAdopter.MovieViewHolder> {

    private Context context ;
    private LayoutInflater inflater;
    private ArrayList<Movie> movieData = new ArrayList<>() ;
    private DbHelper mdbHelper ;
    SharedPreferences ref = MainActivity.appContext.getSharedPreferences("v" , 0) ;



    //cons'
    public RvAdopter(Context context) {
        this.context = context ;
        this.inflater = LayoutInflater.from(context);
        this.mdbHelper = new DbHelper(context , ref.getInt("version" , 1)) ;

        getData() ;
    }

    private void getData() {

         SQLiteDatabase db = mdbHelper.getReadableDatabase();

        String sortOrder = " releaseYear DESC " ;



        Cursor data = db.query(
                "Movies" ,
                null ,
                null ,
                null ,
                null  ,
                null ,
                sortOrder
             );

        while ( data.moveToNext()) {
            movieData.add(new Movie(data.getString(1) ,
                                    data.getString(2) ,
                                    data.getDouble(3) ,
                                    data.getInt(4) ,
                                    data.getString(5)
                                    ));
        }

        if (movieData.size() == 0 ){
            System.out.println("no DATA !! ");
        }


    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.fragment_movie_item, viewGroup, false);
        MovieViewHolder holder = new MovieViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder h , int i) {
        Movie movie = movieData.get(i) ;

        if (movie.getImage() != null) {
            Picasso.get().load(movie.getImage()).into(h.movieImage);
        }

        if (movie.getTitle() != null) {
            h.tv.setText(movie.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return movieData.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView movieImage ;
        TextView tv ;

    public MovieViewHolder(@NonNull View v) {
        super(v);

        movieImage = (ImageView) v.findViewById(R.id.movieImage) ;
        tv = (TextView) v.findViewById(R.id.movieTitle) ;

        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int pos = this.getAdapterPosition() ;

        Movie movie = movieData.get(pos);
        System.out.println("pos:" + pos + "title: " + movie.getTitle());
        ref.edit().putString("pos" , movie.getTitle()).commit() ;

        FragmentActivity activity = (FragmentActivity) context  ;

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container , new MovieInfo()).commit() ;


    }
}


}
