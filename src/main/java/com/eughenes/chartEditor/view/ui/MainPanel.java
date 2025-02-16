package com.eughenes.chartEditor.view.ui;

import com.eughenes.chartEditor.controller.MainPanelController;
import com.formdev.flatlaf.FlatLightLaf;
import com.eughenes.chartEditor.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

@Component
public class MainPanel {

    private final MainPanelController mainPanelController;

    private JPanel appPanel;
    private JButton openButton;

    @Autowired
    public MainPanel(MainPanelController mainPanelController) {
        this.mainPanelController = mainPanelController;
    }

    public void createUIComponents() {
        // Set the FlatLaf theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // This applies the Material Design look
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Create a new JFrame
        JFrame frame = new JFrame("Welcome to ChartGenerator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Add the custom JPanel (appPanel) with the material style
        frame.setContentPane(createAppPanel());

        ImageIcon icon = new ImageIcon(MainPanel.class.getResource("/logo.png"));
        frame.setIconImage(icon.getImage());

        // Make the frame visible
        frame.setVisible(true);
    }

    private JPanel createAppPanel() {
        appPanel = new JPanel();
        appPanel.setLayout(new BoxLayout(appPanel, BoxLayout.Y_AXIS));

        // Create an Open button with material style using FlatLaf
        openButton = new JButton("Open");
        openButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the action when the button is clicked
                try {
                    mainPanelController.onOpenButtonClick();
                    JOptionPane.showMessageDialog(null, "Operation Complete");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Operation Failed");
                }
            }
        });

        appPanel.add(openButton);
        appPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        return appPanel;
    }
}
