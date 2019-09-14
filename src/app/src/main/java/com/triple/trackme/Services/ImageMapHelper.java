package com.triple.trackme.Services;

import com.triple.trackme.Data.Storage.Position;
import com.triple.trackme.Data.Storage.Track;

public class ImageMapHelper {

    private static final int DEFAULT_IMAGE_MAP_SIZE = 512;
    private static final int DEFAULT_MAP_ZOOM = 15;
    private static final int DEFAULT_MAP_LATITUDE = 0;
    private static final int DEFAULT_MAP_LONGITUDE = 0;

    private static StringBuilder url;
    private static String apiKey;
    private static Track trackData;

    public static String getImageUrl(final String apiKey, final Track trackData) {
        ImageMapHelper.apiKey = apiKey;
        ImageMapHelper.trackData = trackData;

        ImageMapHelper.url = new StringBuilder();
        initializeUrl();
        setCenter();
        setZoom();
        setSize();
        setPath();
        setApiKey();
        return url.toString();
    }

    private static void initializeUrl() {
        url.append("https://maps.googleapis.com/maps/api/staticmap?");
    }

    private static void setCenter() {
        Position centerPosition = null;
        if (trackData.positions.size() == 1) {
            centerPosition = trackData.positions.get(0);
        }
        else if (trackData.positions.size() > 1) {
            centerPosition = trackData.positions.get(trackData.positions.size() / 2);
        }

        if (centerPosition == null) {
            url.append("center=" + DEFAULT_MAP_LATITUDE + "," + DEFAULT_MAP_LONGITUDE);
        }
        else {
            url.append("center=" + centerPosition.latitude + "," + centerPosition.longitude);
        }
    }

    private static void setZoom() {
        url.append("&zoom=" + DEFAULT_MAP_ZOOM);
    }

    private static void setSize() {
        url.append("&size=" + DEFAULT_IMAGE_MAP_SIZE + "x" + DEFAULT_IMAGE_MAP_SIZE);
    }

    private static void setPath() {
        if (trackData.positions != null && trackData.positions.size() > 0) {
            url.append("&path=color:0x0082ff" + "%7C" + "weight:10" + "%7C");
            for (int i = 0; i < trackData.positions.size(); i++) {
                Position position = trackData.positions.get(i);
                url.append(position.latitude + "," + position.longitude);
                if (i < trackData.positions.size() - 1) {
                    url.append("%7C");
                }
            }
        }
    }

    private static void setApiKey() {
        url.append("&key=" + apiKey);
    }
}
