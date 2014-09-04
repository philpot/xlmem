package edu.isi.madcat.xlmem;

/* adapted from http://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/ */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {

	public static void main(String[] args) {

		String filePath = null;
		if (args.length > 0) {
				filePath = args[0];
		}
		else {
				filePath = "/tmp/test.txt";
		}

		BufferedReader br = null;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(filePath));

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
	

}