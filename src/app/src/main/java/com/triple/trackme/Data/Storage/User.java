package com.triple.trackme.Data.Storage;

import java.util.ArrayList;

public class User {

    public String id;
    public String email;
    public String firstName;
    public String secondName;
    public String photoFilePath;
    public long countTrack;
    public ArrayList<String> trackFilePaths;

    public User(final String id, final String email, final String firstName,
                final String secondName, final String photoFilePath, final long countTrack,
                final ArrayList<String> trackFilePaths) {

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.photoFilePath = photoFilePath;
        this.countTrack = countTrack;
        this.trackFilePaths = trackFilePaths;
    }

    public User() {
        this.id = "";
        this.email = "";
        this.firstName = "";
        this.secondName = "";
        this.photoFilePath = "";
        this.countTrack = 0;
        this.trackFilePaths = new ArrayList<String>();
    }
}
