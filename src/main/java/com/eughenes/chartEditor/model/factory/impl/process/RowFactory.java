package com.eughenes.chartEditor.model.factory.impl.process;


import com.eughenes.chartEditor.model.entity.process.Note;
import com.eughenes.chartEditor.model.entity.process.Row;
import com.eughenes.chartEditor.model.entity.process.enums.NoteType;
import com.eughenes.chartEditor.model.factory.interfaces.BaseFactory;
import org.springframework.stereotype.Component;

/**
 * Creates the Row Object starting from the String content of the single line of text
 *
 * @author Eughenes
 */
@Component
public class RowFactory implements BaseFactory<Row, String> {

    public Row create(String line) {
        String[] fullLine = line.split("=");
        String[] singleNote = fullLine[1].trim().split(" ");
        return new Row(Integer.parseInt(fullLine[0].trim()), new Note(determineNoteType(singleNote[0].trim()), Integer.parseInt(singleNote[1].trim()), Integer.parseInt(singleNote[2].trim())));
    }

    private NoteType determineNoteType(String input) {
        if (input.equals("S")) {
            return NoteType.STAR;
        }
        return NoteType.NORMAL;
    }

    private NoteType determineNoteTypeWithFret(String key, int fretNumber) {
        if (key.equals("S")) {
            return NoteType.STAR;
        }
        // N 5 = Forced flag, N 6 = Tap flag — must always be preserved
        if (fretNumber == 5 || fretNumber == 6) {
            return NoteType.FLAG;
        }
        return NoteType.NORMAL;
    }

    public Row createWithFlagDetection(String line) {
        String[] fullLine = line.split("=");
        String[] singleNote = fullLine[1].trim().split(" ");
        String key = singleNote[0].trim();
        int fretNumber = Integer.parseInt(singleNote[1].trim());
        int duration = Integer.parseInt(singleNote[2].trim());
        return new Row(Integer.parseInt(fullLine[0].trim()),
                new Note(determineNoteTypeWithFret(key, fretNumber), fretNumber, duration));
    }
}
