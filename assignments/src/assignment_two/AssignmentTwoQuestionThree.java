package assignment_two;

import algorithmDesign.Sequences;
import assignment_one.DocumentTextExtractionMode;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;

import static assignment_one.DocumentTextExtractionMode.REGEX;


/**
 * @author Vivek
 * @since 22/10/22
 */
public class AssignmentTwoQuestionThree {
    private static final Random RANDOM = new Random();

    /**
     * Inner class to compute the overall score and smallestDistanceWords.
     * This is the overall output of the @link{AssignmentTwoQuestionThree.computeScoreAndSmallestEditDistanceWords}
     * <p>
     * score - the variable which stores the overall score of all the 10 smallest edit distances for all the query words
     * smallestDistanceWords - a linked hash map of the query word x EditDistanceNode, in order of insertion
     */
    public static class ScoreAndSmallestDistanceWords {
        private long score;
        private final LinkedHashMap<String, List<EditDistanceNode>> smallestDistanceWords;

        public ScoreAndSmallestDistanceWords() {
            this.score = 0L;
            this.smallestDistanceWords = new LinkedHashMap<>();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", ScoreAndSmallestDistanceWords.class.getSimpleName() + "[", "]").add("score=" + score).add("smallestDistanceWords=" + smallestDistanceWords).toString();
        }

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }

        public Map<String, List<EditDistanceNode>> getSmallestDistanceWords() {
            return smallestDistanceWords;
        }

