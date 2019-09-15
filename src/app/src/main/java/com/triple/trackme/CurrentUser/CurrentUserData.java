package com.triple.trackme.CurrentUser;

import android.util.Log;

import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.Data.Storage.User;
import com.triple.trackme.Data.Work.TrackJsonUtils;
import com.triple.trackme.Data.Work.UserJsonUtils;
import com.triple.trackme.Data.Work.WorkWithDataException;

import java.util.ArrayList;

public class CurrentUserData {

    private static String login;
    private static String name;
    private static String surname;
    private static String photoFilePath;
    private static long countTrack;
    private static ArrayList<String> trackFilePaths;

    public static void initializeUserData() {
        boolean isInitialize = UserJsonUtils.isUserFileInitialize();

        if (isInitialize) {
            initializeUserDataFromFile();
        }
        else {
            initializeUserEmptyData();
        }
    }

    private static void initializeUserDataFromFile() {
        try {
            User user = UserJsonUtils.readUserFromJsonFile();
            login = user.login;
            name = user.name;
            surname = user.surname;
            photoFilePath = user.photoFilePath;
            countTrack = user.countTrack;
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
        countTrack = 0;
        trackFilePaths = new ArrayList<String>();
    }

    public static long getNewTrackId() {
        return countTrack;
    }

    public static ArrayList<Track> getTrackDataAll() {
        ArrayList<Track> trackData = new ArrayList<Track>();
        for (int i = trackFilePaths.size() - 1; i >= 0; i--) {
            try {
                Track track = TrackJsonUtils.readTrackFromJsonFile(trackFilePaths.get(i));
                trackData.add(track);
            }
            catch (Exception exception) {}
        }

        return trackData;
    }

    public static void addTrack(final Track track) {
        String fileName = getTrackFileName(track.id);
        trackFilePaths.add(fileName);
        try {
            TrackJsonUtils.writeTrackToJsonFile(fileName, track);
            countTrack++;
            saveUserData();
        }
        catch (WorkWithDataException exception) {
            trackFilePaths.remove(trackFilePaths.size() - 1);
        }
    }

    private static String getTrackFileName(final long id) {
        final String FILE_NAME_TEMPLATE = "track_";
        return FILE_NAME_TEMPLATE + id;
    }

    private static void saveUserData() throws WorkWithDataException {
        User user = new User(login, name, surname, photoFilePath, countTrack, trackFilePaths);
        UserJsonUtils.writeUserToJsonFile(user);
    }

    public static void clearAllLocalData() {
        for (String trackFilePath: trackFilePaths) {
            TrackJsonUtils.deleteTrackFile(trackFilePath);
        }
        UserJsonUtils.deleteUserFile();
    }

    public static void showInLogAllLocalData() {
        StringBuilder userTrackFilePaths = new StringBuilder();
        for (int i = 0; i < trackFilePaths.size(); i++) {
            userTrackFilePaths.append(trackFilePaths.get(i));
            if (i < trackFilePaths.size() - 1) {
                userTrackFilePaths.append('\n');
            }
        }
        Log.i("LocalData",
                "CURRENT USER INFO" + "\n" +
                        "Login: " + login + "\n" +
                        "Name: " + name + "\n" +
                        "Surname: " + surname + "\n" +
                        "Photo file path: " + photoFilePath + "\n" +
                        "Count track: " + countTrack + "\n" +
                        "Track file paths: " + "\n" + userTrackFilePaths.toString());

        for (String trackFilePath: trackFilePaths) {
            try {
                Track track = TrackJsonUtils.readTrackFromJsonFile(trackFilePath);
                Log.i("LocalData",
                        "TRACK INFO" + "\n" +
                                "Id: " + track.id + "\n" +
                                "Date: " + track.dateTime + "\n" +
                                "Distance: " + track.distance + "\n" +
                                "Time: " + track.time + "\n" +
                                "Avg speed: " + track.avgSpeed + "\n" +
                                "MapImagePath: " + track.mapImagePath);

            }
            catch (WorkWithDataException exception) {
                exception.printStackTrace();
            }
        }
    }
}
