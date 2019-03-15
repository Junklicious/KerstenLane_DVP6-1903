package com.lkersten.android.static_project.model;

public class Profile {

    private String username;
    private String games;
    private String tabletopGames;
    private String platforms;
    private String bio;
    private double[] Location;

    public Profile() { }

    public Profile(String username, String games, String platforms, String bio) {
        this.username = username;
        this.games = games;
        this.platforms = platforms;
        this.bio = bio;
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

    public String getTabletopGames() {
        return tabletopGames;
    }

    public void setTabletopGames(String tabletopGames) {
        this.tabletopGames = tabletopGames;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public double[] getLocation() {
        return Location;
    }

    public void setLocation(double[] location) {
        Location = location;
    }
}
