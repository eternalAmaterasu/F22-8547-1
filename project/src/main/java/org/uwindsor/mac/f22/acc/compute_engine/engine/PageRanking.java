package engine;
import static engine.DocumentTextExtractionMode.REGEX;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;


class NodeInHeap{

	int scoreOfInputWord;
	String webPageName;
	public NodeInHeap(int i, String webPageName) {
		this.scoreOfInputWord = i;
		this.webPageName = webPageName;
	}
	public String getWebPageName() {
		return webPageName;

	}

}

class HeapComparator implements Comparator<NodeInHeap>{

	@Override
	public int compare(NodeInHeap node1, NodeInHeap node2) {
		if (node1.scoreOfInputWord < node2.scoreOfInputWord) {
			return 1;
		}
		else if (node1.scoreOfInputWord > node2.scoreOfInputWord){
			return -1;
		}
		return 0;
	}

}

public class PageRanking {

	public static void main(String[] args) throws IOException {
		
		//Set of input words for which we need to find the score of web pages 
		//based on the availability of words
		String[] inputWords = {""};

		//Initialize the folder which contains the files
		File folderPath = new File("C:\\Users\\HP\\eclipse-workspace\\PageRank\\res\\kayak-data.html");

		Hashtable<Integer, String> wordfrequencyTableForSpecificInput = new Hashtable<>();

		for(File fileInFileList: folderPath.listFiles()) {

			String filePath = fileInFileList.getAbsolutePath();

			//This function returns a hash table of word*count
			Hashtable<String, Long> wordfrequencyTable = Compute_Frequency.computeWordFrequency(filePath , REGEX);

			Integer totalScoreForinput = computeWordFrequencyForSetOfInputs(wordfrequencyTable , inputWords);

			String fileName = fileInFileList.getName();

			//Creates a hash table of score * file name, for the given set of input words.
			wordfrequencyTableForSpecificInput.put(totalScoreForinput, fileName);

		}

		System.out.println(System.lineSeparator() + "Completed generating a hash table for each of the web page, with the best matches for the following input words:");
		System.out.println(Arrays.toString(inputWords));
		System.out.println("Final unique keys: " + wordfrequencyTableForSpecificInput.keySet().size());
		System.out.println(wordfrequencyTableForSpecificInput);

		//This function creates a priority queue and prints the top 10 pages from the heap.
		createHeapAndPrintTopTenResults(wordfrequencyTableForSpecificInput);

	}

	/**
	 * calculates the total score of input words in each page
	 *
	 * @param inputTable
	 * @param sampleInputWords
	 * @return integer This return the total score of input words in the web page.
	 */
	private static Integer computeWordFrequencyForSetOfInputs(Hashtable<String, Long> inputTable, String[] sampleInputWords) {

		Integer totalScore = (int) 0L;

		for(int j = 0; j < sampleInputWords.length; j++) {
			
			//sanitize the input word, i.e. consider only those words which are having 0-9 or/and a-z or/and A-Z, rest all are ignored
			String inputWord = Compute_Frequency.sanitizeString(sampleInputWords[j]);

			Long scoreForEachInputWord = inputTable.get(inputWord);
			if (scoreForEachInputWord != null){
				totalScore = (int) (totalScore + scoreForEachInputWord);
			}
		}

		return totalScore;

	}

	/**
	 * creates a hash table with score and file name, and prints the top 10 nodes
	 *
	 * @param tempHashTable
	 */
	public static void createHeapAndPrintTopTenResults(Hashtable<Integer, String> tempHashTable) {

		PriorityQueue<NodeInHeap> maxheap = new PriorityQueue<NodeInHeap>(10, new HeapComparator());

		for(int scoreForEachInputWord: tempHashTable.keySet())
		{
			maxheap.offer(new NodeInHeap(scoreForEachInputWord,tempHashTable.get(scoreForEachInputWord)));
		}
		
		System.out.println(System.lineSeparator() + "Completed generating a heap having size " + maxheap.size() + ", from the hash table of score * webpagename" );
		System.out.println(System.lineSeparator() + "The top ten pages which contains the input words are : ");

		while(!maxheap.isEmpty())
			System.out.println(maxheap.poll().getWebPageName());

	}

}
