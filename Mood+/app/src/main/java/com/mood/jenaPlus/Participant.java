package com.mood.jenaPlus;

import android.graphics.Color;
import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ceciliaxiang on 2017-02-25.
 */

public class Participant extends User {

    private String userName;
    private MoodList userMoodList;
    private MoodList followingMoodList;

    public Participant(String userName) {
        this.userName = userName;
    }

    public void addMoodEvent(String text, Date date, Boolean addLocation, Location location, String id,
                             String social, String photo, Color color) {
        Mood mood = new Mood();

        mood.setText(text);
        mood.setDate(date);
        mood.setLocation(location);
        mood.setId(id);
        mood.setSocial(social);
        mood.setPhoto(photo);
        mood.setColor(color);

        userMoodList.addMood(mood);
    }


}
