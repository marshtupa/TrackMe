package com.triple.trackme.Services;

import com.triple.trackme.Data.Storage.Position;
import com.triple.trackme.Data.Storage.Track;

public final class ImageMapUtils {

    private static final int DEFAULT_IMAGE_MAP_SIZE = 512;
    private static final int DEFAULT_MAP_ZOOM = 15;
    private static final int DEFAULT_MAP_LATITUDE = 0;
    private static final int DEFAULT_MAP_LONGITUDE = 0;

    private static StringBuilder url;
    private static String apiKey;
    private static Track trackData;

    private ImageMapUtils() { }

    public static String getImageUrl(final String apiKey, final Track trackData) {
        ImageMapUtils.apiKey = apiKey;
        ImageMapUtils.trackData = trackData;

        ImageMapUtils.url = new StringBuilder();
        initializeUrl();
        setCenter();
        setStyle();
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
        final int MIN_SIZE = 1;

        Position centerPosition = null;
        if (trackData.positions.size() == MIN_SIZE) {
            centerPosition = trackData.positions.get(0);
        }
        else if (trackData.positions.size() > MIN_SIZE) {
            centerPosition = trackData.positions.get(trackData.positions.size() / 2);
        }

        if (centerPosition == null) {
            url.append("center=");
            url.append(DEFAULT_MAP_LATITUDE);
            url.append(',');
            url.append(DEFAULT_MAP_LONGITUDE);
        }
        else {
            url.append("center=");
            url.append(centerPosition.latitude);
            url.append(',');
            url.append(centerPosition.longitude);
        }
    }

    private static void setStyle() {
        url.append("&style=" +
                "element:geometry" + "%7C" +
                "color:0xebe3cd");
        url.append("&style=" +
                "element:labels.text.fill" + "%7C" +
                "color:0x523735");
        url.append("&style=" +
                "element:labels.text.stroke" + "%7C" +
                "color:0xf5f1e6");
        url.append("&style=" +
                "feature:administrative" + "%7C" +
                "element:geometry.stroke" + "%7C" +
                "color:0xc9b2a6");
        url.append("&style=" +
                "feature:administrative.land_parcel" + "%7C" +
                "element:geometry.stroke" + "%7C" +
                "color:0xdcd2be");
        url.append("&style=" +
                "feature:administrative.land_parcel" + "%7C" +
                "element:labels.text.fill" + "%7C" +
                "color:0xae9e90");
        url.append("&style=" +
                "feature:landscape.natural" + "%7C" +
                "element:geometry" + "%7C" +
                "color:0xdfd2ae");
        url.append("&style=" +
                "feature:poi" + "%7C" +
                "visibility:off");
        url.append("&style=" +
                "feature:poi" + "%7C" +
                "element:geometry" + "%7C" +
                "color:0xdfd2ae");
        url.append("&style=" +
                "feature:poi" + "%7C" +
                "element:labels.text.fill" + "%7C" +
                "color:0x93817c");
        url.append("&style=" +
                "feature:poi.park" + "%7C" +
                "element:geometry.fill" + "%7C" +
                "color:0xa5b076");
        url.append("&style=" +
                "feature:poi.park" + "%7C" +
                "element:labels.text.fill" + "%7C" +
                "color:0x447530");
        url.append("&style=" +
                "feature:road" + "%7C" +
                "element:geometry" + "%7C" +
                "color:0xf5f1e6");
        url.append("&style=" +
                "feature:road.arterial" + "%7C" +
                "element:geometry" + "%7C" +
                "color:0xfdfcf8");
        url.append("&style=" +
                "feature:road.highway" + "%7C" +
                "element:geometry" + "%7C" +
                "color:0xf8c967");
        url.append("&style=" +
                "feature:road.highway" + "%7C" +
                "element:geometry.stroke" + "%7C" +
                "color:0xe9bc62");
        url.append("&style=" +
                "feature:road.highway.controlled_access" + "%7C" +
                "element:geometry" + "%7C" +
                "color:0xe98d58");
        url.append("&style=" +
                "feature:road.highway.controlled_access" + "%7C" +
                "element:geometry.stroke" + "%7C" +
                "color:0xdb8555");
        url.append("&style=" +
                "feature:road.local" + "%7C" +
                "element:labels.text.fill" + "%7C" +
                "color:0x806b63");
        url.append("&style=" +
                "feature:transit.line" + "%7C" +
                "element:geometry" + "%7C" +
                "color:0xdfd2ae");
        url.append("&style=" +
                "feature:transit.line" + "%7C" +
                "element:labels.text.fill" + "%7C" +
                "color:0x8f7d77");
        url.append("&style=" +
                "feature:transit.line" + "%7C" +
                "element:labels.text.stroke" + "%7C" +
                "color:0xebe3cd");
        url.append("&style=" +
                "feature:transit.station" + "%7C" +
                "element:geometry" + "%7C" +
                "color:0xdfd2ae");
        url.append("&style=" +
                "feature:water" + "%7C" +
                "element:geometry.fill" + "%7C" +
                "color:0xb9d3c2");
        url.append("&style=" +
                "feature:water" + "%7C" +
                "element:labels.text.fill" + "%7C" +
                "color:0x92998d");
    }

    private static void setZoom() {
        url.append("&zoom=");
        url.append(DEFAULT_MAP_ZOOM);
    }

    private static void setSize() {
        url.append("&size=");
        url.append(DEFAULT_IMAGE_MAP_SIZE);
        url.append('x');
        url.append(DEFAULT_IMAGE_MAP_SIZE);
    }

    private static void setPath() {
        if (trackData.positions != null && trackData.positions.size() > 0) {
            url.append("&path=color:0x0082ff");
            url.append("%7C");
            url.append("weight:10");
            url.append("%7C");
            for (int i = 0; i < trackData.positions.size(); i++) {
                Position position = trackData.positions.get(i);
                url.append(position.latitude);
                url.append(',');
                url.append(position.longitude);
                if (i < trackData.positions.size() - 1) {
                    url.append("%7C");
                }
            }
        }
    }

    private static void setApiKey() {
        url.append("&key=");
        url.append(apiKey);
    }
}
