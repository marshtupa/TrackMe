package com.triple.trackme.WorkWithData;

import com.triple.trackme.DataClasses.Position;
import com.triple.trackme.DataClasses.Track;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;

public class TrackJson {

    public static void WriteTrackToJsonFile(File filesDir, String trackJsonFileName, Track track) {
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
            TextFilesIO.WriteTextToFile(filesDir, trackJsonFileName, jsonStr);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Track ReadTrackFromJsonFile(File filesDir, String trackJsonFileName) {
        String jsonStr = TextFilesIO.ReadTextFromFile(filesDir, trackJsonFileName);
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
