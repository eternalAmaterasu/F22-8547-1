package org.uwindsor.mac.f22.acc.compute_engine.engine.parser;

import org.junit.jupiter.api.Test;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SkyScannerTest {
    @Test
    void testParseSkyScannerData() {
        StringBuilder data = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/resources/SkyScannerData2.html"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                data.append(line).append("\n");
            }
        } catch (IOException ignored) {
        }

        List<SearchResponse> searchResponses = SkyScannerParser.parseSkyScannerData(data.toString());
        //System.out.println(searchResponses);
    }
}
