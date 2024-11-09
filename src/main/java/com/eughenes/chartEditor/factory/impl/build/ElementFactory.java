package com.eughenes.chartEditor.factory.impl.build;


import com.eughenes.chartEditor.entity.build.Element;
import com.eughenes.chartEditor.factory.interfaces.BaseFactory;
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
public class ElementFactory implements BaseFactory<List<Element>, Path> {

    public List<Element> create(Path filePath) throws IOException {
        List<Element> elements = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()));
        String line;
        List<String> contentBuilder = new ArrayList<>();
        String currentTitle = null;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("[") && line.contains("]")) {
                //if (currentTitle != null && contentBuilder.length() > 0) {
                if (currentTitle != null && contentBuilder.size() > 0) {
                    elements.add(new Element(currentTitle, contentBuilder));
                    //contentBuilder = new StringBuilder();
                    contentBuilder = new ArrayList<>();
                }
                currentTitle = extractTitle(line);
            } else if (line.startsWith("{") && line.contains("}")) {
                contentBuilder.add(extractContent(line));
            } else if (line.startsWith("{")) {
                contentBuilder.add(line.substring(1));//.append(" \n");
            } else if (line.endsWith("}")) {
                contentBuilder.add(line.substring(0, line.length() - 1));
            } else {
                contentBuilder.add(line);//.append(" \n");
            }
        }

        // Add the last element if exists
        if (currentTitle != null && contentBuilder.size() > 0) {
            elements.add(new Element(currentTitle, contentBuilder));
        }

        reader.close();
        System.out.println("Parsed all elements");
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