package com.example.cabledetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CalibrationActivity extends AppCompatActivity implements SensorEventListener {

    private Button startCalibrationButton;
    private BottomNavigationView menu;
    private SensorManager sensorManager;
    private List<Double> avgMagnitudeCalibrationList;
    private List<Float> avgMagnitutedXVector;
    private List<Float> avgMagnitueYVector;

    public double listSum(List<Double> list){
        double sum=0;
        for (double elem:
             list) {
            sum+=elem;

        }
        return sum;
    }
    public float listFloatSum(List<Float> list){
        float sum = 0;
        for (float elem: list){
            sum+= elem;
        }
        return sum;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        startCalibrationButton = findViewById(R.id.button_calibration_start);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        menu = findViewById(R.id.bottom_navigation);
        menu.setSelectedItemId(R.id.menu_calibration);

        String htmlAsString = getString(R.string.htmlCalibration);
        WebView webViewAbout = (WebView) findViewById(R.id.webViewCalibration);
        webViewAbout.setBackgroundColor(Color.TRANSPARENT);
        webViewAbout.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);

        startCalibrationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                        avgMagnitudeCalibrationList = new ArrayList<>();
                        avgMagnitutedXVector = new ArrayList<>();
                        avgMagnitueYVector = new ArrayList<>();
                        sensorManager.registerListener(CalibrationActivity.this ,sensor, SensorManager.SENSOR_DELAY_NORMAL);
                        return true;
                    case MotionEvent.ACTION_UP:
                        Intent intentCallback = new Intent();
                        intentCallback.putExtra("average", listSum(avgMagnitudeCalibrationList)/avgMagnitudeCalibrationList.size());
                        intentCallback.putExtra("averageVectorX", listFloatSum(avgMagnitutedXVector)/avgMagnitutedXVector.size());
                        intentCallback.putExtra("averageVectorY", listFloatSum(avgMagnitueYVector)/avgMagnitueYVector.size());
                        setResult(RESULT_OK, intentCallback);
                        sensorManager.unregisterListener(CalibrationActivity.this);
                        finish();
                        return true;
                }
                return false;
            }
        });
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_detector:
                        Intent intentDetection = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentDetection);
                        break;
                    case R.id.menu_calibration:
                        break;
                    case R.id.menu_about:
                        Intent intentAbout = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(intentAbout);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public synchronized void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // get values for each axes X,Y,Z
            float magX = event.values[0];
            float magY = event.values[1];
            float magZ = event.values[2];
            double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));
            avgMagnitudeCalibrationList.add(magnitude);
            avgMagnitutedXVector.add(magX);
            avgMagnitueYVector.add(magY);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


}