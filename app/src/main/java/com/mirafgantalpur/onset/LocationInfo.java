package com.mirafgantalpur.onset;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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


public class LocationInfo extends YouTubeBaseActivity implements
                                                YouTubePlayer.OnInitializedListener, WeatherLoaded {

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
    YouTubePlayer youTubePlayer;

    private String username;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        // get username and location from locationList activity
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        location = (Location) intent.getSerializableExtra("selectedLocation");

        // start weather async task for chosen location
        GetWeatherTask getWeatherTask = new GetWeatherTask(this);
        getWeatherTask.setWeatherRetrievedListener(this);
        getWeatherTask.execute(location.getAddress());

        // get YouTube API key
        try {
            YOUTUBE_API_KEY = mContext.getPackageManager().getApplicationInfo
                    (mContext.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString("com.onSet.myYoutubeID");
        } catch (PackageManager.NameNotFoundException n) {
            n.printStackTrace();
        }

        // use firebase helper to get YouTube links from database
        FirebaseHelper.getLocationsYoutubeLinks(username, location, this);

        backButton = findViewById(R.id.info_back_button);
        nextButton = findViewById(R.id.info_next_button);

        // populate information text views:
        populateInfo();
    }

    // if YouTube player is successfully initialized, get the links from database helper,
    // convert links to YouTubeIds, then pass them to player to cue video
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                                        boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer = player;
            youTubeLink = youTubeLinks.get(youTubeIndex);
            youTubeId = getYouTubeId(youTubeLink);
            youTubePlayer.cueVideo(youTubeId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error),
                                                                    errorReason.toString());
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

    // when button is clicked, go to next or previous YouTube video for this location
    public void updateYouTubeVideo(YouTubePlayer player, int youTubeIndex) {
        youTubeLink = youTubeLinks.get(youTubeIndex);
        youTubeId = getYouTubeId(youTubeLink);
        player.cueVideo(youTubeId);
        checkButton(youTubeIndex);
    }

    public void checkButton(int youTubeIndex) {
        if (youTubeIndex == 0) {
            backButton.setVisibility(View.GONE);
        } else if (youTubeLinks.size() > 1) {
            backButton.setVisibility(View.VISIBLE);
        }

        if (youTubeIndex < youTubeLinks.size() - 1 ) {
            nextButton.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.GONE);
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

    // get YouTube links from arraylist
    public void getYouTubeLinks(ArrayList<String> youTubeLinksList) {
        youTubeLinks = youTubeLinksList;
        if (youTubeLinks.size() >= 1) {
            youTubeView = findViewById(R.id.youtube_view);
            youTubeView.initialize(YOUTUBE_API_KEY, this);
        }
        checkButton(youTubeIndex);

    }

    // use delimiters to get the YouTubeId from both types of YouTube links
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

    // show appropriate information for this location
    public void populateInfo() {
        TextView name = findViewById(R.id.info_name_textview);
        name.setText(location.getName());

        TextView addr = findViewById(R.id.info_addr_textview);
        addr.setText(location.getAddress());

        TextView type = findViewById(R.id.info_type_textview);
        type.setText(location.getType());

        TextView privPub = findViewById(R.id.info_privacy_textview);

        if(location.isPrivate()) {
            privPub.setText("PRIVATE");
        } else {
            privPub.setText("PUBLIC");
        }

        TextView filmPerm = findViewById(R.id.info_film_perm_textview);
        filmPerm.setText(location.getFilmPermissions());

        TextView feat = findViewById(R.id.info_feat_textview);
        feat.setText(location.getFeatures());
    }

    public void infoBack(View view) {
        finish();
    }

    // depending on result from async task for finding weather at location, display appropriate
    // weather icon
    public void showWeatherIcon(String[] weather) {
        TextView temp = findViewById(R.id.weather_temperature_textview);
        ImageView imageView = findViewById(R.id.weather_image_view);
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

    public void editEnabled(View view) {
        Intent intent = new Intent(this, EditLocation.class);
        intent.putExtra("username", username);
        intent.putExtra("location", location);
        intent.putExtra("choice", getIntent().getStringExtra("choice"));
        intent.putExtra("youtubeLinks", youTubeLinks);
        startActivity(intent);
    }

    public void viewDetailMap(View view) {
        Intent intent = new Intent(this, DetailLocationMap.class);
        intent.putExtra("username", username);
        intent.putExtra("location", location);
        startActivity(intent);
    }
}