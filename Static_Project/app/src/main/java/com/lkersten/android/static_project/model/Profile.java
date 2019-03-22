package com.lkersten.android.static_project.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Profile {

    private String username;
    private List<String> games;
    private int platforms;
    private String bio;
    private Boolean locationEnabled;
    private GeoPoint location;
    private List<String> blackList;
    private String imageUrl;

    public Profile() {

    }

    public Profile(String username, List<String> games, int platforms, String bio, Boolean locationEnabled, List<String> blackList, String imageUrl) {
        this.username = username;
        this.games = games;
        this.platforms = platforms;
        this.bio = bio;
        this.locationEnabled = locationEnabled;
        this.blackList = blackList;
        this.imageUrl = imageUrl;
    }

    public String gamesAsList() {
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

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Boolean getLocationEnabled() {
        return locationEnabled;
    }

    public void setLocationEnabled(Boolean locationEnabled) {
        this.locationEnabled = locationEnabled;
    }

    public List<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<String> blackList) {
        this.blackList = blackList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
