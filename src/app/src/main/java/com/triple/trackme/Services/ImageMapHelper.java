package com.triple.trackme.Services;

public class ImageMapHelper {

    private static final int DEFAULT_IMAGE_MAP_SIZE = 512;

    public static String getImageUrl(final String apiKey) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?center=Berkeley,CA&zoom=14");
        url.append("&size="+ DEFAULT_IMAGE_MAP_SIZE + "x" + DEFAULT_IMAGE_MAP_SIZE);
        url.append("&key=" + apiKey);
        return url.toString();
    }
}
