package com.triple.trackme.Data.Work;

import com.triple.trackme.Data.Storage.Position;
import com.triple.trackme.Data.Storage.Track;
import com.triple.trackme.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;

public class TrackJson {

    public static void deleteTrackFile(String trackJsonFileName) {
        TextFilesIO.deleteFile(MainActivity.filesDir, trackJsonFileName);
    }

    public static void writeTrackToJsonFile(File filesDir, String trackJsonFileName, Track track) {
        JSONObject trackJson = new JSONObject();

        try {
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

            String jsonStr = trackJson.toString();
            TextFilesIO.writeTextToFile(filesDir, trackJsonFileName, jsonStr);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Track readTrackFromJsonFile(File filesDir, String trackJsonFileName) {
        String jsonStr = TextFilesIO.readTextFromFile(filesDir, trackJsonFileName);
        Track track = new Track();

        try {
            JSONObject trackJson = new JSONObject(jsonStr);

            track.distance = trackJson.getDouble("distance");
            track.time = trackJson.getInt("time");
            track.avgSpeed = trackJson.getDouble("avgSpeed");
            track.positions = new ArrayList<Position>();

            JSONArray positionsJson = trackJson.getJSONArray("positions");
            for (int i = 0; i < positionsJson.length(); i++) {
                JSONObject positionJson = positionsJson.getJSONObject(i);
                Position position = new Position();
                position.latitude = positionJson.getDouble("latitude");
                position.longitude = positionJson.getDouble("longitude");
                track.positions.add(position);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return track;
    }
}
