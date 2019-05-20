package com.malekkbh.myapplication;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieInfo extends Fragment {


    ImageView img ;
    TextView title , ganer , realeasYear , tvRating;
    RatingBar ratingBar ;
    SharedPreferences ref ;

    DbHelper dbHelper ;


    public MovieInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v =  inflater.inflate(R.layout.fragment_movie_info, container, false);

            img = (ImageView) v.findViewById(R.id.img) ;
            title = (TextView) v.findViewById(R.id.tvTitle) ;
            ganer = (TextView) v.findViewById(R.id.tvGaner) ;
            tvRating = (TextView) v.findViewById(R.id.ratingtv) ;
            realeasYear = (TextView) v.findViewById(R.id.tvRealeaseYear) ;
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar) ;
            ref = MainActivity.appContext.getSharedPreferences("v" , 0) ;

            String movieTitle = ref.getString("pos" , "" ) ;


            dbHelper = new DbHelper(this.getContext() , ref.getInt("version" , 1)) ;

            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String selection = "title" + " = ?";
            String[] selectionArgs = { movieTitle };

    //        Cursor data = db.query(
    //                "Movies",   // The table to query
    //                null,             // The array of columns to return (pass null to get all)
    //                selection,              // The columns for the WHERE clause
    //                selectionArgs,          // The values for the WHERE clause
    //                null,                   // don't group the rows
    //                null,                   // don't filter by row groups
    //                null               // The sort order
    //        );

            Cursor data = db.rawQuery("SELECT title,image,rating,releaseYear,genre  FROM 'Movies' WHERE title=?  " , new String[] {movieTitle + ""} );
    //        cursor = SQLiteDatabaseInstance_.rawQuery("SELECT EmployeeName FROM Employee WHERE EmpNo=?", new String[] {empNo + ""});

            Movie newMovie = null;

                if ( data!= null && data.moveToNext()) {
                     newMovie = new Movie(data.getString(0),
                            data.getString(1),
                            data.getDouble(2),
                            data.getInt(3),
                             data.getString(4)
                    );

                }//if


            if(newMovie != null) {

                if (newMovie.getImage() != null)
                    Picasso.get().load(newMovie.getImage()).into(img);

                if (newMovie.getTitle() != null) {
                    title.setText(newMovie.getTitle());
                }

                if (newMovie.getGenre() != null) {
                   char[] s = newMovie.getGenre().toCharArray() ;
                   String ss = "" ;

                   for(int i =0 ; i < s.length ; i++) {

                       if (s[i] != '"') {

                           if (s[i] >= 'a' || s[i] <= 'Z')
                               ss = ss + s[i];
                       }

                   }
                    ganer.setText(ss);
                }

                tvRating.setText(String.valueOf( (float) newMovie.getRating()) );
                ratingBar.setRating((float) newMovie.getRating() / 2);

                int y = newMovie.getReleaseYear() ;

                MovieInfo.this.realeasYear.setText(String.valueOf(y));

            }


        v.setFocusableInTouchMode(true);
        v.requestFocus();
            v.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        //Log.i(tag, "onKey Back listener is working!!!");
                        // getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        getFragmentManager().beginTransaction().replace(R.id.container , new MoviesList()).commit() ;
                        return true;
                    }
                    return false;
                }
            });



            return v ;
    }

}
