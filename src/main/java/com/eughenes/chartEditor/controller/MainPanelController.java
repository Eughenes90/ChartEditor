package com.eughenes.chartEditor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import com.eughenes.chartEditor.services.ChartService;
import com.eughenes.chartEditor.services.FileService;
import java.io.File;
import com.eughenes.chartEditor.utils.LogManager;

import java.io.IOException;

@Component
public class MainPanelController {


    private final ChartService chartService;

    private final FileService fileService;

    @Autowired
    public MainPanelController(ChartService chartService, FileService fileService) {
        this.chartService = chartService;
        this.fileService = fileService;
    }

    public void onOpenButtonClick() throws IOException {


        File inputFile = fileService.selectFile();
        if (inputFile == null) {
            LogManager.logError("The file has not been selected");
            return;
        }
        List<String> fileContent = fileService.readFileContent(inputFile);
        if (fileContent == null || fileContent.isEmpty()) {
            LogManager.logError("The file is empty");
            return;
        }
        List<String> processedChart = chartService.getProcessedChart(fileContent);

        fileService.saveFile(inputFile.toPath(), fileContent, processedChart);

    }

}
