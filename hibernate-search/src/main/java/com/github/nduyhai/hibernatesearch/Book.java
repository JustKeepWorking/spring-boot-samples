package com.github.nduyhai.hibernatesearch;

import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

import static com.github.nduyhai.hibernatesearch.SearchService.*;

/**
 * From https://dzone.com/articles/hibernate-search-based
 */
@Entity
@Indexed
@AnalyzerDefs({
        @AnalyzerDef(name = "autoCompleteEdgeAnalyzer",
                // Split input into tokens according to tokenizer
                tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
                filters = {
                        // Normalize token text to lowercase, as the user is unlikely to
                        // care about casing when searching for matches
                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                                @Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
                                @Parameter(name = "replacement", value = " "),
                                @Parameter(name = "replace", value = "all")}),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = StopFilterFactory.class),
                        // Index partial words starting at the front, so we can provide
                        // Autocomplete functionality
                        @TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = {
                                @Parameter(name = "minGramSize", value = "3"),
                                @Parameter(name = "maxGramSize", value = "50")})}),
        @AnalyzerDef(name = "autoCompleteNGramAnalyzer",
                // Split input into tokens according to tokenizer
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
                filters = {
                        // Normalize token text to lowercase, as the user is unlikely to
                        // care about casing when searching for matches
                        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = NGramFilterFactory.class, params = {
                                @Parameter(name = "minGramSize", value = "3"),
                                @Parameter(name = "maxGramSize", value = "5")}),
                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                                @Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
                                @Parameter(name = "replacement", value = " "),
                                @Parameter(name = "replace", value = "all")})
                }),

        @AnalyzerDef(name = "standardAnalyzer",
                // Split input into tokens according to tokenizer
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
                filters = {
                        // Normalize token text to lowercase, as the user is unlikely to
                        // care about casing when searching for matches
                        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                                @Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
                                @Parameter(name = "replacement", value = " "),
                                @Parameter(name = "replace", value = "all")})
                }) // Def
})
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @Fields({
            @Field(name = "name", index = Index.YES, store = Store.YES,
                    analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
            @Field(name = NAME_EDGE_NGRAM_INDEX, index = Index.YES, store = Store.NO,
                    analyze = Analyze.YES, analyzer = @Analyzer(definition = "autoCompleteEdgeAnalyzer")),
            @Field(name = NAME_NGRAM_INDEX, index = Index.YES, store = Store.NO,
                    analyze = Analyze.YES, analyzer = @Analyzer(definition = "autoCompleteNGramAnalyzer"))
    })
    private String name;

    @Column(name = "author")
    @Fields({
            @Field(name = "author", index = Index.YES, store = Store.YES,
                    analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
            @Field(name = AUTHOR_NGRAM_INDEX, index = Index.YES, store = Store.NO,
                    analyze = Analyze.YES, analyzer = @Analyzer(definition = "autoCompleteEdgeAnalyzer")),
            @Field(name = AUTHOR_EDGE_NGRAM_INDEX, index = Index.YES, store = Store.NO,
                    analyze = Analyze.YES, analyzer = @Analyzer(definition = "autoCompleteNGramAnalyzer"))
    })
    private String author;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
