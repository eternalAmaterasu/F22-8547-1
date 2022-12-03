package org.uwindsor.mac.f22.acc.compute_engine.InvertedIndex;
import java.io.FileWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class StringToText {
    /*public void create() throws IOException {
        File file = File.createTempFile("temp", null);

          System.out.println(file.getAbsolutePath());

    }*/
    public void clear() throws IOException {
        FileWriter fwOb = new FileWriter("/tmp/A1", false);

        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();//clearing already existing data.
        pwOb.close();
        fwOb.close();
        // System.out.println("Delete");
    }

    public void clear2() throws IOException {
        FileWriter fwOb = new FileWriter("/tmp/A2", false);

        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
        // System.out.println("Delete");
    }


    public void add(String ar, int var) {
        BufferedWriter writer = null;
        String as = ar.replaceAll("\\<[^>]*>", "");
        try {

            writer = new BufferedWriter(new FileWriter("/tmp/A" + var + ".txt"));
            writer.write(as.toLowerCase());
            //System.out.println("Sussess for A1");
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
            }
        }


    }
}