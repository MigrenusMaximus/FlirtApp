package com.example.migrenus.flirtapp;

import android.media.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Migrenus on 7/19/2016.
 *
 * All data relating to a single user should
 * be stored within an instance of this object.
 */
public class User {
    private String phoneNumber;
    private Gender gender;
    private Image photo;
    private String photoUri;
    private List<User> likedUsers;
    private List<User> usersLikedBy;
    private Date lastLogin;

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
        this.photo = getPhotoFromUri(this.photoUri);
    }

    // This constructor is mostly used for generating
    // local users, since likes
    public User(String phoneNumber, Gender gender) {
        this.likedUsers = new ArrayList<>();
        this.usersLikedBy = new ArrayList<>();

        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    // Empty constructor only does list initialization
    // to stop potential null pointer exceptions
    public User() {
        this.likedUsers = new ArrayList<>();
        this.usersLikedBy = new ArrayList<>();
    }

    // TODO: Implement photo retrieval
    private Image getPhotoFromUri(String photoUri) {
        return null;
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

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public String getPhotoUri() {
        return photoUri;
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
}
