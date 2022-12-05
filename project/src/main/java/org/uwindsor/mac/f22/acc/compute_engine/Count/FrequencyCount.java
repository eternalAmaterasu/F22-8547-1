package org.uwindsor.mac.f22.acc.compute_engine.Count;

import java.io.*;
import java.util.*;
//import java.nio.charset.StandardCharsets;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;


public class FrequencyCount {

    public static void Frequency(String ar) throws IOException {


        Map<String, Word> countMap = new HashMap<String, Word>();
        //ArrayList<String> urls = new ArrayList<String>();
        //urls.add("https://www.ca.kayak.com/flights/YQG-YUL/2022-12-21/2adults?sort=bestflight_a");
        //urls.add("https://www.skyscanner.ca/?&utm_source=google&utm_medium=cpc&utm_campaign=CA-Travel-Search-Brand-Skyscanner-Exact&utm_term=skyscanner&associateID=SEM_GGT_19370_00041&gclid=CjwKCAiAhKycBhAQEiwAgf19eq5XKvfzv8t2rd8_V6-UJJrJ61J1zP9KB5nI9gA__BYo7YuVY2m9lRoCVV0QAvD_BwE&gclsrc=aw.ds");

        System.out.println("****Reading the Web Pages****");
        //for(int k=0; k< urls.size(); k++)

        //Document doc = Jsoup.connect(urls.get(k)).get();

        // String text = ar;
        File text = new File(ar);

        System.out.println("Computing Frequency for:");
        // BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
        BufferedReader reader = new BufferedReader(new FileReader(text));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("[^A-Za-z]+");
            for (String word : words) {
                if ("".equals(word)) {
                    continue;
                }

                Word wordObj = countMap.get(word);
                if (wordObj == null) {
                    wordObj = new Word();
                    wordObj.word = word;
                    wordObj.frequency = 0;
                    countMap.put(word, wordObj);
                }

                wordObj.frequency++;
            }



            SortedSet<Word> sortedWords = new TreeSet<Word>(countMap.values());
            int i = 0;
            int maxWordsToDisplay = 25;

            String[] wordsToIgnore = {"the", "and", "a"};

            for (Word word : sortedWords) {
                if (i >= maxWordsToDisplay) {
                    break;
                }

                if (Arrays.asList(wordsToIgnore).contains(word.word)) {
                    i++;
                    maxWordsToDisplay++;
                } else {
                    System.out.println(word.frequency + "\t" + word.word);
                    i++;
                }

            }

        }
        reader.close();




    }

    public static class Word implements Comparable<Word> {
        String word;
        int frequency;

        public int hashCode() { return word.hashCode(); }


        public boolean equals(Object obj) { return word.equals(((Word)obj).word); }


        public int compareTo(Word w) { return w.frequency - frequency; }
    }


    public static void main(String[] args) throws IOException {
        Frequency("C:\\Users\\HP\\Downloads\\ACCProject\\res\\Web_Pages\\testHtml1.txt");

    }


}