package com.triple.trackme.Services;

public class ImageMapHelper {

    public static String getImageUrl(String apiKey) {
        String url = "https://maps.googleapis.com/maps/api/staticmap?center=Berkeley,CA&zoom=14&size=400x400";

        url += "&key=" + apiKey;
        return url;
    }
}
