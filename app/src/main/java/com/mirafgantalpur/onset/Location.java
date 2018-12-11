package com.mirafgantalpur.onset;

import java.io.Serializable;

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

    public Location(String n, String t, String a, String city, String country, String fp, String feat, boolean isP, boolean isO) {
        this.name = n;
        this.type = t;
        this.address = a;
        this.city = city;
        this.country = country;
        this.filmPermissions = fp;
        this.features = feat;
        this.isPrivate = isP;
        this.isOnlyForMe = isO;
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


}
