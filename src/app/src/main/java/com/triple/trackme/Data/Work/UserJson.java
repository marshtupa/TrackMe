package com.triple.trackme.Data.Work;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.triple.trackme.Data.Storage.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class UserJson {

    final static private String USER_JSON_FILE_NAME = "UserData";

    public static boolean isUserFileInitialize() {
        return TextFilesIO.isFileExists(USER_JSON_FILE_NAME);
    }

    public static void deleteUserFile() {
        TextFilesIO.deleteFile(USER_JSON_FILE_NAME);
    }

    public static void writeUserToJsonFile(User user) throws WorkWithDataException {
        JSONObject userJson = new JSONObject();

        String Path = "";


        try {
            userJson.put("login", user.login);
            userJson.put("name", user.name);
            userJson.put("surname", user.surname);
            userJson.put("photoFilePath", user.photoFilePath);

            JSONArray trackFilePathsJson = new JSONArray();
            for (String path : user.trackFilePaths) {
                trackFilePathsJson.put(path);
            }
            userJson.put("trackFilePaths", trackFilePathsJson);

            String jsonString = userJson.toString();
            TextFilesIO.writeTextToFile(Path + USER_JSON_FILE_NAME, jsonString);
        }
        catch (IOException | JSONException exception) {
            exception.printStackTrace();
            throw new WorkWithDataException(exception.getMessage());
        }
    }

    public static User readUserFromJsonFile() throws WorkWithDataException {
        User user = new User();

        try {
            String jsonString = TextFilesIO.readTextFromFile(USER_JSON_FILE_NAME);
            JSONObject userJson = new JSONObject(jsonString);

            user.login = userJson.getString("login");
            user.name = userJson.getString("name");
            user.surname = userJson.getString("surname");
            user.photoFilePath = userJson.getString("photoFilePath");

            JSONArray trackFilePathsJson = userJson.getJSONArray("trackFilePaths");
            for (int i = 0; i < trackFilePathsJson.length(); i++) {
                String trackFilePath = trackFilePathsJson.getString(i);
                user.trackFilePaths.add(trackFilePath);
            }
        }
        catch (IOException | JSONException exception) {
            exception.printStackTrace();
            throw new WorkWithDataException(exception.getMessage());
        }

        return user;
    }
}
