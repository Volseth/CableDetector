package com.example.cabledetector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.Plot;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.util.PixelUtils;
import com.androidplot.util.PlotStatistics;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;


public class GraphDetectionFragment extends Fragment {

    private XYPlot magnitudeLevelsPlot = null;
    private XYPlot magnitudeLevelsHistoryPlot = null;

    private SimpleXYSeries magnitudeLvlSeries;
    private SimpleXYSeries magnitudeHistorySeries = null;

    private static final int HISTORY_SIZE = 30;
    private Redrawer redrawer;

    public GraphDetectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Override
    public void onPause() {
        redrawer.pause();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        redrawer.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_detection, container, false);

        magnitudeLevelsPlot = (XYPlot) view.findViewById(R.id.plot);

        magnitudeLvlSeries = new SimpleXYSeries("Magnitude");
        magnitudeHistorySeries = new SimpleXYSeries("Magnitude");
        magnitudeHistorySeries.useImplicitXVals();
        magnitudeLevelsPlot.addSeries(magnitudeLvlSeries,
                new BarFormatter(Color.rgb(0, 200, 0), Color.rgb(0, 80, 0)));


        magnitudeLevelsPlot.setDomainLabel("uT");
        magnitudeLevelsPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).
                setFormat(new DecimalFormat("#"));

        // setup the APR History plot:
        magnitudeLevelsHistoryPlot = (XYPlot) view.findViewById(R.id.plot_history);
        magnitudeLevelsHistoryPlot.addSeries(magnitudeHistorySeries,
                new LineAndPointFormatter(
                        Color.rgb(217, 46, 74), null, null, null));
        magnitudeLevelsHistoryPlot.setDomainLabel("uT");
        magnitudeLevelsHistoryPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).
                setFormat(new DecimalFormat("#"));
        magnitudeLevelsHistoryPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).
                setFormat(new DecimalFormat("#"));

        //VALUE RANGE
        magnitudeLevelsPlot.setRangeBoundaries(0,350,BoundaryMode.FIXED);
        magnitudeLevelsHistoryPlot.setRangeBoundaries(0,350,BoundaryMode.FIXED);
        magnitudeLevelsPlot.setRangeStep(StepMode.SUBDIVIDE,8);
        magnitudeLevelsHistoryPlot.setRangeStep(StepMode.SUBDIVIDE,8);
        magnitudeLevelsPlot.setDomainStep(StepMode.SUBDIVIDE,0);
        magnitudeLevelsHistoryPlot.setDomainStep(StepMode.SUBDIVIDE,0);


        magnitudeLevelsPlot.setBorderPaint(null);
        magnitudeLevelsHistoryPlot.setBorderPaint(null);

        //HISTORY
        final PlotStatistics levelStats = new PlotStatistics(500, false);
        final PlotStatistics histStats = new PlotStatistics(500, false);

        //GRAPH RENDERING
        magnitudeLevelsPlot.addListener(levelStats);
        magnitudeLevelsHistoryPlot.addListener(histStats);
        BarRenderer barRenderer = magnitudeLevelsPlot.getRenderer(BarRenderer.class);
        if(barRenderer != null) {
            barRenderer.setBarGroupWidth(
                    BarRenderer.BarGroupWidthMode.FIXED_WIDTH, PixelUtils.dpToPix(18));
        }
        redrawer = new Redrawer(
                Arrays.asList(new Plot[]{magnitudeLevelsHistoryPlot, magnitudeLevelsPlot}),
                100, true);

        redrawer.start();
        return view;
    }

    public void updateFragment(double magnitude){
        magnitudeLvlSeries.setModel(Collections.singletonList(
                magnitude),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);

        // get rid the oldest sample in history:
        if (magnitudeHistorySeries.size() > HISTORY_SIZE) {
            magnitudeHistorySeries.removeFirst();
        }

        // add the latest history sample:
        magnitudeHistorySeries.addLast(null, magnitude);
    }
}