package luceneAndDB;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.oracleDS.DBConnection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import facade.lucene.InvokeSearch;

public class IndexDB implements InvokeSearch {

	IndexWriter indexWriter;

	ResultSet resultSet;

	@Override
	public void invoke(String indexDir, String dataDir, String query)
			throws IOException, ParseException {
		File INDEX_DIR = new File("C:\\LuceneIndex");
		//Directory directory = FSDirectory.open(new File(indexDir));
		Directory directory = new SimpleFSDirectory(INDEX_DIR);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
		indexWriter = new IndexWriter(directory, analyzer, true,
				IndexWriter.MaxFieldLength.UNLIMITED);

		try {
			this.indexDocs();
		} catch (SQLException e) {
			System.err.println(e);
		}
		// Begin Search now After Indexing
		search(directory, analyzer, query);

	}

	private void indexDocs() throws SQLException, CorruptIndexException,
			IOException {
		resultSet = DBConnection.getRecords();
		while (resultSet.next()) {
			Document d = new Document();
			d.add(new Field("ID", resultSet.getString("ID"),
					Field.Store.YES, Field.Index.NO));
			d.add(new Field("FIRST_NAME", resultSet.getString("FIRST_NAME"),
					Field.Store.NO, Field.Index.ANALYZED));
			d.add(new Field("MIDDLE_NAME", resultSet.getString("MIDDLE_NAME"),
					Field.Store.NO, Field.Index.ANALYZED));
			indexWriter.addDocument(d);
			d.getField("ID").setValue("6");
			indexWriter.addDocument(d);
		}
	}

	private void search(Directory directory, Analyzer analyzer, String data)
			throws CorruptIndexException, IOException, ParseException {
		Searcher searcher = new IndexSearcher(IndexReader.open(directory));
		Query query = new QueryParser(Version.LUCENE_35, "ID", analyzer)
				.parse(data);
		TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
		searcher.search(query, 10);
		//searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
		}
		searcher.close();
	}
}
