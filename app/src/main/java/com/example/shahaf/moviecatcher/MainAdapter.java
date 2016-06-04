package com.example.shahaf.moviecatcher;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Shahaf on 16/05/2016.
 */
public class MainAdapter extends ArrayAdapter<Movie2> {


    ArrayList<Movie2> MovieArrList;
    Context context;


    public MainAdapter(Context context, ArrayList<Movie2> arrayList) {
        super(context, R.layout.mainlistline, arrayList);
        this.context = context;
        this.MovieArrList = arrayList;
    }


    private Activity activity;

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = inflater.inflate(R.layout.mainlistline, parent, false);
        final int idx = position;

        Movie2 m = MovieArrList.get(position);
        float rating = m.getRating() ;
        if (rating > 3.25f) {
            int color = Color.parseColor("#2d13fb0c");
            ((RelativeLayout) vi.findViewById(R.id.colorLay)).setBackgroundColor(color);
        } else if (rating < 3.25f&&rating>0) {
            int color = Color.parseColor("#3dfb0c0c");
            ((RelativeLayout) vi.findViewById(R.id.colorLay)).setBackgroundColor(color);
        }
        else if (rating==0.0f||rating<0){
            int color = Color.parseColor("#00ffffff");
            ((RelativeLayout) vi.findViewById(R.id.colorLay)).setBackgroundColor(color);
        }


        ((TextView) vi.findViewById(R.id.mainListTitle)).setText(m.getTitle());
        ((TextView)vi.findViewById(R.id.mainListPlot)).setText(m.getPlot());


        String BaseImgDirPath = context.getCacheDir().toString()+"/posters";
        File imgFile = new File(BaseImgDirPath, m.getTitle());



        if (imgFile.exists())
        {
            Picasso.with(context).load(imgFile).into((ImageView) vi.findViewById(R.id.mainListImg));
        }else {
            ((ImageView) vi.findViewById(R.id.mainListImg)).setImageResource(R.drawable.posterdefault);

        }


        ((RatingBar) vi.findViewById(R.id.listVratingBar)).setRating(rating);

        int Seen = m.getSeen();
        CheckBox chb = (CheckBox)vi.findViewById(R.id.checkBox);
        //boolean bol =chb.isChecked();

        if (Seen==1)
       chb.setChecked(true);


        //rate.setRating();
        return vi;
    }




}
