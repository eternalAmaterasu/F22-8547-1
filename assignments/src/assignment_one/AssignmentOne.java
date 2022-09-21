package assignment_one;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

/**
 * @author Vivek
 * @since 20/09/22
 */
public class AssignmentOne {

    public static void main(String[] args) {
        String filePath = "assignments/res/W3C_Web_Pages/Accessibility - W3C.htm";
        computeWordFrequencyFromHtmlFile(filePath);
    }

    private static Hashtable<String, Long> computeWordFrequencyFromHtmlFile(String htmlFileLocation) {
        System.out.println("Going to read html file from location: " + htmlFileLocation);
        String htmlData = readHtmlFile(htmlFileLocation);
        System.out.println("HTML file read complete, initiating first cleaning operation.");
        //System.out.println(cleanDocumentViaJsoup(htmlData));
        //System.out.println("********");
        //System.out.println(cleanDocumentViaRegex(htmlData));

        String[] splitHtmlData = cleanDocumentViaRegex(htmlData).split("\\s+");
        System.out.printf("Obtained %d un-sanitized words from the HTML file post primary cleaning operation %n", splitHtmlData.length);
        Hashtable<String, Long> keyAndCountTable = new Hashtable<>();
        for (int i = 0; i < splitHtmlData.length; i++) {
            String key = sanitizeString(splitHtmlData[i]);
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

    private static String sanitizeString(String data) {
        if (data.matches("[0-9a-zA-Z]+")) return data.toLowerCase();
        return "";
    }

    private static String cleanDocumentViaJsoup(String data) {
        return Jsoup.parse(data).text();
    }

    private static String cleanDocumentViaRegex(String data) {
        return data
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
        } catch (IOException e) {
            System.out.printf("Encountered error while reading the file at '%s': '%s'%n", filePath, e);
        }
        return dataLines.toString();
    }
}