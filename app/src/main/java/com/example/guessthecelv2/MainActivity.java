package com.example.guessthecelv2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
ArrayList<String> celebNames = new ArrayList<>();
ArrayList<String> celebURLs = new ArrayList<>();
Button b0, b1, b2, b3;
String[] answers = new String[4];
int locationOfCorrectAnswer = -1;
int chosenCelebrity = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        b0 = findViewById(R.id.button0);
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);

        DownloadTask download = new DownloadTask();
        try {
            String result = download.execute("https://www.imdb.com/list/ls052283250/").get();
            String[] res = result.split("  <div class=\"footer filmosearch\">");
            result = res[0];
            Pattern p = Pattern.compile("img alt=\"(.*?)\"");
            Matcher m = p.matcher(result);

            int count = 0;

            while(m.find()) {
                celebNames.add(m.group(1));
            }

            p = Pattern.compile("src=\"(.*?).jpg\"");
             m = p.matcher(result);

            while(m.find()) {
                celebURLs.add(m.group(1));
            }
newQuestion();




        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void newQuestion() {
        Random rand = new Random();
        chosenCelebrity = rand.nextInt(celebNames.size());
        DownloadImageTask imageTask = new DownloadImageTask();
        try {
            Bitmap celebImage = imageTask.execute(celebURLs.get(chosenCelebrity)).get();
            imageView.setImageBitmap(celebImage);
            locationOfCorrectAnswer = rand.nextInt(answers.length);
            for (int i = 0;i<answers.length;i++) {
                if(i==locationOfCorrectAnswer) {
                    answers[i] = celebNames.get(chosenCelebrity);
                } else {
                    int incorrectAns = rand.nextInt(celebNames.size());
                    while(incorrectAns == chosenCelebrity) {
                        incorrectAns = rand.nextInt(celebNames.size());
                    }
                    answers[i] = celebNames.get(incorrectAns);
                }
            }
            b0.setText(answers[0]);
            b1.setText(answers[1]);
            b2.setText(answers[2]);
            b3.setText(answers[3]);


        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public void celebChosen(View view) {
        if(Integer.valueOf(view.getTag().toString())==locationOfCorrectAnswer) {
            Toast.makeText(getApplicationContext(),"Correct! That's awesome!",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),"Incorrect! It's " + celebNames.get(chosenCelebrity),Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                newQuestion();
            }
        },1000);

    }


    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder("");
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1) {
                    char current = (char) data;
                    result.append(current);
                    data = reader.read();
                }
                return result.toString();



            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
