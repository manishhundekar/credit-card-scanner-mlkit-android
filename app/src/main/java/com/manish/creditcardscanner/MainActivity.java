package com.manish.creditcardscanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.manish.creditcardscanner.charting.MyGraphview;
import com.manish.creditcardscanner.mlkit.CameraSource;
import com.manish.creditcardscanner.mlkit.CameraSourcePreview;
import com.manish.creditcardscanner.mlkit.GraphicOverlay;
import com.manish.creditcardscanner.mlkit.textdetector.TextRecognitionProcessor;
import com.manish.creditcardscanner.utils.ListAdapter;
import com.manish.creditcardscanner.utils.Transaction;

import java.io.IOException;
import java.util.ArrayList;

public final class MainActivity extends AppCompatActivity{
    private static final String TAG = "ARActivity";
    private CameraSourcePreview preview;
    private CameraSource cameraSource = null;
    private GraphicOverlay graphicOverlay;

    private static View view;
    public ListAdapter adapter;
    ArrayList<Transaction> activityArray;

    private static View pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_vision_live_preview);

        preview = findViewById(R.id.preview_view);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.graphic_overlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        createCameraSource();
        startCameraSource();

        setActivityData();
        pieChartSetup();

        view = findViewById(R.id.transaction_display);
        pieChartView = findViewById(R.id.piechart);
    }

    private void createCameraSource() {
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        cameraSource.setMachineLearningFrameProcessor(
                new TextRecognitionProcessor(this, new TextRecognizerOptions.Builder().build()));
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        createCameraSource();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private void pieChartSetup() {
        //Set PieChart Data (Max: 6)
        String[] categories = { "Others", "Investments", "Entertainment", "Groceries", "Bills", "Holidays" };
        float values[]={100,200,300,400,500,600};

        LinearLayout linear=(LinearLayout) findViewById(R.id.piechart);
        linear.addView(new MyGraphview(this, values, categories));

    }

    private void setActivityData() {
        //Temp Data
        Transaction transaction1 = new Transaction(
                "Transfer",
                "March 27, 2022",
                "GBP 123.00",
                "Dr");

        Transaction transaction2 = new Transaction(
                "Transfer",
                "April 12, 2022",
                "GBP 223.00",
                "Cr");

        activityArray = new ArrayList<>();
        activityArray.add(transaction1);
        activityArray.add(transaction2);

        //
        adapter = new ListAdapter(this, R.layout.listview, activityArray);
        ListView listView = (ListView) findViewById(R.id.listVw);
        listView.setAdapter(adapter);
    }


    public static Bitmap loadBitmapFromView() {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static Bitmap loadPieChartBitmapFromView() {
        pieChartView.setDrawingCacheEnabled(true);
        pieChartView.buildDrawingCache();
        return pieChartView.getDrawingCache();
    }

}
