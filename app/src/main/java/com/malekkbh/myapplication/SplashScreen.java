package com.malekkbh.myapplication;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class SplashScreen extends Fragment {

    ProgressDialog pd ;

    DbHelper dbHelper ;

    private static String url = "https://api.androidhive.info/json/movies.json" ;
    SharedPreferences ref = MainActivity.appContext.getSharedPreferences("v" , 0) ;




    public SplashScreen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_splash_screen, container, false);
//        ref.edit().putInt("version" , 16).commit() ;
        dbHelper = new DbHelper(this.getContext() , ref.getInt("version" , 1)) ;

        pd = new ProgressDialog(this.getContext()) ;

        SQLiteDatabase db = dbHelper.getReadableDatabase() ;

//        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", "Movies"});
//        if (cursor.getColumnCount() > 0)
//        {
//            cursor.close();
//            new getMovie().execute() ;
//        }else {
//            getFragmentManager().beginTransaction().replace(R.id.container, new MoviesList()  ).commit() ;
//        }
        System.out.println(db.getVersion() +"kkkkkkkkkkkk");
        if (db.getVersion() == 1 ) {
                 new getMovie().execute() ;
        }else
        {
         getFragmentManager().beginTransaction().replace(R.id.container, new MoviesList()  ).commit() ;
        }




//        db.execSQL("delete from Movies");
//        db.setVersion(1);



//        if (db == null ) {
////        new getMovie().execute() ;
//        }

        return v ;
    }


    public void addData (Movie movie) {

        boolean insertData = dbHelper.addData(movie)  ;

        if (insertData) {
            System.out.println("add to table");}
            else {
                System.out.println("some thing went wrong");
            }
        }

















    private class getMovie extends AsyncTask<Void, Void, ArrayList<String>> {

        //properties:
        View dialogView;
        //ctor:
        public getMovie() {
        }



        //Show progress:
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SplashScreen.this.getContext());
            pd.setMessage("Loading..");
            pd.setCancelable(false);
            pd.show();
        }


        //runs in the background thread.
        //Thread job -> ArrayList
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> result = new ArrayList<>();
            HttpHandler sh = new HttpHandler();

            String jsonStr =  sh.makeServiceCall(url);


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray movies = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject c = movies.getJSONObject(i);

//                        SplashScreen.this.addData("title" , (String) c.get("title"));
//                        SplashScreen.this.addData("image" , (String) c.get("image"));
//                        SplashScreen.this.addData("rating" , String.valueOf(c.get("rating")));
//                        SplashScreen.this.addData("releaseYear" , String.valueOf(c.get("releaseYear")));
//                        SplashScreen.this.addData("genre" , String.valueOf(c.get("genre")));

                        SplashScreen.this.addData( new Movie((String) c.get("title") ,
                                (String) c.get("image") ,
                                Double.valueOf(String.valueOf(c.get("rating")))  ,
                                (int) (c.get("releaseYear")) ,
                                (String) (c.get("genre").toString())
                        )) ;
//                        result.add((String) c.get("english_name"));
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return result;
        }

        //runs on the UI Thread
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pd.isShowing())
                pd.dismiss();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SplashScreen.this.getContext(),
                    android.R.layout.simple_dropdown_item_1line, result);

            getFragmentManager().beginTransaction().replace(R.id.container, new MoviesList()  ).commit() ;


            //   final View dialogView = getLayoutInflater().inflate(R.layout.fragment_add_area, null, false);



    }//classGetCity
}


//             return null;
//        }
//
//    }
//
//
//}



}





