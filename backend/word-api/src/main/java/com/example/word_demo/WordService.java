package com.example.word_demo;

import com.example.word_demo.Word;
import com.example.word_demo.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Business logic that calls what is needed

@Service
public class WordService {

    @Autowired
    private WordRepository wordRepository;

    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }

    public Optional<Word> getWordById(Long id) {
        return wordRepository.findById(id);
    }

    public Word addWord(Word word) {
        return wordRepository.save(word);
    }

    public List<Word> getWords(int skip, int limit, String language) {
        return wordRepository.fetchWordsWithOffset(skip, limit, language);
    }
}
