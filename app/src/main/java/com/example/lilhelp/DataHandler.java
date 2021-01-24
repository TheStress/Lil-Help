package com.example.lilhelp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.renderscript.ScriptGroup;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DataHandler {

    public static final String today = LocalDateTime.now().toString().substring(0,10); // yr/month/day format

    public static void save(Context context, String fileContent) throws FileNotFoundException {
        try (FileOutputStream fos = context.openFileOutput(today, Context.MODE_PRIVATE)) {
            byte[] contentByteArray = fileContent.getBytes();
            fos.write(contentByteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static JournalEntry get(Context context, String filename) throws FileNotFoundException {
        JournalEntry je = new JournalEntry();
        FileInputStream fis = context.openFileInput(filename);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            int i = 0;
            String line = reader.readLine();
            while (line != null) {
                if(i == 0 && line.equals("GQ"))
                {
                    ForwardQuestion fq = new ForwardQuestion();
                    fq.setAnswer(sb.toString());
                    je.setFq(fq);
                    sb.delete(0,sb.length());
                    i++;
                }
                else if(i == 1 && line.equals("AQ"))
                {
                    GratefulQuestion gq = new GratefulQuestion();
                    gq.setAnswer(sb.toString());
                    je.setGq(gq);
                    sb.delete(0,sb.length());
                    i++;
                }
                else if(i == 2 && line.equals("text"))
                {
                    AccomplishQuestion aq = new AccomplishQuestion();
                    aq.setAnswer(sb.toString());
                    je.setAq(aq);
                    sb.delete(0,sb.length());
                }
                else
                {
                    sb.append(line);
                }
                line = reader.readLine();
            }
            je.setText(sb.toString());
        } catch (IOException e) {
            return null;
        } finally {
            return je;
        }
    }
}