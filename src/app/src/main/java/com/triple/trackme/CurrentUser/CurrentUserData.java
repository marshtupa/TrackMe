package com.triple.trackme.CurrentUser;

import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.Data.Storage.User;
import com.triple.trackme.Data.Work.UserJson;

import java.util.ArrayList;
import java.io.File;

public class CurrentUserData {

    final private static int MAX_TRACK_FILES = 3;

    private static boolean isInitialize;
    private static String login;
    private static String name;
    private static String surname;
    private static String photoFilePath;
    private static ArrayList<String> trackFilePaths;

    public static void initializeUserData(File filesDir) {
        boolean isUserFileInitialize = UserJson.isUserFileInitialize(filesDir);
        isInitialize = isUserFileInitialize;

        if (isInitialize) {
            initializeUserDataFromFile(filesDir);
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

    private static void initializeUserDataFromFile(File filesDir) {
        User user = UserJson.readUserFromJsonFile(filesDir);
        login = user.login;
        name = user.name;
        surname = user.surname;
        photoFilePath = user.photoFilePath;
        trackFilePaths = user.trackFilePaths;
    }

    public static void saveUserData(File fileDir) {
        User user = new User();
        user.login = login;
        user.name = name;
        user.surname = surname;
        user.photoFilePath = photoFilePath;
        user.trackFilePaths = trackFilePaths;
        UserJson.writeUserToJsonFile(fileDir, user);
    }
}
