package com.triple.trackme.CurrentUser;

import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.Data.Storage.User;
import com.triple.trackme.Data.Work.TrackJson;
import com.triple.trackme.Data.Work.UserJson;
import com.triple.trackme.Data.Work.WorkWithDataException;

import java.util.ArrayList;

public class CurrentUserData {

    private static String login;
    private static String name;
    private static String surname;
    private static String photoFilePath;
    private static ArrayList<String> trackFilePaths;

    public static void initializeUserData() {
        boolean isInitialize = UserJson.isUserFileInitialize();

        if (isInitialize) {
            initializeUserDataFromFile();
        }
        else {
            initializeUserEmptyData();
        }
    }

    private static void initializeUserDataFromFile() {
        try {
            User user = UserJson.readUserFromJsonFile();
            login = user.login;
            name = user.name;
            surname = user.surname;
            photoFilePath = user.photoFilePath;
            trackFilePaths = user.trackFilePaths;
        }
        catch (WorkWithDataException exception) {
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

    public static ArrayList<Track> getTrackDataAll() {
        ArrayList<Track> trackData = new ArrayList<Track>();
        for (int i = trackFilePaths.size() - 1; i >= 0; i--) {
            try {
                Track track = TrackJson.readTrackFromJsonFile(trackFilePaths.get(i));
                trackData.add(track);
            }
            catch (Exception exception) {}
        }

        return trackData;
    }

    public static void addTrack(final Track track) {
        String fileName = getNextName();
        trackFilePaths.add(fileName);
        try {
            TrackJson.writeTrackToJsonFile(fileName, track);
            saveUserData();
        }
        catch (WorkWithDataException exception) {
            trackFilePaths.remove(trackFilePaths.size() - 1);
        }
    }

    private static String getNextName() {
        final String FILE_NAME_TEMPLATE = "track_";
        final String DIVIDER = "_";

        int nextFileNumber;
        if (trackFilePaths.isEmpty()) {
            nextFileNumber = 1;
        }
        else {
            String lastName = trackFilePaths.get(trackFilePaths.size() - 1);
            nextFileNumber = Integer
                    .parseInt(lastName.substring(lastName.indexOf(DIVIDER) + 1)) + 1;
        }

        return FILE_NAME_TEMPLATE + nextFileNumber;
    }

    private static void saveUserData() throws WorkWithDataException {
        User user = new User();
        user.login = login;
        user.name = name;
        user.surname = surname;
        user.photoFilePath = photoFilePath;
        user.trackFilePaths = trackFilePaths;
        UserJson.writeUserToJsonFile(user);
    }
}
