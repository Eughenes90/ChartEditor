package com.eughenes.chartEditor.model.factory.impl.build;


import com.eughenes.chartEditor.model.entity.build.Element;
import com.eughenes.chartEditor.model.factory.interfaces.BaseFactory;
import com.eughenes.chartEditor.utils.LogManager;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns the List of Elements starting from the filepath of the inputFile
 *
 * @author Eughenes
 */
@Component
public class ElementFactory implements BaseFactory<List<Element>, List<String>> {

    public List<Element> create(List<String> fileContent){
        List<Element> elements = new ArrayList<>();
        List<String> contentBuilder = new ArrayList<>();
        String currentTitle = null;

        for(String line : fileContent) {
            if (line.contains("[") && line.contains("]")) {
                currentTitle = extractTitle(line);
                contentBuilder = new ArrayList<>();
            } else if (line.startsWith("{") && line.contains("}")) {
                contentBuilder.add(extractContent(line));
            } else if (line.startsWith("{")) {
                contentBuilder.add(line.substring(1));//.append(" \n");
            } else if (line.endsWith("}")) {
                contentBuilder.add(line.substring(0, line.length() - 1));
                elements.add(new Element(currentTitle, contentBuilder));
                currentTitle = null;
                contentBuilder = new ArrayList<>();
            } else {
                contentBuilder.add(line);//.append(" \n");
            }
        }

        LogManager.logInfo("Parsed all elements");
        return elements;
    }

    private String extractTitle(String line) {
        int startIndex = line.indexOf('[');
        int endIndex = line.indexOf(']');
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return line.substring(startIndex + 1, endIndex);
        }
        return null;
    }

    private String extractContent(String line) {
        int startIndex = line.indexOf('{');
        int endIndex = line.indexOf('}');
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return line.substring(startIndex + 1, endIndex);
        }
        return null;
    }


}