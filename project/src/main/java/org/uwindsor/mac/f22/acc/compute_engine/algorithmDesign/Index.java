package algorithmDesign;
//@ author Balaji
import java.util.*;  // Provides TreeMap, Iterator, Scanner  
import java.io.*;    // Provides FileReader, FileNotFoundException  
public class Index{
	
public static void main(String[]args) throws IOException

{
	//StringToText st=new StringToText();
	//st.clear();
	//st.add("A,< ght>DDD DOne parser");
    Map<String, Set<Doc>> flightAns = new HashMap<>();//creating a new map
    
    
    for(int i=1;i<=2;i++){//2 iterations 1 for skyscanner and other for kyak
        Scanner FlightDocs = new Scanner(new File("/Users/itachi_luffy/Desktop/Test_File/Text//A"+i+".txt"));
        //reading the txt file to parse the data
      
        Doc document = new Doc("doc"+i);
        while(FlightDocs.hasNext()){//false when dat isnt present in the text file
            String word = FlightDocs.next();//for iterating a single word at a time
            
            document.put(word);//adding the iterated word to the document 
            Set<Doc> documents = flightAns.get(word);
            if(documents == null){//if document is empty add the words to the set
                documents = new HashSet<>();
                flightAns.put(word, documents);
            }
            documents.add(document);
        }
        FlightDocs.close();
    }

    StringBuilder sb = new StringBuilder();
    for(String word: flightAns.keySet()) {
        Set<Doc> documents = flightAns.get(word);
        sb.append(word + ":");
        for(Doc document:documents){
         sb.append("    "+document.getDocName() +" : "+ document.getCount(word));
            sb.append(", ");
           
        }
       // sb.delete(sb.length()-2, sb.length()-1);
        sb.append("\n");
    }
    System.out.println(sb);
    
}

}
