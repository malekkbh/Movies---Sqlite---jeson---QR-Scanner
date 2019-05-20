package com.malekkbh.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String Table_Name = "Movies" ;
    private static final String COL0 = "ID" ;
    private static final String COL1 = "title" ;
    private static final String COL2 = "image" ;
    private static final String COL3 = "rating" ;
    private static final String COL4 = "releaseYear" ;
    private static final String COL5 = "genre" ;

    SharedPreferences ref = MainActivity.appContext.getSharedPreferences("v" , 0) ;
    SharedPreferences.Editor editor = ref.edit();



    public DbHelper(Context context , int v ) {
        super(context, Table_Name , null , v );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + Table_Name + " ( ID INTEGER PRIMARY KEY ," +
                COL1 + " TEXT ," +
                COL2 + " TEXT ," +
                COL3 + " TEXT ," +
                COL4 + " TEXT ," +
                COL5 + " TEXT ) " ;

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + Table_Name );
        onCreate(db);
    }

    public boolean addData (Movie movie ) {
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put( "title", movie.getTitle() );
        contentValues.put( "image", movie.getImage() );
        contentValues.put( "rating", movie.getRating() );
        contentValues.put( "releaseYear", movie.getReleaseYear() );
        contentValues.put( "genre", movie.getGenre().toString() );

        int version = db.getVersion() + 1 ;
        db.setVersion( version );
        editor.putInt("version" , version).commit() ;
        long result = db.insert(Table_Name , null , contentValues) ;


        return result != -1  ;
    }




    public Cursor getData () {
        SQLiteDatabase db = this.getWritableDatabase() ;
        String query = "SELECT * FROM " + Table_Name ;
        Cursor data = db.rawQuery(query, null) ;
        return data ;
    }


}
