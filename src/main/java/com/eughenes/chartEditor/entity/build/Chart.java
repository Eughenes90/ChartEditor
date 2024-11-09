package com.eughenes.chartEditor.entity.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main Object that contains the entire file structure
 * it has a list of generic elements, plus an hasmap of the elements recognized as difficulties
 *
 * @author Eughenes
 */
public class Chart {
    private List<Element> chartElements;

    private HashMap<String, Song> songHashMap;

    public Chart() {
        this.chartElements = new ArrayList<>();
        this.songHashMap = new HashMap<>();
    }

    public List<Element> getChartElements() {
        return chartElements;
    }


    public HashMap<String, Song> getSongHashMap() {
        return songHashMap;
    }

    public List<String> printChart() {
        List<String> print = new ArrayList<>();
        for (Element e : chartElements) {
            print.addAll(e.printElement());
        }
        for (String k : this.songHashMap.keySet()) {
            print.addAll(this.songHashMap.get(k).printSong());
        }
        return print;
    }

}
