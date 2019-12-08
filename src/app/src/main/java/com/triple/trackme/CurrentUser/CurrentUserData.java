package com.triple.trackme.CurrentUser;

import android.util.Log;

import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.Data.Storage.User;
import com.triple.trackme.Data.Work.TrackJsonUtils;
import com.triple.trackme.Data.Work.UserJsonUtils;
import com.triple.trackme.Data.Work.WorkWithDataException;
import com.triple.trackme.Server.TrackDatabase;

import java.util.ArrayList;

public final class CurrentUserData {

    private CurrentUserData() { }
    
    private static String email;
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
            email = user.email;
            name = user.name;
            surname = user.surname;
            photoFilePath = user.photoFilePath;
            countTrack = user.countTrack;
            trackFilePaths = user.trackFilePaths;
        }
        catch (WorkWithDataException exception) {
            exception.printStackTrace();
            initializeUserEmptyData();
        }
    }

    private static void initializeUserEmptyData() {
        email = "";
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
            catch (WorkWithDataException exception) {
                exception.printStackTrace();
            }
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
            TrackDatabase.SaveTrack(track);
        }
        catch (WorkWithDataException exception) {
            exception.printStackTrace();
            trackFilePaths.remove(trackFilePaths.size() - 1);
        }
    }

    private static String getTrackFileName(final long id) {
        final String FILE_NAME_TEMPLATE = "track_";
        return FILE_NAME_TEMPLATE + id;
    }

    private static void saveUserData() throws WorkWithDataException {
        User user = new User(email, name, surname, photoFilePath, countTrack, trackFilePaths);
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
                        "Email: " + email + "\n" +
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
