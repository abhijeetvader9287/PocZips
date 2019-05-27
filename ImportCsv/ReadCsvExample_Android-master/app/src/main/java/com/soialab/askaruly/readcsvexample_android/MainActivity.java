package com.soialab.askaruly.readcsvexample_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.soialab.askaruly.readcsvexample_android.dummy.DummyContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          list=findViewById(R.id.list);
        readWeatherData();                  // If you need to read the whole file row by row
        //readWeatherDataByColumn();        // If you need to read specific column row by row
    }

    // Defining ordered collection as WeatherSample class
    private List<WeatherSample> weatherSamples = new ArrayList<>();

    private void readWeatherDataByColumn() {
        // Read the raw csv file
        InputStream is = getResources().openRawResource(R.raw.data);

        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        // Initialization
        String line = "";

        // Handling exceptions
        try {
            // If buffer is not empty
            while ((line = br.readLine()) != null) {
                // use comma as separator columns of CSV
                String[] cols = line.split(",");

                // Print in logcat
                System.out.println("Coulmn 0 = '" + cols[0] + "', Column 1 = '" + cols[1] + "', Column 2: '" + cols[2] + "'");
            }
        } catch (IOException e) {
            // Prints throwable details
            e.printStackTrace();
        }
    }

    private void readWeatherData() {
        // Read the raw csv file
        InputStream is = getResources().openRawResource(R.raw.data);

        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        // Initialization
        String line = "";

        // Initialization
        try {
            // Step over headers
            reader.readLine();

            // If buffer is not empty
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity","Line: " + line);
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");
                // Read the data
                WeatherSample sample = new WeatherSample();

                // Setters
                sample.setMonth(tokens[0]);
                sample.setRainfall( tokens[1] );
                sample.setSumHours( tokens[2] );

                // Adding object to a class
                weatherSamples.add(sample);

                // Log the object
                Log.d("My Activity", "Just created: " + sample);
            }
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(new MyItemRecyclerViewAdapter(weatherSamples ));
        } catch (IOException e) {
            // Logs error with priority level
            Log.wtf("MyActivity", "Error reading data file on line" + line, e);

            // Prints throwable details
            e.printStackTrace();
        }
    }
}
