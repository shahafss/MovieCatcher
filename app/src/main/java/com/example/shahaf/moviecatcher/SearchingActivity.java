package com.example.shahaf.moviecatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchingActivity extends AppCompatActivity {


    Context cont = this;
    Thread t;
    ArrayList<MsearchRes> searchResult = new ArrayList<>();
    ProgressBar spinner;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);



            CostumListAd searchAdapter = new CostumListAd(cont, searchResult);
            searchAdapter.clear();
            searchAdapter.notifyDataSetChanged();
            Bundle b = msg.getData();
            String jsonStr = b.getString("json");
            searchAdapter.notifyDataSetChanged();
            spinner=(ProgressBar)findViewById(R.id.progBarSpinner);
            spinner.setVisibility(View.GONE);
            try {
                JSONObject json = new JSONObject(jsonStr);
                JSONArray jArr = json.getJSONArray("Search");
                for (int i = 0; i < jArr.length(); i++) {
                    String Mtitle = jArr.getJSONObject(i).getString("Title");
                    String Myear = jArr.getJSONObject(i).getString("Year");
                    String MposterUrl = jArr.getJSONObject(i).getString("Poster");
                    String ImdbId = jArr.getJSONObject(i).getString("imdbID");
                    MsearchRes Msearch = new MsearchRes(Mtitle, Myear, MposterUrl, ImdbId);
                    searchResult.add(Msearch);
                    searchAdapter.notifyDataSetChanged();
                }

                ListView lv = (ListView) findViewById(R.id.searchListView);
                lv.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                if (searchResult.size() == 0)
                    Toast.makeText(cont, "No result were found", Toast.LENGTH_LONG).show();


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        spinner.setVisibility(View.VISIBLE);

                        //final String movieName = searchResult.get(position).Title.replace(" ", "+").replace(".", "");
                        final String searchImdbId = searchResult.get(position).ImdbId;
                        t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder urlSB = new StringBuilder();
                                urlSB.append("http://www.omdbapi.com/?i=");
                                urlSB.append(searchImdbId);
                                urlSB.append("&y=&plot=full&r=json");
                                String searchUrl = urlSB.toString();

                                try {
                                    URL url = new URL(searchUrl);
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                        StringBuilder SB = new StringBuilder();
                                        InputStream is = connection.getInputStream();
                                        InputStreamReader isr = new InputStreamReader(is);
                                        BufferedReader br = new BufferedReader(isr);

                                        String line = br.readLine();
                                        while (line != null) {
                                            SB.append(line);
                                            line = br.readLine();
                                        }
                                        br.close();
                                        isr.close();
                                        is.close();

                                        String jsonStr = SB.toString();
                                        JSONObject json = new JSONObject(jsonStr);
                                        String mTitle = json.getString("Title");
                                        String mPlot = json.getString("Plot");
                                        String mPoster = json.getString("Poster");
                                        String mImdbid = json.getString("imdbID");
                                        float mRating = Float.parseFloat(json.getString("imdbRating"))/2;
                                        String mRlease = json.getString("Released");
                                        String mGenre = json.getString("Genre");
                                        String mRuntime = json.getString("Runtime");


                                        Intent i = new Intent(cont, EditingActivity.class);
                                        i.putExtra("title", mTitle).putExtra("plot", mPlot).putExtra("posterUrl", mPoster).putExtra("imdbID", mImdbid);
                                        i.putExtra("rating", mRating).putExtra("released", mRlease).putExtra("genre", mGenre).putExtra("runtime",mRuntime);
                                        startActivity(i);
                                        finish();



                                    }

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        t.start();

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);


        final ProgressBar spinner = (ProgressBar)findViewById(R.id.progBarSpinner);
        spinner.setVisibility(View.GONE);

        ((Button) findViewById(R.id.searchBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);

                final EditText searchTxt = (EditText) findViewById(R.id.SearchEditText);


                t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        StringBuilder urlSb = new StringBuilder();
                        urlSb.append("http://www.omdbapi.com/?s=");
                        urlSb.append(searchTxt.getText().toString().trim().replace(" ", "+").replace(".", ""));
                        urlSb.append("&y=&plot=full&r=json");
                        String searchUrl = urlSb.toString();

                        try {
                            URL url = new URL(searchUrl);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                StringBuilder SB = new StringBuilder();
                                InputStream is = con.getInputStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader br = new BufferedReader(isr);

                                String line = br.readLine();
                                while (line != null) {
                                    SB.append(line);
                                    line = br.readLine();
                                }
                                br.close();
                                isr.close();
                                is.close();

                                String jsonStr = SB.toString();
                                Bundle b = new Bundle();
                                Message msg = new Message();
                                b.putString("json", jsonStr);
                                msg.setData(b);
                                handler.sendMessage(msg);


                            } else
                                Toast.makeText(getBaseContext(), "Connection Error", Toast.LENGTH_LONG).show();

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();

                        }

                    }
                });
                t.start();


            }
        });


        ((Button) findViewById(R.id.cancelSearchBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cont, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        spinner=(ProgressBar)findViewById(R.id.progBarSpinner);
        spinner.setVisibility(View.GONE);

        final EditText searchTxt = (EditText) findViewById(R.id.SearchEditText);
        if (!searchTxt.getText().toString().equals(""))
        {
            spinner.setVisibility(View.VISIBLE);

            t = new Thread(new Runnable() {
                @Override
                public void run() {

                    StringBuilder urlSb = new StringBuilder();
                    urlSb.append("http://www.omdbapi.com/?s=");
                    urlSb.append(searchTxt.getText().toString().trim().replace(" ", "+").replace(".", ""));
                    urlSb.append("&y=&plot=full&r=json");
                    String searchUrl = urlSb.toString();

                    try {
                        URL url = new URL(searchUrl);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            StringBuilder SB = new StringBuilder();
                            InputStream is = con.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);

                            String line = br.readLine();
                            while (line != null) {
                                SB.append(line);
                                line = br.readLine();
                            }
                            br.close();
                            isr.close();
                            is.close();

                            String jsonStr = SB.toString();
                            Bundle b = new Bundle();
                            Message msg = new Message();
                            b.putString("json", jsonStr);
                            msg.setData(b);
                            handler.sendMessage(msg);


                        } else
                            Toast.makeText(getBaseContext(), "Connection Error", Toast.LENGTH_LONG).show();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                }
            });
            t.start();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(cont,MainActivity.class);
        startActivity(i);
    }
}
