package com.triple.trackme.Data.Work;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.triple.trackme.Activity.Main.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageFilesHelper {

    public static void deleteFile(final String fileName) {
        if (isFileExists(fileName)) {
            File file = new File(MainActivity.filesDir, fileName);
            file.delete();
        }
    }

    public static boolean isFileExists(final String fileName) {
        File file = new File(MainActivity.filesDir, fileName);
        return file.exists();
    }

    public static void writeBitmapToFile(final String fileName, final Bitmap image) throws IOException {
        File file = new File(MainActivity.filesDir, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
    }

    public static Bitmap readBitmapFromFile(final String fileName) throws IOException {
        File file = new File(MainActivity.filesDir, fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        return bitmap;
    }
}
