package assignment_two;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import static assignment_one.DocumentTextExtractionMode.REGEX;

public class SimilarWord {
	public static StringBuilder Builder = new StringBuilder();
	
	/**
	  * determines if the string is alphanumeric
	  * @parameter is s String.
	  */
	public static boolean isAlphaNumeric(String s) {
       return s != null && s.matches("^[a-zA-Z0-9]*$");
	}

	/**
	  * Reads the text file
	  * @param fileName String.
	  */
	public static void readFile(String fileName) {
		//Read the Filename
		try {
		    BufferedReader in = new BufferedReader(new FileReader("F22-8547-1-master/assignments/res/W3C_Web_Pages/" + fileName + ".htm"));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	Builder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
			System.out.println("Unable to read file");
		}
	}
	
	/**
	  * Algorithm to calculate distance between two words
	  * @param word1 String word2 String.
	  */
	public static int editDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
	 
		int[][] dp = new int[len1 + 1][len2 + 1];
	 
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
	 
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
	 
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//updating value of the dp for next increasing length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
	 
		return dp[len1][len2];
	}
	
	/**
	  * finds the similar word smallest distance 
	  * @param fileName String fileWord String.
	  */
	public static void findSmallestDistance(String fileName, String fileWord) {
		// Read file from the resources folder
		readFile(fileName);
		// Converting file content to string
		String content = Builder.toString();
		StringTokenizer str = new StringTokenizer(content," ");
			
		String word;
		String similarWord = "";
		int smallestDistance = 0;
		int distance;
		boolean firstItr = true;
		//iterating the string words
	     while (str.hasMoreTokens()) {
	    	 word = str.nextToken();
	    	 if(isAlphaNumeric(word)) {
	    		 word = word.toLowerCase(); // case ignoring
	    		 distance = editDistance(word, fileWord); // calculate Distance
	    		 if(firstItr) {
	    			 smallestDistance = distance;
	    			 similarWord = word;
	    			 firstItr = false;
	    		 } else {
	    			 if(distance < smallestDistance) {
		    			 smallestDistance = distance;
		    			 similarWord = word;
		    		 } 
	    		 }
	    	 }
	     }
	     
	     System.out.println("For the chosen file '" + fileName + ".htm' and word '" + fileWord + "' similar word and the smallest distance are:");
	     System.out.println("The similar word is: " + similarWord);
	     System.out.println("The smallest distance is: " + smallestDistance);
	}
	
	public static void main(String[] args) {
		// calculating the similar word and smallest distance different files and words 
		findSmallestDistance("Accessibility - W3C", "exanples");
		findSmallestDistance("Data - W3C", "Bata");
		findSmallestDistance("Identifiers - W3C", "Stancard");
		findSmallestDistance("About W3C Standards", "btsinness");
		
	}

}
