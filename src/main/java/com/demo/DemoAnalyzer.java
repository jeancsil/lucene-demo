package com.demo;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;

public class DemoAnalyzer extends Analyzer {
    private static final Logger log = Logger.getLogger(DemoAnalyzer.class);
    private TokenStreamComponents components;

    public DemoAnalyzer(TokenStreamComponents components) {
        this.components = components;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return components;
    }
}
