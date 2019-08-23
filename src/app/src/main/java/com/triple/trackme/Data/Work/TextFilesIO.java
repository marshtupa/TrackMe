package com.triple.trackme.Data.Work;

import com.triple.trackme.Activity.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class TextFilesIO {

    static void deleteFile(String fileName) {
        File file = new File(MainActivity.filesDir, fileName);
        file.delete();
    }

    static boolean isFileExists(String fileName) {
        File file = new File(MainActivity.filesDir, fileName);
        return file.exists();
    }

    static void writeTextToFile(String fileName, String fileText) throws IOException {
        File file = new File(MainActivity.filesDir, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileText);
        bufferedWriter.close();
    }

    static String readTextFromFile(String fileName) throws IOException {
        File file = new File(MainActivity.filesDir, fileName);

        StringBuilder fileText = new StringBuilder();

        FileReader fileReader = new FileReader(file.getAbsoluteFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            fileText.append(line + "\n");
        }

        bufferedReader.close();

        return fileText.toString();
    }
}
