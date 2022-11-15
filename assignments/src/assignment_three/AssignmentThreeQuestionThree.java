package assignment_three;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Vivek
 * @since 15/11/22
 */
public class AssignmentThreeQuestionThree {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Map<String, Long>> documentAndWordCountMap = generateDocumentAndWordCountMap();

        String keyword;
        while (true) {
            System.out.println("***** Enter the keyword you want to search (case-insensitive) and get rank: (-1 to exit)");
            keyword = in.readLine();
            if ("-1".equals(keyword)) break;

            List<Node> sortedWebPagesOnKeyWordCount = generateSortedWebPagesOnKeyWordCount(keyword, documentAndWordCountMap);
            sortedWebPagesOnKeyWordCount.sort(Comparator.comparing(Node::getWordCountInFile).reversed());
            sortedWebPagesOnKeyWordCount.stream()
                    .filter(node -> node.getWordCountInFile() > 0)
                    .forEach(node -> System.out.printf("%s :: %d%n", node.getHtmlFileName(), node.getWordCountInFile()));
        }
        System.out.println("\nBye");
    }

    private static Map<String, Map<String, Long>> generateDocumentAndWordCountMap() {
        Map<String, Map<String, Long>> documentAndWordCountMap = new HashMap<>();
        File baseFolder = new File("assignments/res/W3C_Web_Pages");

        System.out.println("Initiating reading in of the files from " + baseFolder.getName());
        long timer = System.currentTimeMillis();
        for (File file : Objects.requireNonNull(baseFolder.listFiles())) {
            if (file.isDirectory()) continue;
            Map<String, Long> wordCountMap = computeWordFrequencyFromHtmlFile(file.getAbsolutePath());
            documentAndWordCountMap.put(file.getName(), wordCountMap);
        }
        timer = System.currentTimeMillis() - timer;
        System.out.printf("Read complete and documentAndWordCountMap has been generated in %d ms!%n", timer);
        return documentAndWordCountMap;
    }

    private static List<Node> generateSortedWebPagesOnKeyWordCount(String keyword, Map<String, Map<String, Long>> documentAndWordCountMap) {
        List<Node> sortedWebPagesOnKeyWordCount = new ArrayList<>(documentAndWordCountMap.keySet().size());
        documentAndWordCountMap.forEach((htmlWebPageName, wordCountMap) ->
                sortedWebPagesOnKeyWordCount.add(generateNode(htmlWebPageName, wordCountMap.getOrDefault(keyword, 0L))));
        return sortedWebPagesOnKeyWordCount;
    }

    private static Node generateNode(String htmlFileName, Long wordCount) {
        return new Node(htmlFileName, wordCount);
    }

    /**
     * supervisor function which takes in single html file location and the document text extraction mode and returns a hashtable of word x count
     *
     * @param htmlFileLocation
     * @return
     */
    private static Map<String, Long> computeWordFrequencyFromHtmlFile(String htmlFileLocation) {
        //System.out.println("Going to read html file from location: " + htmlFileLocation);
        String htmlData = readHtmlFile(htmlFileLocation);
        //System.out.println("HTML file read complete, initiating first cleaning operation.");

        String cleanHtmlData = cleanDocumentViaJsoup(htmlData);
        String[] splitHtmlData = cleanHtmlData.split("\\s+");
        //System.out.printf("Obtained %d un-sanitized words from the HTML file post primary cleaning operation %n", splitHtmlData.length);
        final Map<String, Long> keyAndCountMap = new HashMap<>();
        for (int i = 0; i < splitHtmlData.length; i++) { //iterate over the words from the string array and augment count of the hashtable
            String key = sanitizeString(splitHtmlData[i]);
            if (!key.isEmpty()) augmentCount(keyAndCountMap, key);
        }
        //System.out.println("Sanitization complete. Completed generating the word frequency hash-table.");
        //System.out.println("Final unique keys: " + keyAndCountMap.keySet().size());
        return keyAndCountMap; //return the populated hashtable
    }

    /**
     * increases count of the key by 1 in the hashtable
     *
     * @param counterMap
     * @param key
     */
    private static void augmentCount(Map<String, Long> counterMap, String key) {
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

    private static class Node {
        private final String htmlFileName;
        private final Long wordCountInFile;

        public Node(String htmlFileName, Long wordCountInFile) {
            this.htmlFileName = htmlFileName;
            this.wordCountInFile = wordCountInFile;
        }

        public String getHtmlFileName() {
            return htmlFileName;
        }

        public Long getWordCountInFile() {
            return wordCountInFile;
        }
    }
}