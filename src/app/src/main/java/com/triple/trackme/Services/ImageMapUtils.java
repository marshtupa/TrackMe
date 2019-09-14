package com.triple.trackme.Services;

import com.triple.trackme.Data.Storage.Position;
import com.triple.trackme.Data.Storage.Track;

public final class ImageMapUtils {

    private static final int DEFAULT_IMAGE_MAP_SIZE = 512;
    private static final int DEFAULT_MAP_ZOOM = 15;
    private static final int DEFAULT_MAP_LATITUDE = 0;
    private static final int DEFAULT_MAP_LONGITUDE = 0;

    private static String apiKey;
    private static Track trackData;

    private ImageMapUtils() { }

    public static String getImageUrl(final String apiKey, final Track trackData) {
        ImageMapUtils.apiKey = apiKey;
        ImageMapUtils.trackData = trackData;

        StringBuilder url = new StringBuilder();
        initializeUrl(url);
        setCenter(url);
        setStyle(url);
        setZoom(url);
        setSize(url);
        setPath(url);
        setApiKey(url);
        return url.toString();
    }

    private static void initializeUrl(final StringBuilder url) {
        url.append("https://maps.googleapis.com/maps/api/staticmap?");
    }

    private static void setCenter(final StringBuilder url) {
        final int MIN_SIZE = 1;

        Position centerPosition = null;
        if (trackData.positions.size() == MIN_SIZE) {
            centerPosition = trackData.positions.get(0);
        }
        else if (trackData.positions.size() > MIN_SIZE) {
            centerPosition = trackData.positions.get(trackData.positions.size() / 2);
        }

        if (centerPosition == null) {
            url.append("center=")
                    .append(DEFAULT_MAP_LATITUDE)
                    .append(',')
                    .append(DEFAULT_MAP_LONGITUDE);
        }
        else {
            url.append("center=")
                    .append(centerPosition.latitude)
                    .append(',')
                    .append(centerPosition.longitude);
        }
    }

    private static void setStyle(final StringBuilder url) {
        url.append("&style=element:geometry%7Ccolor:0xebe3cd")
                .append("&style=element:labels.text.fill%7Ccolor:0x523735")
                .append("&style=element:labels.text.stroke%7Ccolor:0xf5f1e6")
                .append("&style=feature:administrative%7Celement:geometry.stroke%7Ccolor:0xc9b2a6")
                .append("&style=feature:administrative.land_parcel%7Celement:geometry.stroke%7Ccolor:0xdcd2be")
                .append("&style=feature:administrative.land_parcel%7Celement:labels.text.fill%7Ccolor:0xae9e90")
                .append("&style=feature:landscape.natural%7Celement:geometry%7Ccolor:0xdfd2ae")
                .append("&style=feature:poi%7Cvisibility:off")
                .append("&style=feature:poi%7Celement:geometry%7Ccolor:0xdfd2ae")
                .append("&style=feature:poi%7Celement:labels.text.fill%7Ccolor:0x93817c")
                .append("&style=feature:poi.park%7Celement:geometry.fill%7Ccolor:0xa5b076")
                .append("&style=feature:poi.park%7Celement:labels.text.fill%7Ccolor:0x447530")
                .append("&style=feature:road%7Celement:geometry%7Ccolor:0xf5f1e6")
                .append("&style=feature:road.arterial%7Celement:geometry%7Ccolor:0xfdfcf8")
                .append("&style=feature:road.highway%7Celement:geometry%7Ccolor:0xf8c967")
                .append("&style=feature:road.highway%7Celement:geometry.stroke%7Ccolor:0xe9bc62")
                .append("&style=feature:road.highway.controlled_access%7Celement:geometry%7Ccolor:0xe98d58")
                .append("&style=feature:road.highway.controlled_access%7Celement:geometry.stroke%7Ccolor:0xdb8555")
                .append("&style=feature:road.local%7Celement:labels.text.fill%7Ccolor:0x806b63")
                .append("&style=feature:transit.line%7Celement:geometry%7Ccolor:0xdfd2ae")
                .append("&style=feature:transit.line%7Celement:labels.text.fill%7Ccolor:0x8f7d77")
                .append("&style=feature:transit.line%7Celement:labels.text.stroke%7Ccolor:0xebe3cd")
                .append("&style=feature:transit.station%7Celement:geometry%7Ccolor:0xdfd2ae")
                .append("&style=feature:water%7Celement:geometry.fill%7Ccolor:0xb9d3c2")
                .append("&style=feature:water%7Celement:labels.text.fill%7Ccolor:0x92998d");
    }

    private static void setZoom(final StringBuilder url) {
        url.append("&zoom=")
                .append(DEFAULT_MAP_ZOOM);
    }

    private static void setSize(final StringBuilder url) {
        url.append("&size=")
                .append(DEFAULT_IMAGE_MAP_SIZE)
                .append('x')
                .append(DEFAULT_IMAGE_MAP_SIZE);
    }

    private static void setPath(final StringBuilder url) {
        if (trackData.positions != null && trackData.positions.size() > 0) {
            url.append("&path=")
                    .append("color:0x0082ff")
                    .append("%7C")
                    .append("weight:10")
                    .append("%7C");
            for (int i = 0; i < trackData.positions.size(); i++) {
                Position position = trackData.positions.get(i);
                url.append(position.latitude)
                        .append(',')
                        .append(position.longitude);
                if (i < trackData.positions.size() - 1) {
                    url.append("%7C");
                }
            }
        }
    }

    private static void setApiKey(final StringBuilder url) {
        url.append("&key=")
                .append(apiKey);
    }
}
