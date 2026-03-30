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
 * This will manage how the Chart object is processed in order to have
 * (possibly) all the difficulties.
 *
 * Improvements:
 * - Reads Resolution from the [Song] header to compute meaningful tick distances
 * - Uses different minimum distances per difficulty so Easy is sparser than Hard
 *
 * Distances are expressed as multiples of a quarter note (one beat):
 *   Hard   = 1 beat  (same density as before, just normalized)
 *   Medium = 2 beats (half density)
 *   Easy   = 4 beats (quarter density — one note every full bar at 4/4)
 *
 * @author Eughenes
 */
@Component
public class ChartProcessor implements BaseProcessor<Chart> {

    private static final int DEFAULT_RESOLUTION = 192;

    // Multipliers of one quarter note (one beat) per difficulty
    private static final double HARD_BEATS   = 0.5;
    private static final double MEDIUM_BEATS = 1.0;
    private static final double EASY_BEATS   = 2.0;

    private final RowProcessor rowProcessor;

    @Autowired
    public ChartProcessor(RowProcessor rowProcessor) {
        this.rowProcessor = rowProcessor;
    }

    @Override
    public Chart process(Chart chart, Object... params) {
        int resolution = readResolution(chart);
        LogManager.logInfo("Using Resolution: " + resolution);

        int hardDistance   = (int) (resolution * HARD_BEATS);
        int mediumDistance = (int) (resolution * MEDIUM_BEATS);
        int easyDistance   = (int) (resolution * EASY_BEATS);

        for (String key : chart.getSongHashMap().keySet()) {
            Song thisSong = chart.getSongHashMap().get(key);

            if (thisSong.getHard() == null && thisSong.getExpert() != null) {
                thisSong.setHard(new Element(SongDifficulty.HARD.getTextValue() + key,
                        rowProcessor.process(thisSong.getExpert().getContent(), hardDistance, 5)));
                LogManager.logInfo("Processed Difficulty Hard (minDistance=" + hardDistance + ")");
            }

            if (thisSong.getMedium() == null && thisSong.getHard() != null) {
                thisSong.setMedium(new Element(SongDifficulty.MEDIUM.getTextValue() + key,
                        rowProcessor.process(thisSong.getHard().getContent(), mediumDistance, 4)));
                LogManager.logInfo("Processed Difficulty Medium (minDistance=" + mediumDistance + ")");
            }

            if (thisSong.getEasy() == null && thisSong.getMedium() != null) {
                thisSong.setEasy(new Element(SongDifficulty.EASY.getTextValue() + key,
                        rowProcessor.process(thisSong.getMedium().getContent(), easyDistance, 3)));
                LogManager.logInfo("Processed Difficulty Easy (minDistance=" + easyDistance + ")");
            }

            chart.getSongHashMap().put(key, thisSong);
        }
        LogManager.logInfo("Chart Processed");
        return chart;
    }

    /**
     * Reads the Resolution value from the [Song] element in the chart.
     * Falls back to DEFAULT_RESOLUTION (192) if not found.
     */
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
                                LogManager.logError("Could not parse Resolution value: " + parts[1]);
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