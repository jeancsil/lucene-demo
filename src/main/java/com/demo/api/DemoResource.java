package com.demo.api;

import com.demo.DemoService;
import com.demo.Entry;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.ws.rs.Produces;
import java.util.List;

@Api(value = "demo", description = "Demo!", position = 1)
@RestController
@RequestMapping(value = "/")
public class DemoResource {
    @Resource
    private DemoService demoService;

    @RequestMapping(value = "/entries", method = RequestMethod.GET)
    @Produces("application/json; charset=UTF-8")
    @ApiOperation(
            value = "/entries",
            notes = "Retrieve entries matching query"
    )
    @ResponseBody
    public List<Entry> searchEntries(
            @ApiParam(value = "Lucene query", required = true) @RequestParam("q") String query,
            @ApiParam(value = "Lucene query limit") @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
    ) throws Exception {
        return demoService.query(query, limit);
    }

    @RequestMapping(value = "/entries", method = RequestMethod.POST)
    @ApiOperation(
            value = "/entries",
            notes = "Add entries"
    )
    @ResponseBody
    public Entry addEntries(
            @ApiParam(value = "Entry title", required = true) @RequestParam("title") String title,
            @ApiParam(value = "Entry text", required = true) @RequestParam("text") String text
    ) throws Exception {
        Entry entry = new Entry(title, text);
        demoService.index(entry);
        return entry;
    }

    @RequestMapping(value = "/entries/examples", method = RequestMethod.POST)
    @ApiOperation(
            value = "/entries/examples",
            notes = "Add example entries"
    )
    @ResponseBody
    public List<Entry> loadExampleEntries() throws Exception {
        List<Entry> entries = createExamples();
        for(Entry entry : entries){
            demoService.index(entry);
        }
        return entries;
    }

    List<Entry> createExamples(){
        List<Entry> entries = Lists.newArrayList();
        entries.add(new Entry("Information retrieval", "Information retrieval (IR) is the activity of obtaining information resources relevant to an information need from a collection of information resources. Searches can be based on metadata or on full-text (or other content-based) indexing."));
        entries.add(new Entry("Precision", "Precision is the fraction of the documents retrieved that are relevant to the user's information need."));
        entries.add(new Entry("Recall", "Recall is the fraction of the documents that are relevant to the query that are successfully retrieved."));
        entries.add(new Entry("Introduction to Information Retrieval", "This is the companion website for the following book. Christopher D. Manning, Prabhakar Raghavan and Hinrich Schütze, Introduction to Information Retrieval, Cambridge University Press. 2008."));
        return entries;
    }
}
