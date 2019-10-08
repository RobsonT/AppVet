package com.example.apppetshop.model;

import java.util.Date;

public class Pet {
    private int id;
    private String name;
    private String sex;
    private boolean castrated;
    private Date birthDate;
    private String type;
    private String mien;
    private double weight;
    private int idBreed;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isCastrated() {
        return castrated;
    }

    public void setCastrated(boolean castrated) {
        this.castrated = castrated;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMien() {
        return mien;
    }

    public void setMien(String mien) {
        this.mien = mien;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getIdBreed() {
        return idBreed;
    }

    public void setIdBreed(int idBreed) {
        this.idBreed = idBreed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
