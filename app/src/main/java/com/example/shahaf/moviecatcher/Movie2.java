package com.example.shahaf.moviecatcher;

/**
 * Created by Shahaf on 18/05/2016.
 */
public class Movie2 {


    private String Title;
    private String Plot;
    private String Genre;
    private String Released;
    private String Runtime;
    private String PosterUrl;
    private float Rating;
    private int Id;
    private int Seen;


    public Movie2(String title, String plot, String released, String genre, String runtime,
                  String posterUrl, float rating) {
        Title = title;
        Plot = plot;
        Released = released;
        Genre = genre;
        Runtime = runtime;
        PosterUrl = posterUrl;
        Rating = rating;
        //Id = id;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getPosterUrl() {
        return PosterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        PosterUrl = posterUrl;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public int getSeen() {
        return Seen;
    }

    public void setSeen(int seen) {
        Seen = seen;
    }
}
