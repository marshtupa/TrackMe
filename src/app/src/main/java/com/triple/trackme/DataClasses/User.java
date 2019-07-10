package com.triple.trackme.DataClasses;

import java.util.ArrayList;

public class User {

    final private int MAX_TRACK_FILES = 3;

    public String login = "";
    public String name = "";
    public String surname = "";
    public String photoFilePath = "";
    public ArrayList<String> trackFilePaths = new ArrayList<String>();
}