        public void updateSmallestDistanceWords(String word, List<EditDistanceNode> editDistanceNodes) {
            this.smallestDistanceWords.put(word, editDistanceNodes);
        }
    }

    /**
     * Inner class to store the distance of the word being compared for edit distance along with the target word
     * distance - the variable which stores the edit distance of the query word with target word
     * word - the target word against which the query word was measured for edit distance
     */
    private static class EditDistanceNode {
        private final int distance;
        private final String word;

        public EditDistanceNode(int distance, String word) {
            this.distance = distance;
            this.word = word;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", EditDistanceNode.class.getSimpleName() + "[", "]").add("distance=" + distance).add("word='" + word + "'").toString();
        }

        public int getDistance() {
            return distance;
        }

        public String getWord() {
            return word;
        }
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
     * Function to get sanitized meaningful words from the html file being read in as input
     *
     * @param htmlFileLocation: the file location to read from local system
     * @param mode:             Mode of cleaning the data
     * @return List of sanitized words from the html file
     */
    private static List<String> getWordsFromHtmlFile(String htmlFileLocation, DocumentTextExtractionMode mode) {
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
        List<String> finalizedWordsFromFile = new ArrayList<>();
        for (int i = 0; i < splitHtmlData.length; i++) { //iterate over the words from the string array and augment count of the hashtable
            String key = sanitizeString(splitHtmlData[i]);
            if (!key.isEmpty()) finalizedWordsFromFile.add(key);
        }
        return finalizedWordsFromFile;
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
        return data.replaceAll("<(.|\f|\r|\n)*?>", " ").replaceAll("\\s+", " ");
    }

    /**
     * Main function of this question which is to compute the overall score and compute the 10 edit distances and store the words against the query words
     *
     * @param queryWords:          the words for which the score and edit distance is to be calculated
     * @param htmlWebPageLocation: the html web page location on the local file system which needs to be read and input
     * @return ScoreAndSmallestDistanceWords object containing the overall score and a map of the query word x 10 EditDistanceNode [which contains the target word and distance of the query word with target word]
     */
    private static ScoreAndSmallestDistanceWords computeScoreAndSmallestEditDistanceWords(List<String> queryWords, String htmlWebPageLocation) {
        ScoreAndSmallestDistanceWords scoreAndSmallestDistanceWords = new ScoreAndSmallestDistanceWords(); //declaring the result set
        LinkedHashSet<String> uniqueQueryWords = new LinkedHashSet<>(queryWords); //transforming the input list of query words to unique set of query words as a set, as it doesn't make sense to re-compute the entire process for the same word again and again
        List<String> wordsFromHtmlFile = getWordsFromHtmlFile(htmlWebPageLocation, REGEX); //extract sanitized words from the html file
        Set<String> uniqueWordsFromHtmlFile = new HashSet<>(wordsFromHtmlFile); //make sure the words from the html file are now unique
        long score = 0L;

        for (String queryWord : uniqueQueryWords) { //iterate over the unique query words
            List<EditDistanceNode> editDistanceScoreForQueryWordAndHtmlWord = new ArrayList<>(uniqueQueryWords.size()); //declare list of EditDistanceNode type
            uniqueWordsFromHtmlFile.forEach(htmlWord -> { //iterate over all the unique words from html file and compare the query word with this target word
                int distance = Sequences.editDistance(queryWord, htmlWord); //computed edit distance
                editDistanceScoreForQueryWordAndHtmlWord.add(new EditDistanceNode(distance, htmlWord)); //add the computed distance and the word to the list
            });
            editDistanceScoreForQueryWordAndHtmlWord.sort(Comparator.comparing(EditDistanceNode::getDistance)); //Sort the above list on the basis of the edit distance.
            //This'll get us the list of the edit distance node sorted on the basis of non-decreasing distance

            int upperLimit = Math.min(editDistanceScoreForQueryWordAndHtmlWord.size(), 10); //decide the upper limit for the list. we need to select smallest 10, but if the list size is smaller, that'll lead to null pointer
            List<EditDistanceNode> editDistanceNodes = new ArrayList<>(upperLimit); //declare another list of edit distance which'll store the final selected smallest 10 edit distance node
            for (int i = 0; i < upperLimit; i++) {
                score += editDistanceScoreForQueryWordAndHtmlWord.get(i).getDistance(); //add the distance to the score as this is one of the 10 smallest edit distance
                editDistanceNodes.add(editDistanceScoreForQueryWordAndHtmlWord.get(i)); //add the edit distance node to the new list
            }
            //at this point we are done with extracting the info
            scoreAndSmallestDistanceWords.updateSmallestDistanceWords(queryWord, editDistanceNodes); //push the query word x new list to the LinkedHashMap created inside of scoreAndSmallestDistanceWords object
        }
        scoreAndSmallestDistanceWords.setScore(score); //finally having summed up all the edit distances of 10 smallest edit distances of all keywords, set the score in the scoreAndSmallestDistanceWords object
        return scoreAndSmallestDistanceWords; //return the object which is now acting as the parcel of the info computed
    }

    /**
     * Function to generate random string of length 'length'
     *
     * @param length
     * @return randomly generated string
     */
    private static String generateRandomString(int length) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < length; i++) data.append((char) ('a' + RANDOM.nextInt(26)));
        return data.toString();
    }

    public static void main(String[] args) {
        String filePath = "assignments/res/W3C_Web_Pages/Accessibility - W3C.htm"; //selecting 1 of the html files for text extraction
        List<String> queryWords = new ArrayList<>(); //populating the query words
        for (int i = 1; i <= 51; i++) queryWords.add(generateRandomString(11)); // randomly generating 51 query strings of length 11
        queryWords.add("abcdef"); //hard-coding last query string

        ScoreAndSmallestDistanceWords scoreAndSmallestDistanceWords = computeScoreAndSmallestEditDistanceWords(queryWords, filePath); //pass the query words and the html file location to the function and store the result of the computation
        System.out.printf("The score from the computation is : %d%n", scoreAndSmallestDistanceWords.getScore()); //display the score calculated from the operation
        scoreAndSmallestDistanceWords.getSmallestDistanceWords().forEach((key, value) -> { //iterate over and print each of the query word and their 10 smallest edit distance
            System.out.printf("Showing smallest %d edit distances for the word: %s%n", value.size(), key);
            value.forEach(editDistanceNode -> System.out.println("\t" + editDistanceNode)); //display the smallest 10 edit distance word along with their distance for each of the query word
        });
    }
}