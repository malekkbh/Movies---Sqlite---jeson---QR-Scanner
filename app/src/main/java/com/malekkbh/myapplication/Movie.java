package com.malekkbh.myapplication;

import java.util.Arrays;

public class Movie {

    private String title ;
    private String image ;
    private double rating ;
    private int releaseYear ;
    private String genre ;

    public Movie(String title, String image, double rating, int releaseYear, String genre) {
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public Movie(String displayValue) {
        this.title = displayValue ;
        this.image = "";
        this.rating = 0.0;
        this.releaseYear = 0;
        this.genre = "";
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", rating=" + rating +
                ", releaseYear=" + releaseYear +
                ", genre=" + (genre) +
                '}';
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
