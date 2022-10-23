package assignment_one;

//author: Veera venkata satya divya pedapati
//Assignment : Question 3
//Dated on :02/10/2022

/**
 * @author Veera venkata satya divya pedapati
 * @since 02/10/2022
 */

import org.jsoup.Jsoup;
import java.io.File;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SplayTree {
	enum DocumentTextExtractionMode {
	    JSOUP,
	    REGEX
	}
	//creating new node using constructor which store keyword and frequency
	static class Node
	{
	    String keyword;//variable to store keyword
	    int frequency;//variable to store frequency of word
	    Node left, right;
	    Node(String word,int freq)//parameterized constructor to create node with value
	    {
	    	this.keyword=word;
	    	this.frequency=freq;
	    	this.left=this.right=null;
	    }
	}
	static Node root;//creating static root node 
	//rotating tree to right
	static Node rotateRight(Node rrn)
	{
		Node temp = rrn.left;
	    rrn.left = temp.right;
	    temp.right = rrn;
	    return temp;
	}
	//rotating tree to left
	static Node rotateLeft(Node lrn)
	{
		 Node temp = lrn.right;
		 lrn.right = temp.left;
		 temp.left = lrn;
		 return temp;
	}
	//method to insert nodes in splay tree 
	static Node insert(int freq,String word)
	{
		Node Node=new Node(word,freq);//creates new node with given parameters
	    if (root == null) //if node if the first node then it will assign it as root
	    	return Node;
	    root = splay(root, freq,word);//if not inserts node in splay tree based on its position
	    if (root.frequency == freq) 
	    	return root;
	    if (root.frequency > freq)//if newnode frequency is less than root frequency we insert new key into left tree
	    {
	        Node.right = root;
	        Node.left = root.left;
	        root.left = null;
	    }
	    else//if new node frequency is greater than root new node will be inserted in right tree
	    {
	        Node.left = root;
	        Node.right = root.right;
	        root.right = null;
	    }
	    return Node; 
	}
	//method to create splay tree
	//method brings the frequency node at root, if frequency is not in tree it will fetch the last accessed node
	static Node splay(Node Splayroot, int freq,String word)
	{
	    if (Splayroot == null || Splayroot.frequency == freq)
	        return Splayroot;
	    //if root frequency is greater than the frequency given then it splay and rotate right  tree
	    if (Splayroot.frequency > freq)
	    {
	        if (Splayroot.left == null) 
	        	return Splayroot;
	        if (Splayroot.left.frequency > freq)
	        {
	        	Splayroot.left.left = splay(Splayroot.left.left, freq,word);
	        	Splayroot = rotateRight(Splayroot);
	        }
	        else if (Splayroot.left.frequency < freq)
	        {
	        	Splayroot.left.right = splay(Splayroot.left.right, freq,word);
	            if (Splayroot.left.right != null)
	            	Splayroot.left = rotateLeft(Splayroot.left);
	        }
	        return (Splayroot.left == null)? Splayroot: rotateRight(Splayroot);
	    }
	    else
	    {
	        if (Splayroot.right == null) return Splayroot;
	        if (Splayroot.right.frequency > freq)
	        {
	        	Splayroot.right.left = splay(Splayroot.right.left, freq,word);
	            if (Splayroot.right.left != null)
	            	Splayroot.right = rotateRight(Splayroot.right);
	        }
	        else if (Splayroot.right.frequency < freq)
	        {
	        	Splayroot.right.right = splay(Splayroot.right.right, freq,word);
	        	Splayroot = rotateLeft(Splayroot);
	        }
	        return (Splayroot.right == null)? Splayroot: rotateLeft(Splayroot);
	    }
	}
	//method to access splay tree in preorder basis
	static void preOrder(Node root)
	{
	    if (root != null)
	    {
	        System.out.print(root.keyword+":"+root.frequency+" ");
	        preOrder(root.left);
	        preOrder(root.right);
	    }
	}
	
	//method to read html files from given filepath
	private static String readHtmlFile(String filePath) {
        StringBuilder htmlFileData = new StringBuilder();//string builder to store the html file data
        try (BufferedReader bRHtml = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bRHtml.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) htmlFileData.append(line);
            }
        } 
        catch (IOException e) {
            System.out.printf("Encountered error while reading the file at '%s': '%s'%n", filePath, e);
        }
        return htmlFileData.toString();//converting string builder to string
    }
	
	//method to convert alphanumeric string to lowercase 
	 private static String sanitizeString(String data) {
         if (data.matches("[0-9a-zA-Z]+")) return data.toLowerCase();
         return "";//if string is not alpha numeric returns empty string
     }
	 
	 //method remove newlines,breaks and float values
     private static String cleanDocumentViaRegex(String data) 
     {
         return data
                 .replaceAll("<(.|\f|\r|\n)*?>", " ")//replaces newline,floating point,breaks with space
                 .replaceAll("\\s+", " ");
     }
     
     //clean the document data using jsoup
     private static String cleanDocumentViaJsoup(String data) 
     {
         return Jsoup.parse(data).text();
     }
	//method to create splay tree
	public static void createSplayTree(String filedata,DocumentTextExtractionMode mode)
	{
		String cleanHtmlData;
        switch (mode) { //cleaning data based on the mode passed onto
            case REGEX:
                cleanHtmlData = cleanDocumentViaRegex(filedata);
                break;
            case JSOUP:
            default:
                cleanHtmlData = cleanDocumentViaJsoup(filedata);
        }
       //creating string array to store each word
        String[] splitHtmlData = cleanHtmlData.split("\\s+");// splits every word in extracted text from html file
        Map<String, Integer> hashMap = new HashMap<>();//creating hashmap to keep track of frequency
        //loop to iterate every word extracted from html file
        for (String word : splitHtmlData) {
        	String sanitizedWord =sanitizeString(word);//sanitized every word encountered
        	Integer integer = hashMap.get(sanitizedWord);//fetched frequency of 
        	 
            if (integer == null && sanitizedWord!="")//if frequency is null mean first occurance of word
                hashMap.put(sanitizedWord, 1);//map keyword with frequency 1
 
            else if (sanitizedWord!="") {
                hashMap.put(sanitizedWord, integer + 1);//else increment the frequency 
            }
        }
        Set entrySet = hashMap.entrySet();//entry set created to set out of the same elements contained in hash map
        Iterator itr=entrySet.iterator();//iterator to loop the hashmap elements
        //while to iterate all items in hashmap
        while(itr.hasNext())
        {
        	
        	 Map.Entry me = (Map.Entry)itr.next();//accessing element in hashmap
        	 int freq=(int) me.getValue();//converting object into integer
        	 String word=(String) me.getKey();//converting object into string
        	 root=insert(freq,word);//insert keyword and frequency in splay tree
        }
        

	}
	
	
	public static void main(String[] args)
	{
		try {
			File directoryPath = new File("assignments/res/W3C_Web_Pages");//setting up file path to access files
			  File filesList[] = directoryPath.listFiles();//get list of files and store it in an arrya
			  StringBuilder dataall = new StringBuilder();//creating string builder to store html file data
			  for(File file : filesList) //loop to access each and every file
				  dataall=dataall.append(readHtmlFile(file.getAbsolutePath()));//append each file data to string builder
			  String filedata=dataall.toString();//convert string builder to string
   createSplayTree(filedata,DocumentTextExtractionMode.REGEX);//creating splay tree with htmlfile data	    
   root=insert(12,"divya");
   System.out.print("Preorder traversal of the Splay tree is \n");
			preOrder(root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


