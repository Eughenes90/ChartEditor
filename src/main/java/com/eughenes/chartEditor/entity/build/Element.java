package com.eughenes.chartEditor.entity.build;

import java.util.ArrayList;
import java.util.List;

/**
 * The element is a block with a title between [] and comprised between {}
 * This is a generic reference since we have elements that are not difficulties
 * and that will be rewritten without any change
 *
 * @author Eughenes
 */
public class Element {
    private String title;
    private List<String> content;

    public Element(String title, List<String> content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public List<String> printElement() {
        List<String> print = new ArrayList<>();
        print.add("[" + title + "]");
        print.add("{");
        for (String c : content) {
            if (!c.isBlank()) {
                print.add(c);
            }
        }
        print.add("}");
        print.add("");
        return print;
    }

    @Override
    public String toString() {
        return "Element{" +
                "title='" + title + '\'' +
                ", content=" + content +
                '}';
    }
}
