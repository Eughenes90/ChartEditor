package com.eughenes.chartEditor.services.processors.impl;


import com.eughenes.chartEditor.services.processors.interfaces.BaseProcessor;
import com.eughenes.chartEditor.model.entity.process.Row;
import com.eughenes.chartEditor.model.entity.process.enums.NoteType;
import com.eughenes.chartEditor.model.factory.impl.process.RowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This will process every single row in order to fit the timeout between one and the other
 * according to the difficulty.
 *
 * Improvements:
 * - Flags (Forced N5, Tap N6) are always preserved
 * - Notes are remapped to allowed lanes for the difficulty (e.g. Easy only uses lanes 0-2)
 * - Per-lane minimum time distance prevents same-button spam
 * - A global minimum distance prevents overall note density from being too high
 *
 * params[0] = minTimeDistance (int) — minimum ticks between any two notes
 * params[1] = maximumLanes   (int) — how many lanes are available (3=Easy, 4=Medium, 5=Hard)
 *
 * @author Eughenes
 */
@Component
public class RowProcessor implements BaseProcessor<List<String>> {

    private final RowFactory rowFactory;

    @Autowired
    public RowProcessor(RowFactory rowFactory) {
        this.rowFactory = rowFactory;
    }

    public List<String> process(List<String> lines, Object... params) {
        int minTimeDistance = (Integer) params[0];
        int maximumLanes = (Integer) params[1];

        List<String> transformedLines = new ArrayList<>();

        // Track last accepted time per remapped lane to prevent same-button spam
        int[] lastTimePerLane = new int[maximumLanes];
        for (int i = 0; i < maximumLanes; i++) lastTimePerLane[i] = -1;

        // Track last accepted time globally to avoid overall note density
        int lastGlobalTime = -1;
        // Global gap is half the per-lane gap, prevents bursts of different notes
        int minGlobalDistance = minTimeDistance / 2;

        for (String line : lines) {
            if (Objects.equals(line, ""))
                continue;

            if (!lineNeedsToBeProcessed(line)) {
                transformedLines.add(line);
                continue;
            }

            Row currentRow = rowFactory.createWithFlagDetection(line);
            NoteType noteType = currentRow.getNote().getNoteType();

            // FLAGS (Forced N5, Tap N6): always preserve, they modify other notes
            if (noteType.equals(NoteType.FLAG)) {
                transformedLines.add(line);
                continue;
            }

            // STAR POWER (S key): always preserve
            if (noteType.equals(NoteType.STAR)) {
                transformedLines.add(line);
                continue;
            }

            // OPEN notes (fret 7): preserve if within global time range
            int fret = currentRow.getNote().getNoteNumber();
            if (fret == 7) {
                if (isWithinRange(minGlobalDistance, lastGlobalTime, currentRow.getTime())) {
                    transformedLines.add(line);
                    lastGlobalTime = currentRow.getTime();
                }
                continue;
            }

            // NORMAL notes: remap lane to fit within allowed lanes, then apply spacing checks
            int remappedLane = fret % maximumLanes;
            int currentTime = currentRow.getTime();

            boolean laneOk = isWithinRange(minTimeDistance, lastTimePerLane[remappedLane], currentTime);
            boolean globalOk = isWithinRange(minGlobalDistance, lastGlobalTime, currentTime);

            if (laneOk && globalOk) {
                // Rewrite the line with the remapped lane number
                String remappedLine = rewriteLane(line, fret, remappedLane);
                transformedLines.add(remappedLine);
                lastTimePerLane[remappedLane] = currentTime + currentRow.getNote().getNoteDuration();
                lastGlobalTime = currentTime;
            }
        }
        return transformedLines;
    }

    /**
     * Rewrites the lane number in a raw chart line.
     * e.g. "  768 = N 3 0" with fret=3, remapped=1 → "  768 = N 1 0"
     */
    private String rewriteLane(String line, int originalFret, int remappedFret) {
        if (originalFret == remappedFret) return line;
        // Replace only the first occurrence of the fret number after "N "
        return line.replaceFirst("(N\\s+)" + originalFret, "$1" + remappedFret);
    }

    private boolean isWithinRange(int minDistance, int lastTime, int currentTime) {
        return lastTime < 0 || currentTime - lastTime >= minDistance;
    }

    private boolean lineNeedsToBeProcessed(String line) {
        return !line.contains("{") && !line.contains("}") && !line.contains("solo");
    }

}
