package com.gsnathan.android_master_scouter;

import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    CSVWriterTool csv;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view
        setContentView(mScannerView);

        csv = new CSVWriterTool();

        int pid = android.os.Process.myPid();
        String whiteList = "logcat -P '" + pid + "'";
        try {
            Runtime.getRuntime().exec(whiteList).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
        csv = new CSVWriterTool();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Prints scan results

        String result = rawResult.getText();



        String[] str = result.split("br");

        for (String s : str) {
            ArrayList<String> row = new ArrayList<String>();
            String[] data = s.split(",");
            for (String r : data) {
                if (!r.isEmpty() && !r.equals(" "))
                    row.add(r);
            }

            Log.d("result", row.toString());

            for(int x  =0; x< row.size(); x++){
                int prompt = -1;
                try{prompt = Integer.parseInt(row.get(x).trim());
                }catch (NumberFormatException n){
                    n.printStackTrace();
                }
                switch (x) {
                    case 1:
                        row.set(1, prompt == 0 ? "Level 1" : "Level 2");
                        break;
                    case 2:
                        row.set(2, prompt == 0 ? "No Piece" : prompt == 1 ? "Hatch" : "Cargo");
                        break;
                    case 9:
                        row.set(9, prompt != 0 ? "Yes" : "No");
                        break;
                    case 10:
                        row.set(10, prompt == 0 ? "No" : "Yes");
                        break;
                    case 11:
                        row.set(11, prompt == 0 ? "No" : "Yes");
                        break;
                    case 14:
                        row.set(14, prompt == 0 ? "Level 1" : prompt == 1 ? "Level 2" : prompt == 2 ? "Level 3" : "Didn't even get there");
                        break;
                    default:
                        break;

                }
            }
            String[] rowArray = row.toArray(new String[row.size()]);
            try {
                csv.writeLineToCSV(rowArray);
            } catch (IOException e) {
                Log.d("HELL", "IO");
                e.printStackTrace();
            }

        }

        Snackbar snack = Snackbar.make(mScannerView,"Scanned!", Snackbar.LENGTH_INDEFINITE);
        snack.show();
        finish();
    }
}