package com.sedadurmus.yenivavi.Model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    public static WriteFile Instance = new WriteFile();

    public void SaveDataToFile(String text, Context context) {
        SaveDataToFile(text,context,"/userID.txt");
    }
    public void SaveDataToFile(String text, Context context, String filename) {
        try {
             FileWriter fWriter;
            String path = context.getFilesDir().getPath();
            File sdCardFile = new File(path + filename);
            sdCardFile.createNewFile();
            Log.d("TAG", sdCardFile.getPath());


            try {
                fWriter = new FileWriter(sdCardFile, true);
                fWriter.write(text);
                fWriter.flush();
                fWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
    }
    public String ReadDataFromFile(Context context){
        return ReadDataFromFile(context,"/userID.txt");
    }
    public String ReadDataFromFile(Context context,String filename) {
        File sdcard = context.getFilesDir();

        File file = new File(sdcard, filename);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);

            }

            br.close();
            return text.toString();
        } catch (IOException e) {

            String s = e.getMessage();
            //You'll need to add proper error handling here
        }

        return "";
    }
}
