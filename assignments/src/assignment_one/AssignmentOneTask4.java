package assignment_one;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

public class AssignmentOneTask4 {
	
	public static void sortValue(Hashtable<?, Integer> t){ 

		  

	       //Transfer as List and sort it 

	       ArrayList<Map.Entry<String, Integer>> top = new ArrayList(t.entrySet()); 

	       Collections.sort(top, new Comparator<Map.Entry<String, Integer>>(){ 

	// Comparing the entries 

	         public int compare(Map.Entry<String, Integer> a1, Map.Entry<String, Integer> a2) { 

	            return a1.getValue().compareTo(a2.getValue()); 

	        }}); 

	        

	       displayTopTenResults(top); //Display the names. 

	    } 

	 

	//to display top 10 

	private static void displayTopTenResults(ArrayList<Entry<String, Integer>> l) { 

	 

	int size=l.size(); 

	for(int i=1;i<=10;i++) { 

	System.out.println(l.get(size-i).getKey()); 

	} 

	} 

	/*using hash we create a hash table and store elements and their frequency counts as key value pairs. Finally we traverse the hash table and print the key with max values*/ 
//
//	1)We Transfer the tree in list form and sort it using sort function  
//
//	2)compare the entries using the compare function and display the name of the ten reoccuring names In the list . 

	private static void TenMinFrequentWords(SplayTree<Item> splayTree) { 

	 

	for (int i = 0; i < 10; i++) { 

	Item minimumValue = splayTree.findMin(); 

	System.out.println(minimumValue.key + ":" + minimumValue.val); 

	splayTree.remove(minimumValue); 

	 

	} 
	}

}
