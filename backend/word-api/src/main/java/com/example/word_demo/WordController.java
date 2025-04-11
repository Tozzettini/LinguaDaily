package com.example.word_demo;

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

    // Get {limit} number of words while skipping the first {skip} records
    @GetMapping
    public List<Word> getWords(
            @RequestParam(required = false) Integer skip, // Do not use primitive types, springboot cannot assign them null
            @RequestParam(required = false) Integer limit
    ) {
        var effectiveSkip = skip == null ? DEFAULT_SKIP : skip;
        var effectiveLimit = limit == null ? DEFAULT_LIMIT : limit;

        return wordService.getWords(effectiveSkip, effectiveLimit);
    }

    // Insert a new word
    @PostMapping
    public ResponseEntity<Word> addWord(@RequestBody Word word) {
        Word savedWord = wordService.addWord(word);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWord);
    }
}
