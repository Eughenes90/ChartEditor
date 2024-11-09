package com.eughenes.chartEditor.service;


import com.eughenes.chartEditor.control.impl.ChartProcessor;
import com.eughenes.chartEditor.entity.build.Chart;
import com.eughenes.chartEditor.factory.impl.build.ChartFactory;
import com.eughenes.chartEditor.view.file.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Entire system workflow
 *
 * @author Eughenes
 */
@Service
public class ChartService {

    private final ChartFactory chartFactory;

    private final ChartProcessor chartProcessor;

    private final FileHandler fileHandler;

    @Autowired
    public ChartService(ChartFactory chartFactory, ChartProcessor chartProcessor, FileHandler fileHandler) {
        this.chartFactory = chartFactory;
        this.chartProcessor = chartProcessor;
        this.fileHandler = fileHandler;
    }

    public void processChart() throws IOException {
        File chartFile = fileHandler.selectFile();
        if (chartFile != null) {
            Chart chart = chartFactory.create(chartFile);
            chartProcessor.process(chart);
            fileHandler.saveFile(chartFile.toPath(), chart.printChart());
        }
    }

}
