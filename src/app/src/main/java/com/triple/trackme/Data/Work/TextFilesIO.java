package com.triple.trackme.Data.Work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class TextFilesIO {

    static void deleteFile(File filesDir, String fileName) {
        File file = new File(filesDir, fileName);
        file.delete();
    }

    static boolean isFileExists(File filesDir, String fileName) {
        File file = new File(filesDir, fileName);
        return file.exists();
    }

    static void writeTextToFile(File filesDir, String fileName, String fileText) throws IOException {
        File file = new File(filesDir, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileText);
        bufferedWriter.close();
    }

    static String readTextFromFile(File filesDir, String fileName) throws IOException {
        File file = new File(filesDir, fileName);

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
