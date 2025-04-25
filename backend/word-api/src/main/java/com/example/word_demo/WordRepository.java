package com.example.word_demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//Used to interact with the database

public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value = "SELECT * FROM word WHERE word.language ILIKE :language ORDER BY word.id ASC LIMIT :limit OFFSET :skip", nativeQuery = true)
    List<Word> fetchWordsWithOffset(@Param("skip") int skip, @Param("limit") int limit, @Param("language") String language);

}
