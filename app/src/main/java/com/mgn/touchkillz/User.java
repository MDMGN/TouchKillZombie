package com.mgn.touchkillz;


import java.io.Serializable;

public class User implements Serializable {
String country,date,email,image,name,password,uid;
int zombies;
    public User(){}
    public User(String country, String date, String email, String image, String name, String password, int zombies, String uid) {
        this.country = country;
        this.date = date;
        this.email = email;
        this.image = image;
        this.name = name;
        this.password = password;
        this.uid = uid;
        this.zombies = zombies;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getZombies() {
        return zombies;
    }

    public void setZombies(int zombies) {
        this.zombies = zombies;
    }
}
