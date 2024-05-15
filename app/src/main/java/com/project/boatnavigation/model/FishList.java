package com.project.boatnavigation.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FishList {
    @SerializedName("fish")
    private ArrayList<FishModel> fish;

    public ArrayList<FishModel> getFish() {
        return fish;
    }

    public void setFish(ArrayList<FishModel> pet) {
        this.fish = fish;
    }
}
