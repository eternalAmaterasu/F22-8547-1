package engine;

import org.jsoup.Jsoup;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import static engine.DocumentTextExtractionMode.REGEX;

public class Compute_Frequency {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\HP\\eclipse-workspace\\PageRank\\res\\kayak-data.html"; //selecting 1 of the html files for text extraction
        computeWordFrequency(filePath, REGEX); 
    }

  
    static Hashtable<String, Long> computeWordFrequency(String htmlFileLocation, DocumentTextExtractionMode mode) {
        System.out.println("Reading html file from location: " + htmlFileLocation);
        String htmlData = readHtmlFile(htmlFileLocation);
        System.out.println("HTML file read complete");

        String cleanHtmlData;
        switch (mode) { 
            case REGEX:
                cleanHtmlData = cleanDocumentViaRegex(htmlData);
                break;
            case JSOUP:
            default:
                cleanHtmlData = cleanDocumentViaJsoup(htmlData);
        }
        String[] splitHtmlData = cleanHtmlData.split("\\s+");
        System.out.printf("Obtained %d un-sanitized words from the HTML file post primary cleaning operation %n", splitHtmlData.length);
        final Hashtable<String, Long> keyAndCountTable = new Hashtable<>();
        for (int j = 0; j < splitHtmlData.length; j++) { 
            String key = sanitizeString(splitHtmlData[j]);
            if (!key.isEmpty()) augmentCount(keyAndCountTable, key);
        }
        System.out.println("Sanitization complete. Completed generating the word frequency hash-table.");
        System.out.println("Final unique keys: " + keyAndCountTable.keySet().size());
        System.out.println(keyAndCountTable);
        return keyAndCountTable; 
    }

   
    private static void augmentCount(Hashtable<String, Long> counterMap, String key) {
        Long value = counterMap.get(key);
        if (value == null) value = 0L;
        counterMap.put(key, value + 1);
    }

    
    static String sanitizeString(String Data) {
        if (Data.matches("[0-9a-zA-Z]+")) return Data.toLowerCase();
        return "";
    }

   
    private static String cleanDocumentViaJsoup(String Data) {
        return Jsoup.parse(Data).text();
    }

   
    private static String cleanDocumentViaRegex(String Data) {
        return Data
                .replaceAll("<(.|\f|\r|\n)*?>", " ")
                .replaceAll("\\s+", " ");
    }

   
    private static String readHtmlFile(String filePath) {
        StringBuilder dataLines = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) dataLines.append(line);
            }
        } catch (IOException I) {
            System.out.printf("Error reading the file at '%s': '%s'%n", filePath, I);
        }
        return dataLines.toString();
    }
}