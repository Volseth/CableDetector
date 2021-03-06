package com.example.cabledetector;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class DetectionLevelFragment extends Fragment {

    private TextView textViewSeekBarValue, detectionLevelValue;
    private CustomGauge guage;
    private SeekBar sensitivityBar;
    public static DecimalFormat DECIMAL_FORMATTER;

    public DetectionLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detection_level, container, false);
        guage = view.findViewById(R.id.gauge_level);
        detectionLevelValue = view.findViewById(R.id.textView_current_Value);
        textViewSeekBarValue = view.findViewById(R.id.textView_seek_bar_value);
        sensitivityBar = view.findViewById(R.id.seekBar_sensitivity);
        String message = "Sensitivity: " + String.valueOf(sensitivityBar.getProgress());
        textViewSeekBarValue.setText(message);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#", symbols);

        sensitivityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String message = "Sensitivity: " + String.valueOf(progress);
                textViewSeekBarValue.setText(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    public int getSensitivityBar(){
        return sensitivityBar.getProgress();
    }
    public void updateFragment(double magnitude){
        String message = DECIMAL_FORMATTER.format(magnitude) + " \u00B5T";
        guage.setValue(Math.min((int)magnitude,800));
        detectionLevelValue.setText(message);
    }
}