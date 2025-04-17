package com.example.linguadailyapp.database.learnedWord

// Data model for a word and its details
data class WordDetails(
    val word: String,
    val partOfSpeech: String,
    val pronunciation: String,
    val definition: String,
    val etymology: String,
    val examples: List<String>,
    val usageSamples: List<String>,
    val isBookmarked: Boolean = false
)

// Sample word data (you'll replace this with backend data later)
val sampleWordDetails = WordDetails(
    word = "minatory",
    partOfSpeech = "adjective",
    pronunciation = "MIN-uh-tor-ee",
    definition = "Threatening or menacing: expressing or conveying a threat.",
    etymology = "From Latin minātōrius, from minātus, perfect participle of minārī (to threaten).",
    examples = listOf(
        "The government issued a minatory statement warning of severe consequences for any violations.",
        "His minatory posture and clenched fists made it clear he was ready for a confrontation.",
        "The novel's minatory atmosphere keeps readers on edge throughout the story."
    ),
    usageSamples = listOf(
        "The professor's minatory glare silenced the chattering students immediately.",
        "Her minatory tone left no doubt about the consequences of missing the deadline.",
        "The dark clouds had a minatory appearance, prompting everyone to seek shelter before the storm arrived."
    )
)

/*
When you integrate with your backend, you'll:

Replace sampleWordDetails with data fetched from your API/database
Update the currentWordDetails state when users select different words
Possibly implement a ViewModel to manage this state properly


For navigation between screens:

The NavHost setup shows how to share the same data between screens
When a user navigates from MainWordCard to WordDetailScreen, they'll see the same word data
 */