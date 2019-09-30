package com.triple.trackme.Server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Query {

    public static String sendGet(String url) {
        String response = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpclient.execute(request);
            InputStream inputStream = httpResponse.getEntity().getContent();
            response = convertStreamToString(inputStream);
            inputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return response;
    }

    private static String convertStreamToString(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder str = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                str.append(line).append('\n');
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return str.toString();
    }
}
