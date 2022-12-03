package org.uwindsor.mac.f22.acc.compute_engine.InvertedIndex;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Doc {
    String docName;
    Map<String, Integer> m = new HashMap<>();

    public Doc(String docName){
        this.docName = docName;
    }

    public void put(String word) {
        Integer freq = m.get(word);
        m.put(word, (freq == null) ? 1 : freq + 1);
    }

    public Integer getCount(String word) {
        return m.get(word);
        //FequencySearch.manageSearch(m,word);
    }

    public String getDocName() {
        return this.docName;
        
    }
    
}