package com.eughenes.chartEditor.model.entity.build;

import java.util.ArrayList;
import java.util.List;

/**
 * This object contains all the Elements that are recognized as difficulty tracks
 *
 * @author Eughenes
 */
public class Song {
    private Element expert;

    private Element hard;

    private Element medium;

    private Element easy;

    public Song() {
    }

    public Element getExpert() {
        return expert;
    }

    public void setExpert(Element expert) {
        this.expert = expert;
    }

    public Element getHard() {
        return hard;
    }

    public void setHard(Element hard) {
        this.hard = hard;
    }

    public Element getMedium() {
        return medium;
    }

    public void setMedium(Element medium) {
        this.medium = medium;
    }

    public Element getEasy() {
        return easy;
    }

    public void setEasy(Element easy) {
        this.easy = easy;
    }

    public List<String> printSong() {
        List<String> print = new ArrayList<>();
        if (this.expert != null) {
            print.addAll(this.expert.printElement());
        }
        if (this.hard != null) {
            print.addAll(this.hard.printElement());
        }
        if (this.medium != null) {
            print.addAll(this.medium.printElement());
        }
        if (this.easy != null) {
            print.addAll(this.easy.printElement());
        }

        return print;
    }
}
