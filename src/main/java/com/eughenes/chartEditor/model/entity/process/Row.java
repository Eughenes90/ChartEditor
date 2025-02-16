package com.eughenes.chartEditor.model.entity.process;

/**
 * Row element and time component
 *
 * @author Eughenes
 */
public class Row {
    private Integer time;
    private Note note;

    public Row(Integer time, Note note) {
        this.time = time;
        this.note = note;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
