package com.example.map_projects.model;

import android.content.SharedPreferences;

import java.io.Serializable;

public class Demo implements Serializable {
    private String name;
    private String flatno;
    private String street;
    private String title;
    public Demo(String name, String flatno, String street,String title) {
        this.name = name;
        this.flatno=flatno;
        this.street=street;
        this.title=title;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlatno(String flatno) {
        this.flatno = flatno;
    }

    public String getFlatno() {
        return flatno;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
