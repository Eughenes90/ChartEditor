package com.eughenes.chartEditor.services;


import com.eughenes.chartEditor.services.processors.impl.ChartProcessor;
import com.eughenes.chartEditor.model.entity.build.Chart;
import com.eughenes.chartEditor.model.factory.impl.build.ChartFactory;
import com.eughenes.chartEditor.utils.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Entire system workflow
 *
 * @author Eughenes
 */
@Service
public class ChartService {

    private final ChartFactory chartFactory;

    private final ChartProcessor chartProcessor;

    private final FileService fileService;

    @Autowired
    public ChartService(ChartFactory chartFactory, ChartProcessor chartProcessor, FileService fileService) {
        this.chartFactory = chartFactory;
        this.chartProcessor = chartProcessor;
        this.fileService = fileService;
    }

    public List<String> getProcessedChart(List<String> selectedChart){
        Chart chart = chartFactory.create(selectedChart);
        chartProcessor.process(chart);
        return chart.printChart();
    }

}
