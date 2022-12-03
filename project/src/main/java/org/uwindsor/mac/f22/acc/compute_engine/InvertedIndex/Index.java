package org.uwindsor.mac.f22.acc.compute_engine.InvertedIndex;
//@ author Balaji Chavan
import java.util.*;  // Provides TreeMap, Iterator, Scanner
import java.io.*;    // Provides FileReader, FileNotFoundException
public class Index{

    public static void FlightIni(String args,int var) throws IOException {
        StringToText st = new StringToText();

        st.add(args, var);
    }
    public static void performInvertedIndex(){
        Map<String, Set<Doc>> flightAns = new HashMap<>();//creating a new map

        // System.out.println("Enter path");


        // Scanner sc=new Scanner(System.in);
        //String input=sc.nextLine();
        for(int i=1;i<=2;i++){//2 iterations 1 for skyscanner and other for kyak


            Scanner FlightDocs = null;
            try {
                FlightDocs = new Scanner(new File("/tmp/A"+i+".txt"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
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
