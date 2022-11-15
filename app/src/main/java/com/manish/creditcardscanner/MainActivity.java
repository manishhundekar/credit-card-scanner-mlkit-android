package com.manish.creditcardscanner;


import static com.manish.creditcardscanner.charting.Easing.EaseInOutCubic;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.manish.creditcardscanner.charting.PieChart;
import com.manish.creditcardscanner.charting.Legend;
import com.manish.creditcardscanner.charting.data.PieData;
import com.manish.creditcardscanner.charting.data.PieDataSet;
import com.manish.creditcardscanner.charting.data.PieEntry;
import com.manish.creditcardscanner.charting.formatter.PercentFormatter;
import com.manish.creditcardscanner.charting.utils.ColorTemplate;
import com.manish.creditcardscanner.charting.utils.MPPointF;
import com.manish.creditcardscanner.mlkit.CameraSource;
import com.manish.creditcardscanner.mlkit.CameraSourcePreview;
import com.manish.creditcardscanner.mlkit.GraphicOverlay;
import com.manish.creditcardscanner.mlkit.textdetector.TextRecognitionProcessor;

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

    private PieChart chart;
    private static View pieChartView;
    Typeface tfRegular;
    Typeface tfLight;

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

        view = findViewById(R.id.textdisplay);
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
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        view = findViewById(R.id.piechart);

        chart = findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 30, 50, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterTextTypeface(tfLight);
        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(2500, EaseInOutCubic);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setXEntrySpace(10f);
        l.setYEntrySpace(10f);
        l.setYOffset(7f);

        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f);

        setPieChartData();
    }

    private void setPieChartData() {
        //Temp Data
        float range = 300;
        String[] categories = new String[] {
                "Investments", "Entertainment", "Groceries", "Bills", "Holidays", "Others"
        };
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < categories.length ; i++) {
            entries.add(new PieEntry( 30, categories[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Transaction Details");

        dataSet.setDrawIcons(true);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
//        chart.highlightValues(null);
        chart.invalidate();
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

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Futura Bank\nActivity Chart");
        s.setSpan(new RelativeSizeSpan(1.2f), 0, 11, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 11, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 11, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.0f), 11, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 11, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 11, s.length(), 0);
        return s;
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
