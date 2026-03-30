package com.eughenes.chartEditor.services;

import com.eughenes.chartEditor.model.entity.build.Chart;
import com.eughenes.chartEditor.model.factory.impl.build.ChartFactory;
import com.eughenes.chartEditor.services.processors.impl.ChartProcessor;
import com.eughenes.chartEditor.utils.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Entire system workflow — now accepts custom beat multipliers from the UI
 *
 * @author Eughenes
 */
@Service
public class ChartService {

    private final ChartFactory chartFactory;
    private final ChartProcessor chartProcessor;

    @Autowired
    public ChartService(ChartFactory chartFactory, ChartProcessor chartProcessor) {
        this.chartFactory = chartFactory;
        this.chartProcessor = chartProcessor;
    }

    /**
     * Processes the chart using custom per-difficulty beat multipliers.
     */
    public List<String> getProcessedChart(List<String> selectedChart,
                                          double hardBeats,
                                          double mediumBeats,
                                          double easyBeats) {
        Chart chart = chartFactory.create(selectedChart);
        chartProcessor.process(chart, hardBeats, mediumBeats, easyBeats);
        return chart.printChart();
    }

    /**
     * Processes with default multipliers (for backward compatibility).
     */
    public List<String> getProcessedChart(List<String> selectedChart) {
        return getProcessedChart(selectedChart, 0.5, 1.0, 2.0);
    }
}
