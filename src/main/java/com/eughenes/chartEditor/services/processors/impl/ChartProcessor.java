package com.eughenes.chartEditor.services.processors.impl;

import com.eughenes.chartEditor.model.entity.build.Chart;
import com.eughenes.chartEditor.model.entity.build.Element;
import com.eughenes.chartEditor.model.entity.build.Song;
import com.eughenes.chartEditor.model.entity.build.enums.SongDifficulty;
import com.eughenes.chartEditor.services.processors.interfaces.BaseProcessor;
import com.eughenes.chartEditor.utils.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manages how the Chart object is processed to generate all difficulties.
 * Reads Resolution from [Song] header and accepts custom beat multipliers
 * passed as params: params[0]=hardBeats, params[1]=mediumBeats, params[2]=easyBeats
 *
 * @author Eughenes
 */
@Component
public class ChartProcessor implements BaseProcessor<Chart> {

    private static final int DEFAULT_RESOLUTION = 192;

    private final RowProcessor rowProcessor;

    @Autowired
    public ChartProcessor(RowProcessor rowProcessor) {
        this.rowProcessor = rowProcessor;
    }

    @Override
    public Chart process(Chart chart, Object... params) {
        double hardBeats   = params.length > 0 ? (double) params[0] : 0.5;
        double mediumBeats = params.length > 1 ? (double) params[1] : 1.0;
        double easyBeats   = params.length > 2 ? (double) params[2] : 2.0;

        int resolution = readResolution(chart);
        LogManager.logInfo("Using Resolution: " + resolution);

        int hardDistance   = (int) (resolution * hardBeats);
        int mediumDistance = (int) (resolution * mediumBeats);
        int easyDistance   = (int) (resolution * easyBeats);

        for (String key : chart.getSongHashMap().keySet()) {
            Song thisSong = chart.getSongHashMap().get(key);

            if (thisSong.getHard() == null && thisSong.getExpert() != null) {
                thisSong.setHard(new Element(SongDifficulty.HARD.getTextValue() + key,
                        rowProcessor.process(thisSong.getExpert().getContent(), hardDistance, 5)));
                LogManager.logInfo("Processed Hard (minDistance=" + hardDistance + ")");
            }

            if (thisSong.getMedium() == null && thisSong.getHard() != null) {
                thisSong.setMedium(new Element(SongDifficulty.MEDIUM.getTextValue() + key,
                        rowProcessor.process(thisSong.getHard().getContent(), mediumDistance, 4)));
                LogManager.logInfo("Processed Medium (minDistance=" + mediumDistance + ")");
            }

            if (thisSong.getEasy() == null && thisSong.getMedium() != null) {
                thisSong.setEasy(new Element(SongDifficulty.EASY.getTextValue() + key,
                        rowProcessor.process(thisSong.getMedium().getContent(), easyDistance, 3)));
                LogManager.logInfo("Processed Easy (minDistance=" + easyDistance + ")");
            }

            chart.getSongHashMap().put(key, thisSong);
        }
        LogManager.logInfo("Chart Processed");
        return chart;
    }

    private int readResolution(Chart chart) {
        for (Element element : chart.getChartElements()) {
            if ("Song".equals(element.getTitle())) {
                for (String line : element.getContent()) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("Resolution")) {
                        String[] parts = trimmed.split("=");
                        if (parts.length == 2) {
                            try {
                                return Integer.parseInt(parts[1].trim());
                            } catch (NumberFormatException e) {
                                LogManager.logError("Could not parse Resolution: " + parts[1]);
                            }
                        }
                    }
                }
            }
        }
        LogManager.logInfo("Resolution not found, using default: " + DEFAULT_RESOLUTION);
        return DEFAULT_RESOLUTION;
    }
}
