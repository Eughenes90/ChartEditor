package com.eughenes.chartEditor.view.file;

import com.eughenes.chartEditor.base.BaseComponent;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Handles reading and writing the files
 *
 * @author Eughenes
 */
@Component
public class FileHandler extends BaseComponent {
    public File selectFile() {
        logInfo("Selecting File");
        // Select the input file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a .chart file to transform");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Chart Files", "chart", "chart");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            logInfo("Selected chart from " + fileChooser.getSelectedFile().getAbsolutePath());
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public void saveFile(Path originalFilePath, List<String> content) throws IOException {
        // Get the parent directory of the original file
        Path parentDirectory = originalFilePath.getParent();

        // Construct the path for the new file with the same name
        Path newFilePath = parentDirectory.resolve(originalFilePath.getFileName());
        logInfo("Processed Chart saved in " + newFilePath);
        // Write the content to the new file
        Files.write(newFilePath, content);
    }
}
