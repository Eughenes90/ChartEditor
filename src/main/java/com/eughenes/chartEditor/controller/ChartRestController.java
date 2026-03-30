package com.eughenes.chartEditor.controller;

import com.eughenes.chartEditor.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API for the web UI.
 *
 * POST /api/preview  — upload + process, returns preview as JSON
 * GET  /api/download — returns the last processed chart as a downloadable file
 *
 * @author Eughenes
 */
@RestController
@RequestMapping("/api")
public class ChartRestController {

    private final ChartService chartService;

    // In-memory store for the last processed result (single-user local tool)
    private List<String> lastProcessedChart;
    private String lastFileName;

    @Autowired
    public ChartRestController(ChartService chartService) {
        this.chartService = chartService;
    }

    /**
     * Accepts a .chart file upload + difficulty parameters,
     * processes it and returns a JSON preview.
     */
    @PostMapping("/preview")
    public ResponseEntity<Map<String, Object>> preview(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "hardBeats",   defaultValue = "0.5") double hardBeats,
            @RequestParam(value = "mediumBeats", defaultValue = "1.0") double mediumBeats,
            @RequestParam(value = "easyBeats",   defaultValue = "2.0") double easyBeats
    ) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("error", "No file uploaded");
            return ResponseEntity.badRequest().body(response);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.endsWith(".chart")) {
            response.put("error", "Only .chart files are supported");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            List<String> lines = Arrays.asList(content.split("\\r?\\n"));

            lastProcessedChart = chartService.getProcessedChart(lines, hardBeats, mediumBeats, easyBeats);
            lastFileName = originalFilename;

            // Build a short preview (first 120 lines) to avoid sending megabytes to the browser
            int previewLimit = Math.min(120, lastProcessedChart.size());
            String preview = String.join("\n", lastProcessedChart.subList(0, previewLimit));
            if (lastProcessedChart.size() > previewLimit) {
                preview += "\n\n... (" + (lastProcessedChart.size() - previewLimit) + " more lines)";
            }

            response.put("preview", preview);
            response.put("totalLines", lastProcessedChart.size());
            response.put("fileName", lastFileName);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "Failed to read file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        } catch (Exception e) {
            response.put("error", "Processing failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Returns the last processed chart as a downloadable .chart file.
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> download() {
        if (lastProcessedChart == null) {
            return ResponseEntity.notFound().build();
        }

        String content = String.join("\r\n", lastProcessedChart);
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + lastFileName + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }
}
