package com.alchemist.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.service.CsvService;
import com.alchemist.service.PdfService;

import java.io.ByteArrayInputStream;

@RestController
public class CustomController {

    private final CsvService csvService;
    private final PdfService pdfService;

    public CustomController(CsvService csvService, PdfService pdfService) {
        this.csvService = csvService;
        this.pdfService = pdfService;
    }
    
    @GetMapping("/")
    public String getMessage() {
    	return "Welcom to Spring boot Prod-ready features: Actuators";
    }

    // CSV download
    @GetMapping(value = "/report", produces = "text/csv")
    public ResponseEntity<InputStreamResource> downloadCsv() {
        ByteArrayInputStream bis = csvService.generateCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(new InputStreamResource(bis));
    }

    // PDF download
    @GetMapping(value = "/pdf", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> downloadPdf() {
        ByteArrayInputStream bis = pdfService.generatePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=document.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
