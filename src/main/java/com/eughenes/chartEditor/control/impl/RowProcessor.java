package com.eughenes.chartEditor.control.impl;

import com.eughenes.chartEditor.base.BaseComponent;
import com.eughenes.chartEditor.control.interfaces.BaseProcessor;
import com.eughenes.chartEditor.entity.process.Row;
import com.eughenes.chartEditor.entity.process.enums.NoteType;
import com.eughenes.chartEditor.factory.impl.process.RowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This will process every single row in order to fit the timeout between one and the other
 * according to the difficulty
 *
 * @author Eughenes
 */
@Component
public class RowProcessor extends BaseComponent implements BaseProcessor<List<String>> {

    @Autowired
    private RowFactory rowFactory;
    public List<String> process(List<String> lines, Object... params) {
        Integer minTimeDistance = (Integer) params[0];
        Integer maximumNotes = (Integer) params[1];

        List<String> transformedLines = new ArrayList<>();
        HashMap<Integer, Integer> lastTimes = new HashMap<>();
        for (String line : lines) {
            if (Objects.equals(line, ""))
                continue;

            if (!lineNeedsToBeProcessed(line)) {
                transformedLines.add(line);
                continue;
            }

            Row currentRow = rowFactory.create(line);
            if (!currentRow.getNote().getNoteType().equals(NoteType.NORMAL)) {
                transformedLines.add(line);
                continue;
            }

            if (isNoteExpectedForDifficulty(maximumNotes, currentRow.getNote().getNoteNumber()) &&
                    isNoteWithinRange(minTimeDistance, lastTimes.get(currentRow.getNote().getNoteNumber()), currentRow.getTime())) {

                transformedLines.add(line);
                lastTimes.put(currentRow.getNote().getNoteNumber(), currentRow.getTime() + currentRow.getNote().getNoteDuration());

            }
        }
        return transformedLines;
    }

    private boolean isNoteWithinRange(int minTimeDistance, Integer lastTime, int currentTime) {
        return lastTime == null || currentTime - lastTime >= minTimeDistance;
    }

    private boolean isNoteExpectedForDifficulty(int maximumNotes, Integer noteNumber) {
        return noteNumber < maximumNotes;
    }

    private boolean lineNeedsToBeProcessed(String line) {
        return !line.contains("{") && !line.contains("}") && !line.contains("solo");
    }

}
