package com.triple.trackme.Data.Work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFilesIO {

    public static void WriteTextToFile(File filesDir, String fileName, String fileText)
    {
        File file = new File(filesDir, fileName);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(fileText);
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String ReadTextFromFile(File filesDir, String fileName)
    {
        File file = new File(filesDir, fileName);

        String fileText = "";
        StringBuffer output = new StringBuffer();
        try {
            FileReader fileReader = new FileReader(file.getAbsoluteFile());
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line + "\n");
            }

            fileText = output.toString();
            bufferedReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return fileText;
    }
}
