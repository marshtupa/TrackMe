package com.triple.trackme.Data.Work;

import com.triple.trackme.Activity.Main.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

final class TextFilesUtils {

    private TextFilesUtils() { }

    static void deleteFile(final String fileName) {
        if (isFileExists(fileName)) {
            File file = new File(MainActivity.filesDir, fileName);
            file.delete();
        }
    }

    static boolean isFileExists(final String fileName) {
        File file = new File(MainActivity.filesDir, fileName);
        return file.exists();
    }

    static void writeTextToFile(final String fileName, final String fileText) throws IOException {
        File file = new File(MainActivity.filesDir, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileText);
        bufferedWriter.close();
    }

    static String readTextFromFile(final String fileName) throws IOException {
        File file = new File(MainActivity.filesDir, fileName);

        StringBuilder fileText = new StringBuilder();

        FileReader fileReader = new FileReader(file.getAbsoluteFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            fileText.append(line);
            fileText.append('\n');
        }

        bufferedReader.close();

        return fileText.toString();
    }
}
