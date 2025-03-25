package com.example.linguadailyapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.linguadailyapp.database.word.Word
import com.example.linguadailyapp.database.word.WordRepository
import com.example.linguadailyapp.navigation.AppNavigation
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.utils.NotificationPermission
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        NotificationPermission(
            this,
            registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted ->
                NotificationPermission.handlePermissionResult(this, isGranted)
            }
        ).launch()

        runBlocking {
            fillDatabase(this@MainActivity)
        }

        //Starts with false since system inital setting is Lightmode
        var isDarkmode = getDarkModeSetting(this)



        setContent {

            LinguaDailyAppTheme(darkTheme = isDarkmode) {
                AppNavigation().start()
            }
        }
    }
}


fun saveDarkModeSetting(context: Context, isDarkMode: Boolean) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    with(sharedPreferences.edit()) {
        putBoolean("dark_mode", isDarkMode)

        apply()
    }

}

fun getDarkModeSetting(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("dark_mode", false) // Default to light mode
}

suspend fun fillDatabase(context: Context) {
    val wordRepository = WordRepository(context)

    wordRepository.wipeWordsTable()

    // Absolutely not the way you should do it, but works for test purposes
    wordRepository.insert(Word(word = "Ciao", description = "Hello", language = "Italian", date = LocalDate.now()))
    wordRepository.insert(Word(word = "Grazie", description = "Thank you", language = "Italian", date = LocalDate.now().minusDays(1)))
    wordRepository.insert(Word(word = "Per favore", description = "Please", language = "Italian", date = LocalDate.now().minusDays(2)))
    wordRepository.insert(Word(word = "Scusa", description = "Excuse me", language = "Italian", date = LocalDate.now().minusDays(3)))
    wordRepository.insert(Word(word = "Buongiorno", description = "Good morning", language = "Italian", date = LocalDate.now().minusDays(4)))
    wordRepository.insert(Word(word = "Buonasera", description = "Good evening", language = "Italian", date = LocalDate.now().minusDays(5)))
    wordRepository.insert(Word(word = "Buonanotte", description = "Good night", language = "Italian", date = LocalDate.now().minusDays(6)))
    wordRepository.insert(Word(word = "Arrivederci", description = "Goodbye", language = "Italian", date = LocalDate.now().minusDays(7)))
    wordRepository.insert(Word(word = "Come stai?", description = "How are you?", language = "Italian", date = LocalDate.now().minusDays(8)))
    wordRepository.insert(Word(word = "Sto bene", description = "I am fine", language = "Italian", date = LocalDate.now().minusDays(9)))
    wordRepository.insert(Word(word = "Mi chiamo", description = "My name is", language = "Italian", date = LocalDate.now().minusDays(10)))
    wordRepository.insert(Word(word = "Piacere", description = "Nice to meet you", language = "Italian", date = LocalDate.now().minusDays(11)))
    wordRepository.insert(Word(word = "Dove si trova?", description = "Where is it?", language = "Italian", date = LocalDate.now().minusDays(12)))
    wordRepository.insert(Word(word = "Quanto costa?", description = "How much does it cost?", language = "Italian", date = LocalDate.now().minusDays(13)))
    wordRepository.insert(Word(word = "Che ore sono?", description = "What time is it?", language = "Italian", date = LocalDate.now().minusDays(14)))
    wordRepository.insert(Word(word = "Parlo un po’ di italiano", description = "I speak a little Italian", language = "Italian", date = LocalDate.now().minusDays(15)))
    wordRepository.insert(Word(word = "Non capisco", description = "I don’t understand", language = "Italian", date = LocalDate.now().minusDays(16)))
    wordRepository.insert(Word(word = "Può ripetere?", description = "Can you repeat?", language = "Italian", date = LocalDate.now().minusDays(17)))
    wordRepository.insert(Word(word = "Dov’è il bagno?", description = "Where is the bathroom?", language = "Italian", date = LocalDate.now().minusDays(18)))
    wordRepository.insert(Word(word = "Ho bisogno di aiuto", description = "I need help", language = "Italian", date = LocalDate.now().minusDays(19)))
    wordRepository.insert(Word(word = "Posso avere il conto, per favore?", description = "Can I have the bill, please?", language = "Italian", date = LocalDate.now().minusDays(20)))
    wordRepository.insert(Word(word = "Mi dispiace", description = "I'm sorry", language = "Italian", date = LocalDate.now().minusDays(21)))
    wordRepository.insert(Word(word = "Va bene", description = "Okay", language = "Italian", date = LocalDate.now().minusDays(22)))
    wordRepository.insert(Word(word = "Come si dice?", description = "How do you say?", language = "Italian", date = LocalDate.now().minusDays(23)))
    wordRepository.insert(Word(word = "Quanto tempo ci vuole?", description = "How long does it take?", language = "Italian", date = LocalDate.now().minusDays(24)))
    wordRepository.insert(Word(word = "Mi può aiutare?", description = "Can you help me?", language = "Italian", date = LocalDate.now().minusDays(25)))
    wordRepository.insert(Word(word = "Sto cercando...", description = "I am looking for...", language = "Italian", date = LocalDate.now().plusDays(1)))
    wordRepository.insert(Word(word = "Qual è il tuo nome?", description = "What is your name?", language = "Italian", date = LocalDate.now().plusDays(2)))
    wordRepository.insert(Word(word = "Dove abiti?", description = "Where do you live?", language = "Italian", date = LocalDate.now().plusDays(3)))
    wordRepository.insert(Word(word = "Mi piace", description = "I like", language = "Italian", date = LocalDate.now().plusDays(4)))
    wordRepository.insert(Word(word = "Non mi piace", description = "I don’t like", language = "Italian", date = LocalDate.now().plusDays(5)))
}


