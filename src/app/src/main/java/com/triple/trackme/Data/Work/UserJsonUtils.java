package com.triple.trackme.Data.Work;

import com.triple.trackme.Data.Storage.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;

public final class UserJsonUtils {

    final static private String USER_JSON_FILE_NAME = "UserData";

    private UserJsonUtils() { }

    public static boolean isUserFileInitialize() {
        return TextFilesUtils.isFileExists(USER_JSON_FILE_NAME);
    }

    public static void deleteUserFile() {
        TextFilesUtils.deleteFile(USER_JSON_FILE_NAME);
    }

    public static void writeUserToJsonFile(final User user) throws WorkWithDataException {
        JSONObject userJson = new JSONObject();

        try {
            userJson.put("email", user.email);
            userJson.put("firstName", user.firstName);
            userJson.put("secondName", user.secondName);
            userJson.put("photoFilePath", user.photoFilePath);
            userJson.put("countTrack", user.countTrack);

            JSONArray trackFilePathsJson = new JSONArray();
            for (String path : user.trackFilePaths) {
                trackFilePathsJson.put(path);
            }
            userJson.put("trackFilePaths", trackFilePathsJson);

            String jsonString = userJson.toString();
            TextFilesUtils.writeTextToFile(USER_JSON_FILE_NAME, jsonString);
        }
        catch (IOException | JSONException exception) {
            exception.printStackTrace();
            throw (WorkWithDataException)(new WorkWithDataException().initCause(exception));
        }
    }

    public static User readUserFromJsonFile() throws WorkWithDataException {
        try {
            String jsonString = TextFilesUtils.readTextFromFile(USER_JSON_FILE_NAME);
            JSONObject userJson = new JSONObject(jsonString);

            String email = userJson.getString("email");
            String firstName = userJson.getString("firstName");
            String secondName = userJson.getString("secondName");
            String photoFilePath = userJson.getString("photoFilePath");
            long countTrack = userJson.getLong("countTrack");
            ArrayList<String> trackFilePaths = new ArrayList<String>();

            JSONArray trackFilePathsJson = userJson.getJSONArray("trackFilePaths");
            for (int i = 0; i < trackFilePathsJson.length(); i++) {
                String trackFilePath = trackFilePathsJson.getString(i);
                trackFilePaths.add(trackFilePath);
            }

            return new User(email, firstName, secondName, photoFilePath, countTrack, trackFilePaths);
        }
        catch (IOException | JSONException exception) {
            exception.printStackTrace();
            throw (WorkWithDataException)(new WorkWithDataException().initCause(exception));
        }
    }
}
