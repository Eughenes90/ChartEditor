package com.eughenes.chartEditor.model.entity.build.enums;

/**
 * Difficulty levels supported
 *
 * @author Eughenes
 */
public enum SongDifficulty {
    EXPERT("Expert"),
    HARD("Hard"),
    MEDIUM("Medium"),
    EASY("Easy");

    private String textValue;

    SongDifficulty(String textValue) {
        this.textValue = textValue;
    }

    public String getTextValue() {
        return textValue;
    }
}
