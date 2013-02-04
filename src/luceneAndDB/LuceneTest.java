package luceneAndDB;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class LuceneTest {
	public static void main(String[] args) {
		try {
			File tempFile = new File("E:\\temp");
			Directory INDEX_DIR = new SimpleFSDirectory(tempFile);
			StandardAnalyzer analyzer = new StandardAnalyzer(
					Version.LUCENE_CURRENT);
			IndexWriter writer = new IndexWriter(INDEX_DIR, analyzer, true,
					IndexWriter.MaxFieldLength.UNLIMITED);
			writer.setRAMBufferSizeMB(48);
			writer.setUseCompoundFile(false);

			// in this below code we have added data for first table which
			// contains id and name
			Document doc = new Document();
			doc.add(new Field("tab1_id", "1", Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field("tab1_name", "jaydatt", Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
			writer.addDocument(doc);

			doc.getField("tab1_id").setValue("2");
			doc.getField("tab1_name").setValue("jay");
			writer.addDocument(doc);
			// ends entry for first table

			// now here is the second table in which we have added id and
			// desgnation of the user
			doc = new Document();
			doc.add(new Field("tab2_id", "1", Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field("tab2_field", "SE", Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
			writer.addDocument(doc);

			doc.getField("tab2_id").setValue("2");
			doc.getField("tab2_field").setValue("JSE");
			writer.addDocument(doc);
			// ends entry for second table

			writer.optimize();
			writer.close();

			analyzer.close();
			new LuceneTest().search();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void search() {
		try {
			// in this search method we will fetch the data for the user from
			// first table
			File tempFile = new File("E:\\temp");
			Directory INDEX_DIR = new SimpleFSDirectory(tempFile);
			Query q = new QueryParser(Version.LUCENE_CURRENT, "tab1_name",
					new StandardAnalyzer(Version.LUCENE_CURRENT))
					.parse("jaydatt");

			TopScoreDocCollector collector = TopScoreDocCollector.create(10,
					true);
			IndexSearcher searcher = new IndexSearcher(INDEX_DIR, true);
			searcher.search(q, collector);
			
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				System.out.println(d.get("tab1_id"));
				System.out.println(d.get("tab1_name"));
			}
			searcher.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
