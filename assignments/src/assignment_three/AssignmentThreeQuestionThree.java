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
        Map<String, Map<String, Long>> documentAndWordCountMap = generateDocumentAndWordCountMap(); //stores a map of the document name vs a map of word and count of it in the document

        String keyword;
        while (true) {
            System.out.println("***** Enter the keyword you want to search (case-insensitive) and get rank: (-1 to exit)");
            keyword = in.readLine(); //read user input to take the word to search for
            if ("-1".equals(keyword)) break;

            List<Node> webPageRankOnKeyWordCount = generateWebPageRankOnKeyWordCount(keyword, documentAndWordCountMap); //generate web page rank based on the keyword count in that file
            webPageRankOnKeyWordCount.sort(Comparator.comparing(Node::getWordCountInFile).reversed()); //sort the list on the basis of the word count in the node, and then reverse it, so that the list is sorted from highest rank to lowest
            webPageRankOnKeyWordCount.stream() //stream through the sorted list
                    .filter(node -> node.getWordCountInFile() > 0) //filter out the nodes having count > 0
                    .forEach(node -> System.out.printf("%s :: %d%n", node.getHtmlFileName(), node.getWordCountInFile())); //print the web page and the keyword count in that webpage, in highest to lowest rank order
        }
        System.out.println("\nBye");
    }

    /**
     * Generate the Document and Word Count map which serves as the common lookup for any input keyword.
     * This reads each and every html file present in the resource and generates the document vs word count map
     *
     * @return Map<String, Map < String, Long>> documentAndWordCountMap
     */
    private static Map<String, Map<String, Long>> generateDocumentAndWordCountMap() {
        Map<String, Map<String, Long>> documentAndWordCountMap = new HashMap<>();
        File baseFolder = new File("assignments/res/W3C_Web_Pages"); //base folder location of the web pages

        System.out.println("Initiating reading in of the files from " + baseFolder.getName());
        long timer = System.currentTimeMillis(); //timing the read and compute operation
        for (File file : Objects.requireNonNull(baseFolder.listFiles())) { //iterate through the files in the folder
            if (file.isDirectory()) continue;
            Map<String, Long> wordCountMap = computeWordFrequencyFromHtmlFile(file.getAbsolutePath()); //compute the word frequency from the html file being worked upon
            documentAndWordCountMap.put(file.getName(), wordCountMap); //update the documentAndWordCountMap with the current web page name and the computed word freq map
        }
        timer = System.currentTimeMillis() - timer; //timing the read and compute operation
        System.out.printf("Read complete and documentAndWordCountMap has been generated in %d ms!%n", timer); //printing the time taken to run the compute
        return documentAndWordCountMap;
    }

    /**
     * Generates the web page rank based on the keyword count
     *
     * @param keyword:                 the string to be searched
     * @param documentAndWordCountMap: the already generated document and word count map on which the keywords needs to be searched
     * @return List <Node> containing the list of web page name and the count of the keyword in that web page
     */
    private static List<Node> generateWebPageRankOnKeyWordCount(String keyword, Map<String, Map<String, Long>> documentAndWordCountMap) {
        List<Node> sortedWebPagesOnKeyWordCount = new ArrayList<>(documentAndWordCountMap.keySet().size()); //initialize an array list with size of the keyset of the documentAndWordCountMap
        documentAndWordCountMap.forEach((htmlWebPageName, wordCountMap)  //iterate over the documentAndWordCountMap in entry format - key x value
                -> sortedWebPagesOnKeyWordCount.add(generateNode(htmlWebPageName, wordCountMap.getOrDefault(keyword, 0L)))); // perform multiple operations at once
        //for each web page, get the count of the keyword in the wordcount map associated to that web page
        //then create a Node by passing the name of the web page and the count of the word in that page
        //then in the same line add this new Node into the list of node created above
        return sortedWebPagesOnKeyWordCount;
    }

    /**
     * Helper function to generate an object of class Node
     *
     * @param htmlFileName: the web page for which this Node is created
     * @param wordCount:    the word count of the keyword in the htmlFileName
     * @return a Node object containing the values
     */
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
        String htmlData = readHtmlFile(htmlFileLocation);
        String cleanHtmlData = cleanDocumentViaJsoup(htmlData);
        String[] splitHtmlData = cleanHtmlData.split("\\s+");
        final Map<String, Long> keyAndCountMap = new HashMap<>();
        for (int i = 0; i < splitHtmlData.length; i++) { //iterate over the words from the string array and augment count of the hashtable
            String key = sanitizeString(splitHtmlData[i]);
            if (!key.isEmpty()) augmentCount(keyAndCountMap, key);
        }
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

    /**
     * Helper Class which holds the html file name and the count of the keyword in this html file name
     */
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