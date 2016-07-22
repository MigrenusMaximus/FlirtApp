package com.example.migrenus.flirtapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Migrenus on 7/20/2016.
 *
 * TODO: Add description
 */
public class Place implements Serializable {
    private String mName;
    // TODO: Make type enum
    private String mType;
//    private Location mLocation;
    private double mLatitude;
    private double mLongitude;
    private List<User> mActiveUsers;
    private String mPlacesApiId;

    public Place(String name, String type, double latitude, double longitude, String placesApiId) {
        mActiveUsers = new ArrayList<>();

        mName = name;
        mType = type;
//        mLocation = location;
        mLatitude = latitude;
        mLongitude = longitude;
//        mActiveUsers = activeUsers;
        mPlacesApiId = placesApiId;
    }

    public Place() {
        mActiveUsers = new ArrayList<>();

        mName = "NullName";
        mType = "NullType";
    }

    public void retrieveUsersForLocation() {
        // TODO: Implement location-based user retrieval
        // should ask the server for a list of active users
        // at this location
        for(int i = 0; i < 16; i++) {
            mActiveUsers.add(new User("meh", (i % 2 == 0) ? Gender.Male : Gender.Female ));
        }
    }

    public List<User> filterUsers(Gender gender) {
        List<User> newList = new ArrayList<>();

        for (User user : mActiveUsers) {
            if (user.getGender() == gender)
                newList.add(user);
        }

        return newList;
    }

    public List<User> filterUsers(int lastLogin) {
        List<User> newList = new ArrayList<>();

        for (User user : mActiveUsers) {
            if (true)//user.getLastLogin() > lastLogin)
                newList.add(user);
        }

        return newList;
    }

    // getters and setters
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public List<User> getActiveUsers() {
        return mActiveUsers;
    }

    public void setActiveUsers(List<User> activeUsers) {
        this.mActiveUsers = activeUsers;
    }

    public String getPlacesApiId() {
        return mPlacesApiId;
    }

    public void setPlacesApiId(String placesApiId) {
        this.mPlacesApiId = placesApiId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }
}
