package com.eughenes.chartEditor.factory.impl.process;

import com.eughenes.chartEditor.base.BaseComponent;
import com.eughenes.chartEditor.entity.process.Note;
import com.eughenes.chartEditor.entity.process.Row;
import com.eughenes.chartEditor.entity.process.enums.NoteType;
import com.eughenes.chartEditor.factory.interfaces.BaseFactory;
import org.springframework.stereotype.Component;

/**
 * Creates the Row Object starting from the String content of the single line of text
 *
 * @author Eughenes
 */
@Component
public class RowFactory extends BaseComponent implements BaseFactory<Row, String> {

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
}
