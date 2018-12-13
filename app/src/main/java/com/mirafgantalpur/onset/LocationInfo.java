package com.mirafgantalpur.onset;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LocationInfo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, WeatherLoaded {

    private static final int RECOVERY_REQUEST = 1;
    Context mContext = this;
    String YOUTUBE_API_KEY;
    ArrayList<String> youTubeLinks = new ArrayList<>();
    String youTubeLink;
    String youTubeId;
    int youTubeIndex = 0;
    ImageButton backButton;
    ImageButton nextButton;
    YouTubePlayerView youTubeView;
    //YouTubePlayer.OnInitializedListener onInitializedListener;
    YouTubePlayer youTubePlayer;

    private String username;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        // Get username and location from locationList activity:
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        location = (Location) intent.getSerializableExtra("selectedLocation");
        GetWeatherTask getWeatherTask = new GetWeatherTask(this);
        getWeatherTask.setWeatherRetrievedListener(this);
        getWeatherTask.execute(location.getAddress());

        // Handle Youtube Links:
        try {
            YOUTUBE_API_KEY = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("com.onSet.myYoutubeID");
        } catch (PackageManager.NameNotFoundException n) {
            n.printStackTrace();
        }

        FirebaseHelper.getLocationsYoutubeLinks(username, location, this);

        backButton = findViewById(R.id.info_backButton);
        nextButton = findViewById(R.id.info_nextButton);

        // Populate information text views:
        populateInfo();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer = player;
            youTubeLink = youTubeLinks.get(youTubeIndex);
            youTubeId = getYouTubeId(youTubeLink);
            youTubePlayer.cueVideo(youTubeId);
            Log.d("test", "initialized");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    public void updateYouTubeVideo(YouTubePlayer player, int youTubeIndex) {
        youTubeLink = youTubeLinks.get(youTubeIndex);
        youTubeId = getYouTubeId(youTubeLink);
        player.cueVideo(youTubeId);
        checkButton(youTubeIndex);
    }

    public void checkButton(int youTubeIndex) {

        if (youTubeIndex == 0) {
            backButton.setEnabled(false);
        } else if (youTubeLinks.size() >1 ) {
            backButton.setEnabled(true);
        }

        if (youTubeIndex < youTubeLinks.size() -1 ) {
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
        }
    }

    public void onBackButton(View view) {
        youTubeIndex--;
        updateYouTubeVideo(youTubePlayer, youTubeIndex);
    }

    public void onNextButton(View view) {
        youTubeIndex++;
        updateYouTubeVideo(youTubePlayer, youTubeIndex);
    }

    public void getYouTubeLinks(ArrayList<String> youTubeLinksList) {
        youTubeLinks = youTubeLinksList;
        if (youTubeLinks.size() >= 1) {
            youTubeView = findViewById(R.id.youtube_view);
            youTubeView.initialize(YOUTUBE_API_KEY, this);
        }
        checkButton(youTubeIndex);

    }

    public static String getYouTubeId(String url) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }

    public void populateInfo() {

        TextView name = findViewById(R.id.info_nameTV);
        name.setText(location.getName());

        TextView addr = findViewById(R.id.info_addrTV);
        addr.setText(location.getAddress());

        TextView type = findViewById(R.id.info_typeTV);
        type.setText(location.getType());

        TextView privPub = findViewById(R.id.info_privacyTV);
        if(location.isPrivate()) {
            privPub.setText("PRIVATE");
        } else {
            privPub.setText("PUBLIC");
        }

        TextView filmPerm = findViewById(R.id.info_filmPermTV);
        filmPerm.setText(location.getFilmPermissions());

        TextView feat = findViewById(R.id.info_featTV);
        feat.setText(location.getFeatures());

    }

    public void editEnabled(View view) {
        Intent intent = new Intent(this, Edit_Location.class);
        intent.putExtra("username", username);
        intent.putExtra("location", location);
        intent.putExtra("choice", getIntent().getStringExtra("choice"));
        startActivity(intent);
    }

    public void onViewMap(View view) {
        Intent intent = new Intent(this, DetailLocationMap.class);
        intent.putExtra("username", username);
        intent.putExtra("location", location);
        startActivity(intent);

    }

    public void infoBack(View view) {
        finish();
    }

    public void showWeatherIcon(String[] weather) {
        TextView temp = findViewById(R.id.weatherTemperatureTextView);
        ImageView imageView = findViewById(R.id.weatherImageView);
        if (weather == null) {
            imageView.setImageDrawable(null);
        } else {
            // convert from kelvin to celsius
            String temperature = String.format("%.2f °C", Double.parseDouble(weather[1]) - 273.15);
            String weatherType = weather[0].toLowerCase();
            temp.setText(temperature);
            if (weatherType.contains("clear")) {
                imageView.setImageResource(R.drawable.clearsky);
            } else if (weatherType.contains("thunderstorm")) {
                imageView.setImageResource(R.drawable.thunder);
            } else if (weatherType.contains("rain")) {
                imageView.setImageResource(R.drawable.rain);
            } else if (weatherType.contains("snow")) {
                imageView.setImageResource(R.drawable.snow);
                ;
            } else if (weatherType.contains("mist") || weatherType.contains("haze")) {
                imageView.setImageResource(R.drawable.mist);
            } else if (weatherType.contains("cloud")) {
                imageView.setImageResource(R.drawable.cloudy);
            } else {
                imageView.setImageDrawable(null);
            }
        }
    }

    public void viewDetailMap(View view) {
        Intent intent = new Intent(this, DetailLocationMap.class);
        intent.putExtra("username", username);
        intent.putExtra("location", location);
        startActivity(intent);
    }
}