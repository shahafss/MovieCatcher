package com.example.shahaf.moviecatcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.GLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context cont = this;
    int ID = 0;
    ArrayList<Movie2> movie2Arr;
    String imdbID;
    SqlHelper sqlhelper;
    MainAdapter mainAdapter;
    DBHelper DBhelper;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sqlhelper = new SqlHelper(cont);
        DBhelper = new DBHelper(cont);
        movie2Arr = new ArrayList<>();
        mainAdapter = new MainAdapter(cont, movie2Arr);
        lv = (ListView) findViewById(R.id.mainListView);

        movie2Arr = DBhelper.fillList();
        refreshList();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie2 m = movie2Arr.get(position);
                String Title = m.getTitle();
                String Plot = m.getPlot();
                String Genre = m.getGenre();
                String Released = m.getReleased();
                String Runtime = m.getRuntime();
                String PosterUrl = m.getPosterUrl();
                float Rating = m.getRating();
                int Seen = m.getSeen();

                int mID = m.getId();
                ID = mID;

                Intent i = new Intent(cont, EditingActivity.class);
                i.putExtra("title", Title).putExtra("plot", Plot).putExtra("genre", Genre).putExtra("released", Released)
                        .putExtra("runtime", Runtime).putExtra("posterUrl", PosterUrl).putExtra("rating", Rating).putExtra("seen",Seen);
                startActivity(i);
                finish();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Movie2 m = movie2Arr.get(position);
                ID = m.getId();
                registerForContextMenu(lv);


                return false;
            }
        });


        ((Button) findViewById(R.id.addBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder b = new AlertDialog.Builder(cont);
                b.setMessage("Add movie to list");
                b.setTitle("Add");
                b.setIcon(R.drawable.lorangebubble);
                b.setCancelable(true);
                b.setPositiveButton("Create new", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(cont, EditingActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                b.setNegativeButton("Search in web", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(cont, SearchingActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

                b.show();


            }
        });



    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderIcon(R.drawable.lorangebubble);
        menu.setHeaderTitle("Set");
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 1, "Share");
        menu.add(0, v.getId(), 2, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        DBHelper helper = new DBHelper(cont);
        SqlHelper mySqlHelper = new SqlHelper(cont);
        Cursor c = helper.getAllMoviesAsCursor();

        if (item.getTitle() == "Edit") {

            String sql = "SELECT * " +
                    " FROM " + DBConstants.TABLENAME +
                    " WHERE " + DBConstants.ID + "=" + ID;

            String title = "";
            String plot = "";
            String genre = "";
            String released = "";
            String runtime = "";
            String posterUrl = "";
            float rating = 0;
            int Seen = 0;

            Cursor cur = mySqlHelper.getReadableDatabase().rawQuery(sql, null);


            while (cur.moveToNext()) {
                title = cur.getString(1);
                plot = cur.getString(2);
                genre = cur.getString(3);
                released = cur.getString(4);
                runtime = cur.getString(5);
                posterUrl = cur.getString(6);
                rating = cur.getFloat(7);
                Seen = cur.getInt(8);
            }


            Intent i = new Intent(cont, EditingActivity.class);
            i.putExtra("title", title).putExtra("plot", plot).putExtra("genre", genre).putExtra("released", released)
                    .putExtra("runtime", runtime).putExtra("posterUrl", posterUrl).putExtra("rating", rating).putExtra("seen",Seen);
            startActivity(i);
            finish();

        } else if (item.getTitle() == "Share") {

            shareMovie(ID);

        } else if (item.getTitle() == "Delete") {

            DBhelper.deleteMovie(ID);
            refreshList();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();

    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder b = new AlertDialog.Builder(cont);
        b.setTitle("Exit");
        b.setMessage("Exit MovieCatcher?");
        b.setIcon(R.drawable.lorangebubble);
        b.setCancelable(true);
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        b.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


        if (id == R.id.deleteList) {


            AlertDialog.Builder b = new AlertDialog.Builder(cont);
            b.setTitle("Delete list");
            b.setMessage("Are you sure?");
            b.setIcon(R.drawable.lorangebubble);
            b.setCancelable(true);
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    DBHelper helper = new DBHelper(cont);
                    helper.deleteAll();
                    refreshList();

                }
            });
            b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            b.show();

            return true;
        } else if (id == R.id.exitApp) {

            AlertDialog.Builder b = new AlertDialog.Builder(cont);
            b.setTitle("Exit");
            b.setMessage("Exit MovieCatcher?");
            b.setIcon(R.drawable.lorangebubble);
            b.setCancelable(true);
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });
            b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            b.show();

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    private void refreshList() {
        DBHelper helper = new DBHelper(MainActivity.this);
        //refresh the arraylist
        movie2Arr = helper.fillList();
        mainAdapter = new MainAdapter(cont, movie2Arr);
        lv.setAdapter(mainAdapter);

    }


    public void shareMovie(int id) {

        SqlHelper mySqlHelper = new SqlHelper(cont);
        String sql = "SELECT * " +
                " FROM " + DBConstants.TABLENAME +
                " WHERE " + DBConstants.ID + "=" + id;

        String title = "";
        String plot = "";
        String posterUrl = "";

        Cursor cur = mySqlHelper.getReadableDatabase().rawQuery(sql, null);


        while (cur.moveToNext()) {
            title = cur.getString(1);
            plot = cur.getString(2);
            posterUrl = cur.getString(6);
        }


        String sharedText = "Hey! Check out this movie!" + "\n" + "\n" +
                title + "\n" + "\n" + plot + "\n" + "\n" +
                "Poster: " + posterUrl + "\n" + "\n" +
                "-=Sent via MovieCatcher=-";
        Intent shareIn = new Intent();
        shareIn.setAction(Intent.ACTION_SEND);
        shareIn.putExtra(Intent.EXTRA_TEXT, sharedText);
        shareIn.setType("text/plain");
        startActivity(shareIn);
    }

}
