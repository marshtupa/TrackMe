package com.triple.trackme.CurrentUser;

import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.Data.Storage.User;
import com.triple.trackme.Data.Work.TrackJson;
import com.triple.trackme.Data.Work.UserJson;
import com.triple.trackme.Data.Work.WorkWithDataException;

import java.util.ArrayList;

public class CurrentUserData {

    final private static int MAX_TRACK_FILES = 3;

    private static boolean isInitialize;
    private static String login;
    private static String name;
    private static String surname;
    private static String photoFilePath;
    private static ArrayList<String> trackFilePaths;

    public static void initializeUserData() {
        boolean isUserFileInitialize = UserJson.isUserFileInitialize();
        isInitialize = isUserFileInitialize;

        if (isInitialize) {
            initializeUserDataFromFile();
        }
        else {
            initializeUserEmptyData();
        }
    }

    private static void initializeUserEmptyData() {
        login = "";
        name = "";
        surname = "";
        photoFilePath = "";
        trackFilePaths = new ArrayList<String>();
    }

    private static void initializeUserDataFromFile() {
        User user = new User();
        try {
            user = UserJson.readUserFromJsonFile();
        }
        catch (WorkWithDataException exception) {

        }
        login = user.login;
        name = user.name;
        surname = user.surname;
        photoFilePath = user.photoFilePath;
        trackFilePaths = user.trackFilePaths;
    }

    public static void addTrack(Track track) {
        if (trackFilePaths.size() >= MAX_TRACK_FILES) {
            deleteTrack();
        }

        String fileName = getNextName();
        try {
            TrackJson.writeTrackToJsonFile(fileName, track);
        }
        catch (WorkWithDataException exception) {

        }
        trackFilePaths.add(fileName);

        saveUserData();
    }

    private static void deleteTrack() {
        String firstName = trackFilePaths.get(0);
        TrackJson.deleteTrackFile(firstName);
        trackFilePaths.remove(0);
    }

    private static String getNextName() {
        final String fileNameTemplate = "track_";
        final String divider = "_";

        int nextFileNumber;
        if (trackFilePaths.size() == 0) {
            nextFileNumber = 1;
        }
        else {
            String lastName = trackFilePaths.get(trackFilePaths.size() - 1);
            nextFileNumber = Integer.parseInt(lastName.substring(lastName.indexOf(divider) + 1)) + 1;
        }

        return fileNameTemplate + nextFileNumber;
    }

    public static void saveUserData() {
        User user = new User();
        user.login = login;
        user.name = name;
        user.surname = surname;
        user.photoFilePath = photoFilePath;
        user.trackFilePaths = trackFilePaths;
        try {
            UserJson.writeUserToJsonFile(user);
        }
        catch (WorkWithDataException exception) {

        }
    }
}
