package com.example.word_demo.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Word Controller to define and use the API endpoints

@RestController
@RequestMapping("/words")
public class WordController {

    @Autowired
    private WordService wordService;

    private int DEFAULT_SKIP = 10;
    private int DEFAULT_LIMIT = 10;
    private String DEFAULT_LANGUAGE = "English";

    // Get {limit} number of words while skipping the first {skip} records from the language {language}
    @GetMapping
    public List<Word> getWords(
            @RequestParam(required = false) Integer skip, // Do not use primitive types, springboot cannot assign them null
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String language
    ) {
        var effectiveSkip = skip == null ? DEFAULT_SKIP : skip;
        var effectiveLimit = limit == null ? DEFAULT_LIMIT : limit;
        var effectiveLanguage = language == null ? DEFAULT_LANGUAGE : language.substring(0, 1).toUpperCase() + language.substring(1);

        return wordService.getWords(effectiveSkip, effectiveLimit, effectiveLanguage);
    }

    // Insert a new word
    @PostMapping
    public ResponseEntity<Word> addWord(@RequestBody Word word) {
        Word savedWord = wordService.addWord(word);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWord);
    }
}
