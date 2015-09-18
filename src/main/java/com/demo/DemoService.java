package com.demo;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class DemoService {
    private static final Logger log = Logger.getLogger(DemoService.class);
    private IndexWriter indexWriter;
    private Directory directory;
    private Analyzer analyzer;

    public DemoService(IndexWriter indexWriter, Directory directory, Analyzer analyzer){
        this.indexWriter = indexWriter;
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public void index(Entry entry) {
        Document document = new Document();
        document.add(new TextField("title", entry.getTitle(), Field.Store.YES));
        document.add(new TextField("text", entry.getText(), Field.Store.YES));
        try {
            indexWriter.addDocument(document);
            indexWriter.commit();
        } catch (IOException e) {
            log.warn("Failed to write document to index!", e);
        }
    }

    public List<Entry> query(String query, int hits){
        List<Entry> entries = Lists.newArrayList();
        try{
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
            TopScoreDocCollector collector = TopScoreDocCollector.create(hits);
            Query q = new MultiFieldQueryParser(new String[] {"title", "text"},analyzer).parse(query);
            searcher.search(q, collector);
            ScoreDoc[] results = collector.topDocs().scoreDocs;
            for(ScoreDoc scoreDoc : results){
                Document d = searcher.doc(scoreDoc.doc);
                entries.add(new Entry(d.get("title"), d.get("text")));
            }
        }catch (Exception e){
            log.warn(String.format("Failed to retrieve information for query '%s'", query), e);
        }
        return entries;
    }
}
