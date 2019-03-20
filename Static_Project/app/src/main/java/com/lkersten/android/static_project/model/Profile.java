package com.lkersten.android.static_project.model;

import java.util.List;

public class Profile {

    private String username;
    private List<String> games;
    private int platforms;
    private String bio;
    private Boolean locationEnabled;
    private double[] location;
    private List<String> blackList;

    public Profile() { }

    public Profile(String username, List<String> games, int platforms, String bio) {
        this.username = username;
        this.games = games;
        this.platforms = platforms;
        this.bio = bio;
        locationEnabled = false;
    }

    public Profile(String username, List<String> games, int platforms, String bio, Boolean locationEnabled) {
        this.username = username;
        this.games = games;
        this.platforms = platforms;
        this.bio = bio;
        this.locationEnabled = locationEnabled;
    }

    public Profile(String username, List<String> games, int platforms, String bio, Boolean locationEnabled, List<String> blackList) {
        this.username = username;
        this.games = games;
        this.platforms = platforms;
        this.bio = bio;
        this.locationEnabled = locationEnabled;
        this.blackList = blackList;
    }

    public String getGamesAsList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            sb.append(games.get(i));
            if (i != games.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getGames() {
        return games;
    }

    public void setGames(List<String> games) {
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
