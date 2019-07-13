package com.triple.trackme.Data.Work;

import com.triple.trackme.Data.Storage.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.File;

public class UserJson {

    final static private String USER_JSON_FILE_NAME = "UserData";

    public static boolean isUserFileInitialize(File filesDir) {
        boolean isUserFileInitialize = TextFilesIO.fileExists(filesDir, USER_JSON_FILE_NAME);
        return isUserFileInitialize;
    }

    public static void writeUserToJsonFile(File filesDir, User user) {
        JSONObject userJson = new JSONObject();

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

            String jsonStr = userJson.toString();
            TextFilesIO.writeTextToFile(filesDir, USER_JSON_FILE_NAME, jsonStr);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static User readUserFromJsonFile(File filesDir) {
        String jsonStr = TextFilesIO.readTextFromFile(filesDir, USER_JSON_FILE_NAME);
        User user = new User();

        try {
            JSONObject userJson = new JSONObject(jsonStr);

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
        catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
