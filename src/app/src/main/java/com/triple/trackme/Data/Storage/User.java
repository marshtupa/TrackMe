package com.triple.trackme.Data.Storage;

import java.util.ArrayList;

public class User {

    public String login;
    public String name;
    public String surname;
    public String photoFilePath;
    public long countTrack;
    public ArrayList<String> trackFilePaths;

    public User(final String login, final String name, final String surname,
                final String photoFilePath, final long countTrack,
                final ArrayList<String> trackFilePaths) {

        this.login = login;
        this.name = name;
        this.surname = surname;
        this.photoFilePath = photoFilePath;
        this.countTrack = countTrack;
        this.trackFilePaths = trackFilePaths;
    }
}
