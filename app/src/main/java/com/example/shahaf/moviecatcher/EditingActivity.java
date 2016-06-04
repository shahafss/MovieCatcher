package com.example.shahaf.moviecatcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EditingActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 10;
    SqlHelper helper;
    Thread t;
    Context cont = this;
    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);
        helper = new SqlHelper(EditingActivity.this);

        iv = (ImageView)findViewById(R.id.posterImageView);

        Intent i = getIntent();

        ((EditText) findViewById(R.id.editTitleText)).setText(i.getStringExtra("title"));
        ((EditText) findViewById(R.id.editPlotText)).setText(i.getStringExtra("plot"));
        ((EditText) findViewById(R.id.editGenreText)).setText(i.getStringExtra("genre"));
        ((EditText) findViewById(R.id.editReleasedText)).setText(i.getStringExtra("released"));
        ((EditText) findViewById(R.id.editRuntimeText)).setText(i.getStringExtra("runtime"));
        ((EditText) findViewById(R.id.posterUrlEditText)).setText(i.getStringExtra("posterUrl"));
        ((RatingBar) findViewById(R.id.ratingBar)).setRating(i.getFloatExtra("rating", 0));
        int Seen = i.getIntExtra("seen", 0);
        if (Seen == 1)
            ((CheckBox) findViewById(R.id.editCheckBox)).setChecked(true);
        else ((CheckBox) findViewById(R.id.editCheckBox)).setChecked(false);

        final String Title = ((EditText) findViewById(R.id.editTitleText)).getText().toString();
        String PosterUrl = ((EditText) findViewById(R.id.posterUrlEditText)).getText().toString();

        ImageView posterImg = (ImageView) findViewById(R.id.posterImageView);

        File imgFile = new File(cont.getExternalFilesDir("").toString() + "/posters/" +
                ((EditText) findViewById(R.id.editTitleText)).getText().toString());

        if (imgFile.exists()) {
            Picasso.with(cont).load(imgFile).into(posterImg);

        } else if (!PosterUrl.equals("")) {
            Picasso.with(cont).load(PosterUrl).into(posterImg);

        } else {
            posterImg.setImageResource(R.drawable.posterdefault);
        }
        final String imdbid = i.getStringExtra("imdbID");


        ((Button) findViewById(R.id.editSavebtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String Title = ((EditText) findViewById(R.id.editTitleText)).getText().toString();
                String Plot = ((EditText) findViewById(R.id.editPlotText)).getText().toString();
                String Genre = ((EditText) findViewById(R.id.editGenreText)).getText().toString();
                String Released = ((EditText) findViewById(R.id.editReleasedText)).getText().toString();
                String Runtime = ((EditText) findViewById(R.id.editRuntimeText)).getText().toString();
                final String PosterUrl = ((EditText) findViewById(R.id.posterUrlEditText)).getText().toString();
                float Rating = ((RatingBar) findViewById(R.id.ratingBar)).getRating();
                boolean Seen = ((CheckBox) findViewById(R.id.editCheckBox)).isChecked();
                int check;
                if (Seen)
                    check = 1;
                else check = 0;


                Movie2 m = new Movie2(Title, Plot, Genre, Released, Runtime, PosterUrl, Rating);
                m.setSeen(check);
                DBHelper helper = new DBHelper(EditingActivity.this);
                helper.addMovie(m);


                t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            URL url = new URL(PosterUrl);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                InputStream is = connection.getInputStream();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                int line = -1;
                                while ((line = is.read()) != -1)
                                    baos.write(line);
                                baos.close();

                                byte[] byteArr = baos.toByteArray();

                                String BaseImgDirPath = cont.getCacheDir().toString() + "/posters";
                                File BaseImgDir = new File(BaseImgDirPath);
                                if (!BaseImgDir.exists())
                                    BaseImgDir.mkdir();
                                File ImgFile = new File(BaseImgDirPath, Title);
                                FileOutputStream os = new FileOutputStream(ImgFile);
                                os.write(byteArr);
                                os.close();
                                if (!ImgFile.exists())
                                    Toast.makeText(cont, "No poster found..", Toast.LENGTH_SHORT).show();

                            }


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                t.start();

                Toast.makeText(cont, "Saved!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }

        });

        ((Button) findViewById(R.id.showPosterBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlStr = ((EditText) findViewById(R.id.posterUrlEditText)).getText().toString();
                ImageView posterImg = (ImageView) findViewById(R.id.posterImageView);

                if (!urlStr.equals(""))
                    Picasso.with(cont).load(urlStr).into(posterImg);

            }
        });


        ((ImageView) findViewById(R.id.posterImageView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            }
        });




        ((Button) findViewById(R.id.editShareBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sharedText = "Hey! Check out this movie!" + "\n" + "\n" +
                        ((EditText) findViewById(R.id.editTitleText)).getText().toString() + "\n" + "\n" +
                        ((EditText) findViewById(R.id.editPlotText)).getText().toString() + "\n" + "\n" +
                        "Poster: " + ((EditText) findViewById(R.id.posterUrlEditText)).getText().toString() + "\n" + "\n" +
                        "-=Sent via MovieCatcher=-";
                Intent shareIn = new Intent();
                shareIn.setAction(Intent.ACTION_SEND);
                shareIn.putExtra(Intent.EXTRA_TEXT, sharedText);
                shareIn.setType("text/plain");
                startActivity(shareIn);
            }
        });


        ((Button) findViewById(R.id.editCancelBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cont, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        iv.setImageBitmap(bmp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(cont, MainActivity.class);
        startActivity(i);
    }


}
