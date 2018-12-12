package com.mirafgantalpur.onset;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LocationInfo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener
{

    private YouTubePlayerView youTubeView;
    private static final int RECOVERY_REQUEST = 1;
    Context mContext = this;
    String YOUTUBE_API_KEY;

    private String username;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        // Get username and location from locationList activity:
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        location = (Location)intent.getSerializableExtra("selectedLocation");

        // Handle Youtube Links:
        try {
            YOUTUBE_API_KEY = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("com.onSet.myYoutubeID");
        } catch (PackageManager.NameNotFoundException n) {
            n.printStackTrace();
        }
        youTubeView = findViewById(R.id.youtube_view);
        youTubeView.initialize(YOUTUBE_API_KEY, this);

        // Populate information edit texts:
        populateInfo();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            String youTubeLink = getYouTubeId("https://www.youtube.com/watch?v=4CbLXeGSDxg");
            player.cueVideo(youTubeLink);
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

    public static String getYouTubeId (String url) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if(matcher.find()){
            return matcher.group();
        } else {
            return "error";
        }
    }

    public void populateInfo() {

        TextView name = findViewById(R.id.location_title);
        name.setText(location.getName());

        EditText type = findViewById(R.id.info_typeET);
        type.setText(location.getType());

        EditText addr = findViewById(R.id.info_addrET);
        addr.setText(location.getAddress());

        EditText privPub = findViewById(R.id.info_privPubET);
        if(location.isPrivate()) {
            privPub.setText("PRIVATE");
        } else {
            privPub.setText("PUBLIC");
        }

        EditText filmPerm = findViewById(R.id.info_filmPermET);
        filmPerm.setText(location.getFilmPermissions());

        EditText feat = findViewById(R.id.info_featET);
        feat.setText(location.getFeatures());

        editDisable(type, addr, privPub, filmPerm, feat);

    }

    public void editDisable(EditText t, EditText a, EditText p, EditText fp, EditText f) {

        t.setFocusable(false);
        t.setClickable(false);

        a.setFocusable(false);
        a.setClickable(false);

        p.setFocusable(false);
        p.setClickable(false);

        fp.setFocusable(false);
        fp.setClickable(false);

        f.setFocusable(false);
        f.setClickable(false);
    }

    public void editEnabled(View view) {
        Intent intent = new Intent(this, Edit_Location.class);
        intent.putExtra("username", username);
        intent.putExtra("location", location);
        startActivity(intent);
    }

    public void infoBack(View view) {
        finish();
    }

}