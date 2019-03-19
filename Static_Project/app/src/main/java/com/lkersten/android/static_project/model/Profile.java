package com.lkersten.android.static_project.model;

public class Profile {

    private String username;
    private String games;
    private int platforms;
    private String bio;
    private Boolean locationEnabled;
    private double[] location;

    public Profile() { }

    public Profile(String username, String games, int platforms, String bio) {
        this.username = username;
        this.games = games;
        this.platforms = platforms;
        this.bio = bio;

    }

    public Profile(String username, String games, int platforms, String bio, Boolean locationEnabled) {
        this.username = username;
        this.games = games;
        this.platforms = platforms;
        this.bio = bio;
        this.locationEnabled = locationEnabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public int getPlatforms() {
        return platforms;
    }

    public void setPlatforms(int platforms) {
        this.platforms = platforms;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        location = location;
    }

    public Boolean isLocationEnabled() {
        return locationEnabled;
    }

    public void setLocationEnabled(Boolean locationEnabled) {
        locationEnabled = locationEnabled;
    }
}
