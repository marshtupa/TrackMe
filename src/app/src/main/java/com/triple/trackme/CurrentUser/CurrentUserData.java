package com.triple.trackme.CurrentUser;

import android.util.Log;

import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.Data.Storage.User;
import com.triple.trackme.Data.Work.TrackJsonUtils;
import com.triple.trackme.Data.Work.UserJsonUtils;
import com.triple.trackme.Data.Work.WorkWithDataException;

import java.util.ArrayList;

public final class CurrentUserData {

    private CurrentUserData() { }

    private static User currentUser;
    private static boolean localUserInitialize;
    private static boolean databaseUserInitialize;

    public static void initializeUserData() {
        localUserInitialize = UserJsonUtils.isUserFileInitialize();

        if (localUserInitialize) {
            initializeUserDataFromFile();
        }
        else {
            initializeUserEmptyData();
        }
    }

    private static void initializeUserDataFromFile() {
        try {
            currentUser = UserJsonUtils.readUserFromJsonFile();
        }
        catch (WorkWithDataException exception) {
            exception.printStackTrace();
            initializeUserEmptyData();
        }
    }

    private static void initializeUserEmptyData() {
        currentUser = new User();
    }

    public static long getNewTrackId() {
        return currentUser.countTrack;
    }

    public static ArrayList<Track> getTrackDataAll() {
        ArrayList<Track> trackData = new ArrayList<Track>();
        for (int i = currentUser.trackFilePaths.size() - 1; i >= 0; i--) {
            try {
                Track track = TrackJsonUtils
                        .readTrackFromJsonFile(currentUser.trackFilePaths.get(i));
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
        currentUser.trackFilePaths.add(fileName);
        try {
            TrackJsonUtils.writeTrackToJsonFile(fileName, track);
            currentUser.countTrack++;
            saveUserData();
            TrackDatabase.SaveTrack(track, currentUser.uid);
        }
        catch (WorkWithDataException exception) {
            exception.printStackTrace();
            currentUser.trackFilePaths.remove(currentUser.trackFilePaths.size() - 1);
        }
    }

    private static String getTrackFileName(final long id) {
        final String FILE_NAME_TEMPLATE = "track_";
        return FILE_NAME_TEMPLATE + id;
    }

    private static void saveUserData() throws WorkWithDataException {
        UserJsonUtils.writeUserToJsonFile(currentUser);
    }

    public static void clearAllLocalData() {
        for (String trackFilePath: currentUser.trackFilePaths) {
            TrackJsonUtils.deleteTrackFile(trackFilePath);
        }
        UserJsonUtils.deleteUserFile();
    }

    public static void showInLogAllLocalData() {
        StringBuilder userTrackFilePaths = new StringBuilder();
        for (int i = 0; i < currentUser.trackFilePaths.size(); i++) {
            userTrackFilePaths.append(currentUser.trackFilePaths.get(i));
            if (i < currentUser.trackFilePaths.size() - 1) {
                userTrackFilePaths.append('\n');
            }
        }
        Log.i("LocalData",
                "CURRENT USER INFO" + "\n" +
                        "Uid: " + currentUser.uid + "\n" +
                        "Email: " + currentUser.email + "\n" +
                        "First name: " + currentUser.firstName + "\n" +
                        "Second name: " + currentUser.secondName + "\n" +
                        "Photo file path: " + currentUser.photoFilePath + "\n" +
                        "Count track: " + currentUser.countTrack + "\n" +
                        "Track file paths: " + "\n" + userTrackFilePaths.toString());

        for (String trackFilePath: currentUser.trackFilePaths) {
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
