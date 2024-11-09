package com.eughenes.chartEditor.entity.process;

import com.eughenes.chartEditor.entity.process.enums.NoteType;

/**
 * Single element inside the row
 *
 * @author Eughenes
 */
public class Note {
    private NoteType noteType;
    private Integer noteNumber;

    private Integer noteDuration;

    public Note(NoteType noteType, Integer noteNumber, Integer noteDuration) {
        this.noteType = noteType;
        this.noteNumber = noteNumber;
        this.noteDuration = noteDuration;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public Integer getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(Integer noteNumber) {
        this.noteNumber = noteNumber;
    }

    public Integer getNoteDuration() {
        return noteDuration;
    }

    public void setNoteDuration(Integer noteDuration) {
        this.noteDuration = noteDuration;
    }
}
