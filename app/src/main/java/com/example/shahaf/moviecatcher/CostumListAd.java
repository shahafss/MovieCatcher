package com.example.shahaf.moviecatcher;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shahaf on 02/05/2016.
 */
public class CostumListAd extends ArrayAdapter<MsearchRes> {

    ArrayList<MsearchRes> SearchResList;
    Context context;




    public CostumListAd(Context context, ArrayList<MsearchRes> arrayList)
    {
        super(context,R.layout.searchline,arrayList);
        this.context = context;
        this.SearchResList = arrayList;
    }


    private Activity activity;

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = inflater.inflate(R.layout.searchline,parent,false);
        final int idx = position;
        MsearchRes m = SearchResList.get(position);



        ImageView iv = (ImageView)vi.findViewById(R.id.searchLineImg);
        ((TextView)vi.findViewById(R.id.searchLineTitle)).setText(m.Title);
        ((TextView)vi.findViewById(R.id.searchLineYear)).setText(" ("+m.Year+")");
        Picasso.with(context).load(m.PosterUrl).into(iv);
        return vi;
    }
}