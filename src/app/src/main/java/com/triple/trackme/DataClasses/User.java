package com.triple.trackme.DataClasses;

import java.util.ArrayList;

public class User {

    final private int MAX_TRACK_FILES = 3;

    public String login = null;
    public String name = null;
    public String surname = null;
    public String photoFileUrl = null;
    public ArrayList<String> trackFileUrls = new ArrayList<String>();
}
