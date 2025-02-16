package com.eughenes.chartEditor.services;

import com.eughenes.chartEditor.utils.LogManager;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing the files
 *
 * @author Eughenes
 */
@Service
public class FileService {

    public File selectFile() {
        LogManager.logInfo("Selecting File");
        // Select the input file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a .chart file");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Chart Files", "chart", "chart");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public void saveFile(Path originalFilePath, List<String> content) throws IOException {
        // Get the parent directory of the original file
        Path parentDirectory = originalFilePath.getParent();

        // Construct the path for the new file with the same name
        Path newFilePath = parentDirectory.resolve(originalFilePath.getFileName());
        LogManager.logInfo("Processed Chart saved in " + newFilePath);
        // Write the content to the new file
        Files.write(newFilePath, content);
    }

    public void saveFile(Path originalFilePath, List<String> oroginalChart, List<String> newChart) throws IOException {
        // Get the parent directory of the original file
        Path parentDirectory = originalFilePath.getParent();

        // Construct the path for the new file with the same name
        Path backupFilePath = parentDirectory.resolve(originalFilePath.getFileName()+".backup");
        Path newFilePath = parentDirectory.resolve(originalFilePath.getFileName());
        LogManager.logInfo("Processed Chart saved in " + newFilePath);
        // Write the content to the new file
        Files.write(backupFilePath, oroginalChart);
        Files.write(newFilePath, newChart);
    }


    public List<String> readFileContent(File inputFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile.toPath().toString()));
        List<String> fileContent = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            LogManager.logInfo(line);
            fileContent.add(line);
        }
        return fileContent;
    }
}
