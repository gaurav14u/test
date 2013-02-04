package luceneAndDB;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;

import facade.lucene.InvokeSearch;

public class LuceneClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InvokeSearch search = new IndexDB();
		try {
			search.invoke("C:\\LuceneIndex", "C:\\Books", "6");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
