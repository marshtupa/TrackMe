package com.triple.trackme.Server;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.triple.trackme.Data.Storage.Track;

public final class TrackDatabase {

    private TrackDatabase() { }

    public static void SaveTrack(final Track track) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference()
                .child("users")
                .child("test")
                .child("tracks")
                .child(String.valueOf(track.id));

        myRef.setValue(track);
    }
}
