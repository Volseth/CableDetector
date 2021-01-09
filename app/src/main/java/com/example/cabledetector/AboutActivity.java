package com.example.cabledetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutActivity extends AppCompatActivity {

    private BottomNavigationView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        menu = findViewById(R.id.bottom_navigation);
        menu.setSelectedItemId(R.id.menu_about);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_detector:
                        Intent intentDetection = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentDetection);
                        break;
                    case R.id.menu_calibration:
                        Intent intentCalibration = new Intent(getApplicationContext(), CalibrationActivity.class);
                        startActivity(intentCalibration);
                        break;
                    case R.id.menu_about:
                        break;
                }
                return true;
            }
        });
        String htmlAsString = getString(R.string.htmlAbout);
        WebView webView = (WebView) findViewById(R.id.webAbout);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
    }
}