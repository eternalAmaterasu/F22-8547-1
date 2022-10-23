package assignment_one;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

//Create class Question5Part1
public class Question5Part1 {
	// Main Method
	public static void main(String[] args) throws IOException {
		// StringBuilder Object for writing content
		StringBuilder content = new StringBuilder();
		// Reading HTML files from folder resources W3C Web Pages
		final File file = new File("assignments/res/W3C_Web_Pages");
		try {
			for (final File entry : file.listFiles()) {
				if (entry.isFile()) {
					String fileName = entry.getName();
					BufferedReader in = new BufferedReader(new FileReader("assignments/res/W3C_Web_Pages/" + fileName));
					String str;
					// readLine from the File
					while ((str = in.readLine()) != null) {
						content.append(str);
					}
					in.close();
				}
			}
		} catch (IOException e) {
			System.out.println("could not read file");
		}

		// try to convert content/data in to string
		String str = content.toString();
		StringTokenizer s = new StringTokenizer(str, " ");
		// accessing splay tree class to create spell checker
		Question5Part2<String, Integer> splayTree = new Question5Part2<String, Integer>(); 
		String value;
		int count = 0;

		// finding out the string words
		while (s.hasMoreTokens()) {
			value = s.nextToken();
			//find out frequency of html file
			if (IsNumAlpha(value)) {
				//converting into lower case
				value = value.toLowerCase(); 
				if (count == 0) { 
					//adding word into splay tree
					splayTree.put(value, 1);
					count++;
				} else {
					if (splayTree.contains(value)) {
						splayTree.put(value, splayTree.get(value) + 1); 
						System.out.println(value);
					} else {
						splayTree.put(value, 1);
						System.out.println(value);
					}
				}
			}

		}

	}

	public static boolean IsNumAlpha(String str) {
		return str != null && str.matches("^[a-zA-Z0-9]*$");
	}

}
