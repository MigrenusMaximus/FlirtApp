package com.example.migrenus.flirtapp;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Migrenus on 7/19/2016.
 *
 * All data relating to a single user should
 * be stored within an instance of this object.
 */
public class User implements Serializable {
    private String phoneNumber;
    private Gender gender;
    private Image photo;
    private String photoUri;
    private List<User> likedUsers;
    private List<User> usersLikedBy;
    private int lastLogin;

    // This constructor is mostly used for initializing
    // other users, since their likes will be known beforehand
    public User(String phoneNumber, Gender gender, String photoUri, List<User> likedUsers, List<User> usersLikedBy) {
        this.likedUsers = new ArrayList<>();
        this.usersLikedBy = new ArrayList<>();

        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.photoUri = photoUri;
        this.likedUsers = likedUsers;
        this.usersLikedBy = usersLikedBy;
        this.lastLogin = Calendar.getInstance().get(Calendar.SECOND);
    }

    // This constructor is mostly used for generating
    // local users, since likes
    public User(String phoneNumber, Gender gender) {
        this.likedUsers = new ArrayList<>();
        this.usersLikedBy = new ArrayList<>();

        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.lastLogin = Calendar.getInstance().get(Calendar.SECOND);
    }

    // Empty constructor only does list initialization
    // to stop potential null pointer exceptions
    public User() {
        this.likedUsers = new ArrayList<>();
        this.usersLikedBy = new ArrayList<>();
    }

    // Getters and setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhotoUri() {
        return (photoUri == null) ?
                ((gender == Gender.Male) ?
                        "http://home.arcor.de/pal.heredi/Smiley%20Face_kleiner.jpg" :
                        "http://images.clipartpanda.com/blue-smiley-face-png-4c9Mz7ncE.gif") :
                photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public List<User> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<User> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public List<User> getUsersLikedBy() {
        return usersLikedBy;
    }

    public void setUsersLikedBy(List<User> usersLikedBy) {
        this.usersLikedBy = usersLikedBy;
    }

    public int getLastLogin() {
        return lastLogin;
    }
}
