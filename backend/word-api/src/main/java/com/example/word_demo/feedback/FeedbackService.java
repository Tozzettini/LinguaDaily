package com.example.word_demo.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository wordRepository;

    public Feedback addFeedback(Feedback feedback) {
        return wordRepository.save(feedback);
    }
}
