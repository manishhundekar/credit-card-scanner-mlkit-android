package com.manish.creditcardscanner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.manish.creditcardscanner.mlkit.CameraSource;
import com.manish.creditcardscanner.mlkit.CameraSourcePreview;
import com.manish.creditcardscanner.mlkit.GraphicOverlay;
import com.manish.creditcardscanner.mlkit.textdetector.TextGraphic;
import com.manish.creditcardscanner.mlkit.textdetector.TextRecognitionProcessor;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public final class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private CameraSourcePreview preview;
    private CameraSource cameraSource = null;
    private GraphicOverlay graphicOverlay;

    public static Bitmap bitmap;
    private static View view;
    public ListAdapter adapter;
    String[] array = {"Android","IPhone"};

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

        adapter = new ListAdapter(this, R.layout.listview, array, array, array, array);
        ListView listView = (ListView) findViewById(R.id.listVw);
        listView.setAdapter(adapter);

        view = findViewById(R.id.textdisplay);
        bitmap = loadBitmapFromView();
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

    public static Bitmap loadBitmapFromView() {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}
