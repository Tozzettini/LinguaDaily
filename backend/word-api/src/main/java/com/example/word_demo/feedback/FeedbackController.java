package com.example.word_demo.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    @PostMapping
    public ResponseEntity<String> addFeedback(@RequestBody Feedback feedback) {
        if (feedback.getFeedback() == null || feedback.getFeedback().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Feedback cannot be empty.");
        }

        feedbackService.addFeedback(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body("Feedback saved successfully");
    }
}
