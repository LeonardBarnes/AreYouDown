package com.areyoudown.areyoudown;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonard on 2016/08/30.
 */
public class User {

    private String sex;
    private String FirstName;
    private String LastName;
    private String age;
    private String ProfilePicture;
    private String intention;
    private String intrestedIn;
    //private String getAge;
    private String location;
    private String agerange;
    private String timecommitment;
    private String uid;
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFbdetails() {
        return fbdetails;
    }

    public void setFbdetails(String fbdetails) {
        this.fbdetails = fbdetails;
    }

    private String fbdetails;



    public boolean isTimeenforced() {
        return timeenforced;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTimeenforced(boolean timeenforced) {
        this.timeenforced = timeenforced;
    }

    private boolean timeenforced;

    public String getTimecommitment() {
        return timecommitment;
    }

    public void setTimecommitment(String timecommitment) {
        this.timecommitment = timecommitment;
    }

    public String getAgerange() {
        return agerange;
    }

    public void setAgerange(String agerange) {
        this.agerange = agerange;
    }

    public User() {

    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public String getIntention() {
        return intention;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public String getIntrestedIn() {
        return intrestedIn;
    }

    public void setIntrestedIn(String intrestedIn) {
        this.intrestedIn = intrestedIn;
    }

    //public String getGetAge() {
    //return getAge;
    //}

    //public void setGetAge(String getAge) {
    //this.getAge = getAge;
    //}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
