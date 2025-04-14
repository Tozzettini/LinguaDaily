[10:42 PM, 4/10/2025] Joost (Fontys):  Your Current Design (Local-Only, Frontend-Tracked)
Architecture:

🗄️ Centralized database stores all approved words, uniquely ordered by id.

📱 The frontend (app) manages progress:

available_words (SQLite) → up to 300 unshown words

learned_words (SQLite) → words already shown, with metadata like learned_at, bookmarked

🔁 App fetches words in batches using a simple API:

Example: GET /words?skip=300&limit=100

App keeps track of skip value locally to avoid repeats.

Flow:

On install → App requests first 300 words (skip=0)

Daily notification → moves a word from available to learned

When available_words < threshold → app fetches next batch (e.g., skip=300)

If app is uninstalled, all progress is lost

Pros:

Offline support

Simple backend

Efficient, few network calls

Cons:

Backend has no user context (can’t personalize or sync)

Reinstalls lose all user progress

Repeats are possible if app loses skip value

Hard to scale or add new features (difficulty levels, streaks, etc.)

✅ Hybrid Model (Server-Tracked Progress, Local Cache)
Architecture:

🗄️ Centralized database:

words table: all approved words

users table: user identity and progress

user_words table: optionally stores full word history (user_id, word_id, learned_at, etc.)

📱 App stores only a local cache of ~300 words

May also store recently learned words offline until sync

Flow:

On install or cache drop → app asks backend:
“Give me the next 100 words for user 123”

Backend:

Looks up user’s last_word_id or fetches from user_words

Returns next batch

Updates progress

App stores batch in local available_words

App uses those words for notifications and requests

If user logs in on a new phone → backend knows what they’ve seen

Pros:

Still supports offline use

Backend ensures no repeats

Progress survives reinstall or device switch

Enables future personalization (difficulty, categories, streaks)

Cleaner sync model and future scalability

Cons:

Slightly more complex backend

Needs user account or unique ID to track progress

Requires sync mechanism on app side (e.g., transaction retries, offline caching)

🧠 TL;DR
Feature	Your Design	Hybrid Model
Offline support	✅ Yes	✅ Yes
Backend tracks progress	❌ No	✅ Yes
Prevents duplicates forever	❌ Only if skip is tracked well	✅ Yes
Handles reinstalls	❌ No	✅ Yes
Needs user login	❌ No	⚠️ Yes (or at least stable user ID)
Simple to implement	✅ Yes	⚠️ Medium complexity
Scales to future features	⚠️ Hard	✅ Easy
[10:43 PM, 4/10/2025] Joost (Fontys): So afterwards we are going to a hybrid model (server-tracked progess, local cache)
