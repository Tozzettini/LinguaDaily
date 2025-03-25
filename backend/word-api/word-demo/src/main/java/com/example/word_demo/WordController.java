package com.example.word_demo;

import com.example.word_demo.Word;
import com.example.word_demo.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Word Controller to define and use the API endpoints

@RestController
@RequestMapping("/words")
public class WordController {

    @Autowired
    private WordService wordService;

    // Get all words
    @GetMapping
    public List<Word> getAllWords() {
        return wordService.getAllWords();
    }

    // Get a single word by id
    @GetMapping("/{id}")
    public ResponseEntity<Word> getWordById(@PathVariable Long id) {
        Optional<Word> word = wordService.getWordById(id);
        return word.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Insert a new word
    @PostMapping
    public ResponseEntity<Word> addWord(@RequestBody Word word) {
        Word savedWord = wordService.addWord(word);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWord);
    }
}
