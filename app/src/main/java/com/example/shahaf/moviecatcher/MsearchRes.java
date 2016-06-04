package com.example.shahaf.moviecatcher;

/**
 * Created by Shahaf on 03/05/2016.
 */
public class MsearchRes {

    public String Title;
    public String Year;
    public String PosterUrl;
    public String ImdbId;

    public MsearchRes(String title, String year, String posterUrl, String imdbId)
    {
        Title = title;
        Year = year;
        PosterUrl = posterUrl;
        ImdbId = imdbId;
    }

}
