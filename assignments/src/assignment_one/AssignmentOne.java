package assignment_one;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import static assignment_one.DocumentTextExtractionMode.REGEX;

/**
 * @author Vivek
 * @since 20/09/22
 */
public class AssignmentOne {

    public static void main(String[] args) {
        String filePath = "assignments/res/W3C_Web_Pages/Accessibility - W3C.htm"; //selecting 1 of the html files for text extraction
        computeWordFrequencyFromHtmlFile(filePath, REGEX); //passing the filepath and the document text extraction mode
    }

    /**
     * supervisor function which takes in single html file location and the document text extraction mode and returns a hashtable of word x count
     *
     * @param htmlFileLocation
     * @param mode
     * @return
     */
    private static Hashtable<String, Long> computeWordFrequencyFromHtmlFile(String htmlFileLocation, DocumentTextExtractionMode mode) {
        System.out.println("Going to read html file from location: " + htmlFileLocation);
        String htmlData = readHtmlFile(htmlFileLocation);
        System.out.println("HTML file read complete, initiating first cleaning operation.");

        String cleanHtmlData;
        switch (mode) { //cleaning data based on the mode passed onto
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
        for (int i = 0; i < splitHtmlData.length; i++) { //iterate over the words from the string array and augment count of the hashtable
            String key = sanitizeString(splitHtmlData[i]);
            if (!key.isEmpty()) augmentCount(keyAndCountTable, key);
        }
        System.out.println("Sanitization complete. Completed generating the word frequency hash-table.");
        System.out.println("Final unique keys: " + keyAndCountTable.keySet().size());
        System.out.println(keyAndCountTable);
        return keyAndCountTable; //return the populated hashtable
    }

    /**
     * increases count of the key by 1 in the hashtable
     *
     * @param counterMap
     * @param key
     */
    private static void augmentCount(Hashtable<String, Long> counterMap, String key) {
        Long value = counterMap.get(key);
        if (value == null) value = 0L;
        counterMap.put(key, value + 1);
    }

    /**
     * sanitize the word, i.e. consider only those words which are having 0-9 or/and a-z or/and A-Z, rest all are ignored
     *
     * @param data
     * @return lowercase version of accepted word else empty
     */
    private static String sanitizeString(String data) {
        if (data.matches("[0-9a-zA-Z]+")) return data.toLowerCase();
        return "";
    }

    /**
     * clean data using jsoup lib
     *
     * @param data
     * @return
     */
    private static String cleanDocumentViaJsoup(String data) {
        return Jsoup.parse(data).text();
    }

    /**
     * clean data using custom regex
     *
     * @param data
     * @return
     */
    private static String cleanDocumentViaRegex(String data) {
        return data
                .replaceAll("<(.|\f|\r|\n)*?>", " ")
                .replaceAll("\\s+", " ");
    }

    /**
     * Helper method to read the actual html file from the filesystem based on the path of the file to be read
     *
     * @param filePath
     * @return the text values of the html file in a single string
     */
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