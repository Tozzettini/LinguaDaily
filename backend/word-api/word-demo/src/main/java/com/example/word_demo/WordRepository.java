package com.example.word_demo;

import com.example.word_demo.Word;
import org.springframework.data.jpa.repository.JpaRepository;

//Used to interact with the database

public interface WordRepository extends JpaRepository<Word, Long> {
}
