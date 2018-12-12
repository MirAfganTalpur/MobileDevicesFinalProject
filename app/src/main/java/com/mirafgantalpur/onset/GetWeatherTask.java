package com.mirafgantalpur.onset;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GetWeatherTask extends AsyncTask<String, Void, String> {
    private String apiKey = "&appid=af487439612e72cb69eebfc4cef7f802";
    private String base = "api.openweathermap.org/data/2.5/weather?zip="; // {zip code},{country code}

    private Exception exception = null;
    private Context context;
    private WeatherLoaded listener;

    public GetWeatherTask (Context context) {
        this.context = context;
    }

    public void setWeatherRetrievedListener (WeatherLoaded handler) {
        this.listener = handler;
    }
    @Override
    protected String doInBackground(String... string) {
        String apiCallString = base + string[0] + apiKey;
        Uri uri = Uri.parse(apiCallString);
        StringBuilder content = new StringBuilder();
        try {
            InputStream inRaw = context.getContentResolver().openInputStream(uri);
            BufferedReader in = new BufferedReader(new InputStreamReader(inRaw));
            String line = null;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            inRaw.close();
        } catch (IOException e) {
            this.exception = e;
            return null;
        }
        return content.toString();
    }

    @Override
    protected void onPostExecute(String weather) {
        listener.showWeatherIcon(weather);
    }
}
