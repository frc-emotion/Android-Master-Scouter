package com.gsnathan.android_master_scouter;

import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

class CSVWriterTool {
    private static final String CSV_FILE = "scouter_data_total.csv";
    private static final String APP_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/DeepScouterApp";

    private final String[] HEADERS = {"Team Number", "Start Position", "Starting Piece", "Sand Storm Cargo", "Sand Storm Hatches", "Cargo Ship Cargo", "Cargo Ship Hatches"
, "Rocket Cargo", "Rocket Hatches", "Rocket Level 1", "Rocket Level 2", "Rocket Level 3", "Dropped Cargo", "Dropped Hatches", "Climb Position", "Notes"};
    private File csvFile;

    CSVWriterTool() {
        File folder = new File(APP_FOLDER);
        if(! folder.isDirectory()){
            folder.mkdir();
        }
        csvFile = new File(folder, CSV_FILE);
    }

    void writeLineToCSV(String[] data) throws IOException {
        CSVWriter writer;
        //append file
        if (csvFile.exists() && !csvFile.isDirectory()) {
            FileWriter mFileWriter = new FileWriter(csvFile, true);
            writer = new CSVWriter(mFileWriter);
        }
        //else create a file
        else {
            try {
                csvFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("IOOO", "ioddo");
            }
            writer = new CSVWriter(new FileWriter(csvFile));
            writer.writeNext(HEADERS);
        }
        writer.writeNext(data);
        writer.close();
    }
}