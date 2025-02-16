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
 * @author Eughenes
 */
@Component
public class ChartProcessor implements BaseProcessor<Chart> {

    private final RowProcessor rowProcessor;

    @Autowired
    public ChartProcessor(RowProcessor rowProcessor) {
        this.rowProcessor = rowProcessor;
    }

    @Override
    public Chart process(Chart chart, Object... params) {
        int difficulty = 700;
        for (String key : chart.getSongHashMap().keySet()) {
            Song thisSong = chart.getSongHashMap().get(key);
            while (mustBeProcessed(thisSong)) {
                if (thisSong.getHard() == null && thisSong.getExpert() != null) {
                    thisSong.setHard(new Element(SongDifficulty.HARD.getTextValue() + key, rowProcessor.process(thisSong.getExpert().getContent(), difficulty, 5)));
                    LogManager.logInfo("Processed Difficulty Hard");
                } else if (thisSong.getMedium() == null && thisSong.getHard() != null) {
                    thisSong.setMedium(new Element(SongDifficulty.MEDIUM.getTextValue() + key, rowProcessor.process(thisSong.getHard().getContent(), difficulty, 4)));
                    LogManager.logInfo("Processed Difficulty Medium");
                } else if (thisSong.getEasy() == null && thisSong.getMedium() != null) {
                    thisSong.setEasy(new Element(SongDifficulty.EASY.getTextValue() + key, rowProcessor.process(thisSong.getMedium().getContent(), difficulty, 3)));
                    LogManager.logInfo("Processed Difficulty Easy");
                }
            }
            chart.getSongHashMap().put(key, thisSong);
        }
        LogManager.logInfo("Chart Processed");
        return chart;
    }

    private boolean mustBeProcessed(Song song) {
        if (song.getExpert() == null && song.getHard() == null && song.getMedium() == null) {
            return false;
        } else if (song.getEasy() != null) {
            return false;
        }
        return true;
    }
}
