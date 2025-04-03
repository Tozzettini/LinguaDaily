package com.example.word_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;

// Word Controller to define and use the API endpoints

@RestController
@RequestMapping("/words")
public class WordController {

    @Autowired
    private WordService wordService;

    // Get all words
    @GetMapping
    public ResponseEntity<?> getAllWords(@RequestParam(required = false) String since) {
        System.out.println(since);

        if (since != null && !since.equals(Instant.MIN.toString())) {
            try {
                Instant sinceDate = Instant.parse(since);

                var words = this.wordService.getAllWords();

                words = words.stream().filter(w -> !w.getCreated().isBefore(sinceDate)).toList();

                return ResponseEntity.ok(words);

            } catch (DateTimeParseException e) {
                System.out.println(e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid date format for 'since'.");
            }
        }

        return ResponseEntity.ok(wordService.getAllWords());
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
