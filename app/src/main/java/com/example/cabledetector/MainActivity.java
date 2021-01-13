package com.example.cabledetector;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private ArrowDetectionFragment arrowDetectionFragment;
    private DetectionLevelFragment detectionLevelFragment;
    private GraphDetectionFragment graphDetectionFragment;
    private BottomNavigationView menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrowDetectionFragment = new ArrowDetectionFragment();
        detectionLevelFragment = new DetectionLevelFragment();
        graphDetectionFragment = new GraphDetectionFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentDetection, detectionLevelFragment)
                .replace(R.id.fragmentArrow, arrowDetectionFragment)
                .replace(R.id.fragmentGraph, graphDetectionFragment)
                .commit();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        menu = findViewById(R.id.bottom_navigation);
        menu.setSelectedItemId(R.id.menu_detector);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_detector:
                        break;
                    case R.id.menu_calibration:
                        Intent intentCalibration = new Intent(getApplicationContext(), CalibrationActivity.class);
                        startActivityForResult(intentCalibration, 1);
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
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
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
            System.out.println("MAG X: "+ magX);
            System.out.println("MAG Y: "+ magY);
            graphDetectionFragment.updateFragment(magnitude);

            arrowDetectionFragment.updateFragment(detectionLevelFragment.getSensitivityBar());
            arrowDetectionFragment.updateFragment(magX, magY, magnitude);


            detectionLevelFragment.updateFragment(magnitude);

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                double result = data.getDoubleExtra("average", 42.00);
                float magXAvg = data.getFloatExtra("averageVectorX", 13);
                float magYAvg = data.getFloatExtra("averageVectorY", -40);
                arrowDetectionFragment.updateFragment(result, magXAvg, magYAvg);
            }
        }
    }
}