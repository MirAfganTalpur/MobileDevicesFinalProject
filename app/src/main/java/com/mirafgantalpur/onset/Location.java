package com.mirafgantalpur.onset;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.UUID;

public class Location implements Serializable {
    private String name;
    private String type;
    private String address;
    private String city;
    private String country;
    private String filmPermissions;
    private String features;
    private boolean isPrivate;         // False = is Public.
    private boolean isOnlyForMe;       // False = is for Everyone.
    private UUID uuid;
    private String authorUsername;
    private ArrayList<String> youtubeLinks;

    public Location(String name, String type, String address, String city, String country, String permissions, String features, boolean isPrivate, boolean isOnlyForMe, UUID uuid) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.city = city;
        this.country = country;
        this.filmPermissions = permissions;
        this.features = features;
        this.isPrivate = isPrivate;
        this.isOnlyForMe = isOnlyForMe;
        this.uuid = uuid;
        this.youtubeLinks = new ArrayList<>();
    }

    public Location (DataSnapshot info) {
        this.name = (String) info.child("name").getValue();
        this.type = (String) info.child("type").getValue();
        this.address = (String) info.child("address").getValue();
        this.city = (String) info.child("city").getValue();
        this.country = (String) info.child("country").getValue();
        this.filmPermissions = (String) info.child("filmPermissions").getValue();
        this.features = (String) info.child("features").getValue();
        this.isPrivate = (Boolean) info.child("private").getValue();
        this.isOnlyForMe = (Boolean) info.child("onlyForMe").getValue();
        this.uuid = UUID.fromString((String) info.child("uuid").getValue());
        this.youtubeLinks = new ArrayList<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getFilmPermissions() {
        return filmPermissions;
    }
    public void setFilmPermissions(String filmPermissions) {
        this.filmPermissions = filmPermissions;
    }

    public String getFeatures() {
        return features;
    }
    public void setFeatures(String features) {
        this.features = features;
    }

    public boolean isPrivate() {
        return isPrivate;
    }
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isOnlyForMe() {
        return isOnlyForMe;
    }
    public void setOnlyForMe(boolean onlyForMe) {
        isOnlyForMe = onlyForMe;
    }

    public void setAuthorUsername (String username) {
        this.authorUsername = username;
    }

    public String getAuthorUsername () {
        return authorUsername;
    }

    public ArrayList<String> getYoutubeLinks() {
        return youtubeLinks;
    }
    public void setYoutubeLinks(ArrayList<String> youtubeLinks) {
        this.youtubeLinks = youtubeLinks;
    }
    public void addYoutubeLink (String link) {
        youtubeLinks.add(link);
    }
}