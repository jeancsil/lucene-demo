package com.demo.config;

import com.demo.DemoAnalyzer;
import com.demo.DemoService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
public class DemoConfig {
    @Bean
    public Analyzer analyzer(){
        Tokenizer source = new WhitespaceTokenizer();
        return new DemoAnalyzer(
                new Analyzer.TokenStreamComponents(
                        source, new EdgeNGramTokenFilter(
                                new ASCIIFoldingFilter(
                                        new LowerCaseFilter(
                                                source
                                        )
                                ),
                        2, 5)
                )
        );
    }

    @Bean
    public IndexWriterConfig indexWriterConfig(Analyzer analyzer){
        return new IndexWriterConfig(analyzer);
    }

    @Bean
    public IndexWriter indexWriter(Directory directory, IndexWriterConfig indexWriterConfig) throws IOException {
        return new IndexWriter(directory, indexWriterConfig);
    }

    @Bean
    public DemoService demoService(IndexWriter indexWriter, Directory directory, Analyzer analyzer) {
        return new DemoService(indexWriter, directory, analyzer);
    }

    @Profile("default")
    public static class InMemoryConfig {
        @Bean
        public Directory directory() throws IOException {
            return new RAMDirectory();
        }
    }

    @Profile("persistent")
    public static class PersistentConfig {
        @Bean
        public Directory directory() throws IOException {
            return new SimpleFSDirectory(Paths.get("/tmp"));
        }
    }
}

