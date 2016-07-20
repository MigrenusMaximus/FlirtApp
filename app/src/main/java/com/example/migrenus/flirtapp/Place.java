package com.example.migrenus.flirtapp;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Migrenus on 7/20/2016.
 *
 * TODO: Add description
 */
public class Place {
    private String name;
    // TODO: Make type enum
    private String type;
    private Location location;
    private List<User> activeUsers;

    public Place(String name, String type, Location location, List<User> activeUsers) {
        this.activeUsers = new ArrayList<>();

        this.name = name;
        this.type = type;
        this.location = location;
        this.activeUsers = activeUsers;
    }

    public Place() {
        this.activeUsers = new ArrayList<>();

        this.name = "NullName";
        this.type = "NullType";
    }

    // getters and setters
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<User> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<User> activeUsers) {
        this.activeUsers = activeUsers;
    }
}
