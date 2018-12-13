package com.mirafgantalpur.onset;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class GetWeatherTask extends AsyncTask<String, Void, String[]> {
    private String baseURL = "http://api.openweathermap.org/data/2.5/weather?lat=%1$,.5f&lon=%2$,.5f&APPID=af487439612e72cb69eebfc4cef7f802";

    private Exception exception = null;
    private Context context;
    private WeatherLoaded listener;

    public GetWeatherTask(Context context) {
        this.context = context;
    }

    public void setWeatherRetrievedListener(WeatherLoaded handler) {
        this.listener = handler;
    }

    @Override
    protected String[] doInBackground(String... string) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(getURL(string[0]));
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();


        } catch (IOException e) {
            return null;
        }
        return getCurrentWeather(content.toString());
    }

    @Override
    protected void onPostExecute(String[] weather) {
        listener.showWeatherIcon(weather);
    }

    private String[] getCurrentWeather(String json) {
        try {
            JSONObject data = new JSONObject(json);
            String weatherType = data.getJSONArray("weather").getJSONObject(0).getString("main");
            String temp = data.getJSONObject("main").getString("temp");
            return new String[]{weatherType, temp};
        } catch (Exception e) {
            return null;
        }
    }

    private String getURL(String inputtedAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        try {
            address = coder.getFromLocationName(inputtedAddress, 1);
            if (address.size() == 0) {
                return "";
            }
            Address location = address.get(0);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            return String.format(baseURL, latitude, longitude);

        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
