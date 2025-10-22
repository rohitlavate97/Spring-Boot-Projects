package com.alchemist.service;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Service
public class CsvService {

    public ByteArrayInputStream generateCsv() {
        String[] headers = {"Name", "Email", "Age"};
        String[][] data = {{"John", "john@email.com", "30"},
                           {"Alice", "alice@email.com", "25"}};

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            // Write headers
            writer.append(String.join(",", headers)).append("\n");

            // Write data rows
            for (String[] row : data) {
                writer.append(String.join(",", row)).append("\n");
            }
            writer.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }
}
