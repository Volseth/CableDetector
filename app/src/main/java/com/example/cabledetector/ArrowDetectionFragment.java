package com.example.cabledetector;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ArrowDetectionFragment extends Fragment {

    private ImageView arrow;
    private TextView textDetection;
    private Button buttonResetCalibration;
    private int sensitivity=50;
    private double baseMagnitudeLevel = 42;
    private float baseMagX;
    private float baseMagY;
    private final double DEFAULT_MAGNITUDE_LEVEL = 30.0;
    private final float DEFAULT_VECTOR_X = 13;
    private final float DEFAULT_VECTOR_Y = -40;
    private final double MAGNITUDE_LEVEL_THRESHOLD = 1000;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arrow_detection, container, false);
        arrow = view.findViewById(R.id.imageView_arrow);
        textDetection = view.findViewById(R.id.textView_arrow);
        buttonResetCalibration = view.findViewById(R.id.button_calibration_reset);

        buttonResetCalibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFragment(DEFAULT_MAGNITUDE_LEVEL, DEFAULT_VECTOR_X, DEFAULT_VECTOR_Y);
            }
        });
        return view;
    }
    public void updateFragment(double baseMagnitudeLevel, float baseMagX, float baseMagY){
        this.baseMagnitudeLevel = baseMagnitudeLevel;
        this.baseMagX = baseMagX;
        this.baseMagY = baseMagY;
    }
    public void updateFragment(int sensitivity){
        this.sensitivity=sensitivity;
    }

    public void updateFragment(float magX, float magY, double magnitudeValue){
        float magXabs = Math.abs(magX -baseMagX);
        float magYabs = Math.abs(magY -baseMagY);

        float maxVal = Math.max(magXabs,magYabs);
        double currentDetectedMagnitude = magnitudeValue - baseMagnitudeLevel;
        double currentMagnitudeThreshold = (1/(double)sensitivity)*MAGNITUDE_LEVEL_THRESHOLD;
        if(currentDetectedMagnitude > currentMagnitudeThreshold)
        {
            arrow.setImageResource(R.drawable.reddot);
            textDetection.setText(R.string.arrow_detection);
            textDetection.setBackgroundColor(Color.RED);
        }
        else {
            textDetection.setText(R.string.arrow_no_detection);
            textDetection.setBackgroundColor(Color.GREEN);
            if (maxVal == magXabs) {

                if (magX >= 0) {
                    arrow.setImageResource(R.drawable.arrow_right);
                } else {
                    arrow.setImageResource(R.drawable.arrow_left);

                }
            }
            if (maxVal == magYabs) {

                if (magY >= 0) {
                    arrow.setImageResource(R.drawable.arrow_up);
                } else {
                    arrow.setImageResource(R.drawable.arrow_down);
                }

            }
        }


    }
}