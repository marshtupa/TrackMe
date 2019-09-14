package com.triple.trackme;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.triple.trackme.Data.Storage.User;
import com.triple.trackme.Data.Work.UserJson;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;

import static androidx.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.triple.trackme", appContext.getPackageName());
    }

    @Test
    public void addition_isCorrect() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        //File file = new File(appContext.getDir());

        assertEquals(4, 2 + 2);
    }

    @Test
    public void Writing_and_reading_of_User_isCorrect() {
        //String filename = "UserData";
        User user = new User();
        
        ArrayList<String> array = new ArrayList<>();
        Context appContext = InstrumentationRegistry.getTargetContext();
        //File file = c.getFilesDir();
        //init user
        user.login = "log";
        user.name = "Nikolay";
        user.photoFilePath = "photo/";
        user.surname = "Ivanov";
        array.add("trackDir1/");
        array.add("trackDir2/");
        array.add("trackDir2/");
        user.trackFilePaths = array;
        //write user and read user
        User user2 = new User();
        ArrayList<String> array2 = new ArrayList<>();
        user2.login = "logg";
        user2.name = "logg";
        user2.photoFilePath = "logg";
        user2.surname = "logg";
        user2.login = "logg";
        array2.add("trackDir1/");
        array2.add("trackDir2/");
        array2.add("trackDir2/");
        user2.trackFilePaths = array2;


        try{
            UserJson.writeUserToJsonFile(user);
            //user2 = UserJson.readUserFromJsonFile();
            user2.login = "loggg";
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("ERROR: Reading/Writing file exception!");
        }

        //check
        boolean check = false;
        if      ((user.login.equals(user2.login)))
        //(user.name.equals(user2.name))&&
        //(user.photoFilePath.equals(user2.photoFilePath))&&
        //(user.surname.equals(user2.surname)))
        //(user.trackFilePaths.equals(user2.trackFilePaths)))
        {check = true;}
        else{
            System.out.println("ERROR: Users not equaled!");
            String a = user2.login;
            String b = user.login;
            System.out.println(a + a + "12" + b + b);
        }
        assertEquals(true,check);
    }
}


