package com.eughenes.chartEditor.control.impl;


import com.eughenes.chartEditor.control.interfaces.BaseProcessor;
import com.eughenes.chartEditor.entity.build.Chart;
import com.eughenes.chartEditor.entity.build.Element;
import com.eughenes.chartEditor.entity.build.Song;
import com.eughenes.chartEditor.entity.build.enums.SongDifficulty;
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
    public ChartProcessor(RowProcessor rowProcessor){
        this.rowProcessor = rowProcessor;
    }

    @Override
    public Chart process(Chart chart, Object... params) {
        for (String key : chart.getSongHashMap().keySet()) {
            Song thisSong = chart.getSongHashMap().get(key);
            while (mustBeProcessed(thisSong)) {
                if (thisSong.getHard() == null && thisSong.getExpert() != null) {
                    thisSong.setHard(new Element(SongDifficulty.HARD.getTextValue() + key, rowProcessor.process(thisSong.getExpert().getContent(), 500, 5)));
                    System.out.println("Processed Difficulty Hard");
                } else if (thisSong.getMedium() == null && thisSong.getHard() != null) {
                    thisSong.setMedium(new Element(SongDifficulty.MEDIUM.getTextValue() + key, rowProcessor.process(thisSong.getHard().getContent(), 500, 4)));
                    System.out.println("Processed Difficulty Medium");
                } else if (thisSong.getEasy() == null && thisSong.getMedium() != null) {
                    thisSong.setEasy(new Element(SongDifficulty.EASY.getTextValue() + key, rowProcessor.process(thisSong.getMedium().getContent(), 500, 3)));
                    System.out.println("Processed Difficulty Easy");
                }
            }
            chart.getSongHashMap().put(key, thisSong);
        }
        System.out.println("Chart Processed");
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
