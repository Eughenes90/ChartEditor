package com.eughenes.chartEditor.factory.impl.build;

import com.eughenes.chartEditor.base.BaseComponent;
import com.eughenes.chartEditor.entity.build.Chart;
import com.eughenes.chartEditor.entity.build.Element;
import com.eughenes.chartEditor.entity.build.Song;
import com.eughenes.chartEditor.entity.build.enums.SongDifficulty;
import com.eughenes.chartEditor.factory.interfaces.BaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Creates a Chart reading the input file.
 *
 * @author Eughenes
 */
@Component
public class ChartFactory extends BaseComponent implements BaseFactory<Chart, File> {
    @Autowired
    private ElementFactory elementFactory;

    public Chart create(File inputFile) throws IOException {
        List<Element> elements = elementFactory.create(inputFile.toPath());
        Chart createdChart = elementsIntoChart(elements);
        logInfo("Chart created");
        return createdChart;
    }

    private Chart elementsIntoChart(List<Element> elements) {
        Chart chart = new Chart();

        // Map to associate difficulty strings with the corresponding setter methods
        Map<SongDifficulty, BiConsumer<Song, Element>> difficultyMap = new HashMap<>();
        difficultyMap.put(SongDifficulty.EXPERT, Song::setExpert);
        difficultyMap.put(SongDifficulty.HARD, Song::setHard);
        difficultyMap.put(SongDifficulty.MEDIUM, Song::setMedium);
        difficultyMap.put(SongDifficulty.EASY, Song::setEasy);

        for (Element element : elements) {
            boolean matched = false;
            for (Map.Entry<SongDifficulty, BiConsumer<Song, Element>> entry : difficultyMap.entrySet()) {
                String difficulty = entry.getKey().getTextValue();
                BiConsumer<Song, Element> setter = entry.getValue();

                if (element.getTitle().contains(difficulty)) {
                    String key = element.getTitle().replaceAll(difficulty, "");
                    Song song = chart.getSongHashMap().computeIfAbsent(key, k -> new Song());
                    setter.accept(song, element);
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                chart.getChartElements().add(element);
            }
        }

        return chart;
    }

}
