package com.triple.trackme.Data.Work;

import com.triple.trackme.Data.Storage.Position;
import com.triple.trackme.Data.Storage.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class TrackJson {

    public static boolean isTrackFileInitialize(String trackJsonFileName) {
        return TextFilesIO.isFileExists(trackJsonFileName);
    }

    public static void deleteTrackFile(String trackJsonFileName) {
        TextFilesIO.deleteFile(trackJsonFileName);
    }

    public static void writeTrackToJsonFile(String trackJsonFileName, Track track) throws WorkWithDataException {
        JSONObject trackJson = new JSONObject();

        try {
            trackJson.put("dateTime", track.dateTime);
            trackJson.put("distance", track.distance);
            trackJson.put("time", track.time);
            trackJson.put("avgSpeed", track.avgSpeed);

            JSONArray positionsJson = new JSONArray();
            for (Position position : track.positions) {
                JSONObject positionJson = new JSONObject();
                positionJson.put("longitude", position.longitude);
                positionJson.put("latitude", position.latitude);
                positionsJson.put(positionJson);
            }
            trackJson.put("positions", positionsJson);

            String jsonString = trackJson.toString();
            TextFilesIO.writeTextToFile(trackJsonFileName, jsonString);
        }
        catch (IOException | JSONException exception) {
            exception.printStackTrace();
            throw new WorkWithDataException(exception.getMessage());
        }
    }

    public static Track readTrackFromJsonFile(String trackJsonFileName) throws WorkWithDataException {
        Track track = new Track();

        try {
            String jsonString = TextFilesIO.readTextFromFile(trackJsonFileName);
            JSONObject trackJson = new JSONObject(jsonString);

            track.dateTime = trackJson.getString("dateTime");
            track.distance = trackJson.getDouble("distance");
            track.time = trackJson.getInt("time");
            track.avgSpeed = trackJson.getDouble("avgSpeed");
            track.positions = new ArrayList<Position>();

            JSONArray positionsJson = trackJson.getJSONArray("positions");
            for (int i = 0; i < positionsJson.length(); i++) {
                JSONObject positionJson = positionsJson.getJSONObject(i);
                double longitude = positionJson.getDouble("longitude");
                double latitude = positionJson.getDouble("latitude");
                Position position = new Position(longitude, latitude);
                track.positions.add(position);
            }
        }
        catch (IOException | JSONException exception) {
            exception.printStackTrace();
            throw new WorkWithDataException(exception.getMessage());
        }

        return track;
    }
}
