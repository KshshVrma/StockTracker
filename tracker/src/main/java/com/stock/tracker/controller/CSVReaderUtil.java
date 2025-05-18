package com.stock.tracker.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;

public class CSVReaderUtil {

    public static List<String> readSymbolsFromCSV(String filename) {
        List<String> symbols = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new ClassPathResource(filename).getInputStream()));
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { // skip header
                    firstLine = false;
                    continue;
                }
                symbols.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return symbols;
    }
}
