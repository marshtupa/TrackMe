package com.triple.trackme.Server;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class UserDatabase {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();;

    private UserDatabase() { }

    public static FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public static void signUp(String firstName, String secondName, String email, String password) {

    }
}
