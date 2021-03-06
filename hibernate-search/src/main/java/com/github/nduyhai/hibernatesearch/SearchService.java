package com.github.nduyhai.hibernatesearch;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class SearchService {

    private EntityManagerFactory factory;

    public static final String NAME_NGRAM_INDEX = "edgeNGramName";
    public static final String NAME_EDGE_NGRAM_INDEX = "nGramName";
    public static final String AUTHOR_NGRAM_INDEX = "edgeNGramAuthor";
    public static final String AUTHOR_EDGE_NGRAM_INDEX = "nGramAuthor";

    public SearchService(EntityManagerFactory factory) {
        this.factory = factory;
    }

    @PostConstruct
    public void initHibernateSearch() {
        try {
            EntityManager em = factory.createEntityManager();
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public List<Book> fuzzy(String criteria, int limit) {
        final EntityManager entityManager = this.factory.createEntityManager();
        final FullTextEntityManager em = Search.getFullTextEntityManager(entityManager);
        final QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
        final Query luceneQuery = qb.keyword().fuzzy()
                .withEditDistanceUpTo(1)
                .withPrefixLength(1)
                .onFields("name")
                .andField("author")
                .matching(criteria)
                .createQuery();

        javax.persistence.Query jpaQuery = em.createFullTextQuery(luceneQuery, Book.class);
        jpaQuery.setMaxResults(limit);
        return jpaQuery.getResultList();
    }

    @Transactional
    public List<Book> search(String criteria, int limit) {
        final EntityManager entityManager = this.factory.createEntityManager();
        final FullTextEntityManager em = Search.getFullTextEntityManager(entityManager);
        final QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
        final Query luceneQuery = qb
                .phrase().withSlop(2)
                .onField(NAME_NGRAM_INDEX)
                .andField(NAME_EDGE_NGRAM_INDEX)
                .andField(AUTHOR_NGRAM_INDEX)
                .andField(AUTHOR_EDGE_NGRAM_INDEX)
                .boostedTo(5)
                .sentence(criteria.toLowerCase())
                .createQuery();

        javax.persistence.Query jpaQuery = em.createFullTextQuery(luceneQuery, Book.class);
        jpaQuery.setMaxResults(limit);
        return jpaQuery.getResultList();
    }
}
