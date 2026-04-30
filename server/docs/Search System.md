Postgres can do more than people give it credit for. At Hello Interview, we don't use Elasticsearch or Algolia for search. It's just Postgres with pgvector and tsvector.

Just shipped a big round of improvements, so I wanted to break down how it works.

We run hybrid search (vector + full-text) and you probably need both.

pgvector converts text into arrays of ~1500 numbers that capture meaning. "automobile" and "car" end up near each other. You embed your query, measure distance to every document, and return the closest. This works well when someone searches "how do I handle lots of users" and you want to surface articles about horizontal scaling.

But it needs to be paired with tsvector, Postgres's built-in full-text search. It tokenizes words ("running" becomes "run"), stores them in a GIN index, and checks whether query tokens appear in the document.

Each has a blind spot. Searching "leader election" should return our ZooKeeper article, which discusses it extensively. But in vector space those concepts aren't close because the article is *about* ZooKeeper. tsvector catches it instantly because the words literally appear in the text.

We UNION candidate pools instead of just reranking.

If you retrieve 100 candidates via vector search and rerank them using text scores, any document ranked >100 in vector distance never entered your candidate pool and no amount of reranking can fix it.

So we UNION three candidate pools in one query:

- Nearest vectors via pgvector ANN search

- Top tsvector keyword matches via GIN index scan

- Title matches for navigational queries

Each surfaces documents the others miss. We rerank the combined set with a weighted score using Postgres CTEs.

We spent more time tuning weights than building the system.

Our final score combines four signals:

- vec_sim (distance in embedding space)

- 0.3* text_score (keyword relevance via ts_rank)

- 0.4* priority (editorial weight for important content)

- A lexical title bonus if the title contains query words

These coefficients interact in non-obvious ways. Bump text_score too high and keyword matches dominate over semantically better results. Too low and you're back to embedding blind spots. There's no correct answer, just tradeoffs for your specific data.

I had Claude generate tons of test cases I reviewed and could run from the command line. Claude could then run the suite, see what broke, tweak, repeat.

The tests also caught silent data bugs. ID collisions overwriting documents, a content category missing from the index, pages invisible because our parser only handled one of two Next.js metadata patterns. None of these threw errors, but the test suite caught all of them.

While we don't have huge scale (yet), this same setup mirrors what many companies with much larger workloads use.


Uses OpenAI text-embedding-3-small 


###
https://chatgpt.com/share/69d67a66-caec-832a-92bd-92df9b5734ce

###
This writeup is actually describing a production-grade search architecture—and you’re already halfway there with your current system (your markdown shows a solid embedding pipeline).
Let me break it down in a way that connects directly to your devotional app (Koinonia Daily) and show where you should evolve.
🧠 1. The Core Idea: Hybrid Search
They are combining two fundamentally different search systems:
A. Semantic Search (pgvector)
Uses embeddings (like you already do)
Finds meaning
Example:
Query: “how to grow spiritually”
Returns teachings about prayer, discipline, intimacy with God
👉 This is what your current system is built around:
“Similarity = distance in vector space” �
Recommendation System.md None
B. Keyword Search (tsvector)
Uses PostgreSQL full-text search
Finds exact words
Example:
Query: “Holy Spirit baptism”
Returns teachings that literally contain those words
⚠️ Key Insight
Semantic ≠ Keyword
Your current system:
❌ Only semantic (embeddings)
Their system:
✅ Semantic + Keyword
🔥 2. Why Embeddings Alone Are Not Enough
Your system works great for:
“teachings similar to this”
“spiritually aligned content”
But fails for:
Example:
User searches:
“leader election”
Teaching content:
“ZooKeeper coordination algorithm…”
👉 Embeddings might miss it (topic mismatch) 👉 Keywords will hit instantly
⚖️ 3. Their Key Innovation: Candidate Pool UNION
This is the most important part.
Most systems:
Plain text
1. Get top 100 via embeddings
2. Rerank
Problem:
If a good result is ranked 101 → it’s gone forever
✅ Their approach:
They merge multiple candidate pools:
SQL
SELECT * FROM (
  vector_results
  UNION
  text_results
  UNION
  title_results
) candidates
ORDER BY final_score DESC;
💡 Why this is powerful
Each method finds different results:
Source
Strength
Vector
Meaning
Text
Exact match
Title
Navigation
👉 Combined = coverage + accuracy
🎯 4. Their Ranking Strategy (This is where the magic is)
They compute a final score like:
Plain text
score =
  vec_sim
  + 0.3 * text_score
  + 0.4 * priority
  + title_bonus
Let’s map this to YOUR system:
From your doc:
You already plan:
Boost popularity ✅
Boost freshness ✅
Boost relevance ✅ �
Recommendation System.md None
Upgrade it to:
Plain text
final_score =
  (semantic_similarity * 1.0)
+ (keyword_score * 0.3)
+ (popularity * 0.4)
+ (freshness * 0.2)
+ (title_match_bonus)
🏗️ 5. How This Fits Your Current Architecture
You already have:
✅ Embedding pipeline
Chunking
pgvector
HNSW index
Async processing
❌ Missing:
Full-text search (tsvector)
Hybrid ranking
Candidate union
⚙️ 6. What You Should Add (Concrete Plan)
Step 1 — Add Full-Text Search
In Postgres:
SQL
ALTER TABLE teachings ADD COLUMN search_vector tsvector;

UPDATE teachings SET search_vector =
  to_tsvector('english', coalesce(title, '') || ' ' || coalesce(summary, '') || ' ' || coalesce(message, ''));

CREATE INDEX idx_search_vector ON teachings USING GIN(search_vector);
Step 2 — Query BOTH systems
A. Semantic (you already have)
SQL
SELECT id, 1 - (embedding <=> :query_embedding) AS vec_score
FROM teaching_chunks
ORDER BY embedding <=> :query_embedding
LIMIT 50;
B. Keyword
SQL
SELECT id, ts_rank(search_vector, plainto_tsquery(:query)) AS text_score
FROM teachings
WHERE search_vector @@ plainto_tsquery(:query)
LIMIT 50;
C. Title Boost
SQL
SELECT id, 1.0 AS title_score
FROM teachings
WHERE title ILIKE '%' || :query || '%'
Step 3 — UNION them
SQL
WITH combined AS (
  SELECT id, vec_score, 0 AS text_score, 0 AS title_score FROM vector_results
  UNION
  SELECT id, 0, text_score, 0 FROM text_results
  UNION
  SELECT id, 0, 0, title_score FROM title_results
)
SELECT id,
       SUM(vec_score) +
       0.3 * SUM(text_score) +
       0.5 * SUM(title_score) AS final_score
FROM combined
GROUP BY id
ORDER BY final_score DESC
LIMIT 20;
🔁 7. How This Improves Your App
🔍 Search Feature
Before:
Only “meaning-based”
After:
Meaning + exact scripture/topic lookup
❤️ Recommendation System (Your existing design)
From your doc:
“Similarity is not search” �
Recommendation System.md None
Correct.
But now:
You can enhance recommendations:
Before:
Only embeddings
After:
Plain text
recommendation_score =
  semantic_similarity
+ engagement_score
+ keyword_overlap
+ recency
⚠️ 8. Important Tradeoffs (Don’t ignore this)
The writeup hints at something critical:
“We spent more time tuning weights than building the system”
Reality:
This is NOT plug-and-play
You must:
test queries
adjust weights
observe user behavior
🧠 9. Where You Should Be Careful
Let me challenge your current design a bit:
❌ Weak assumption in your system
“Semantic is enough for recommendations”
Not always.
Example:
User listens to “Faith”
Embeddings suggest “Hope”
But user actually wants:
“Faith confessions”
“Faith scriptures”
👉 Keyword + topic signals matter
🚀 10. Final Architecture (For Your App)
🔍 Search
Hybrid (vector + tsvector + title)
UNION candidates
Weighted ranking
❤️ Recommendations
Vector similarity (primary)
engagement signals
keyword/topic overlap
freshness
💡 11. Big Picture Insight
What they built is essentially:
Search = Retrieval (multi-source) + Ranking (weighted)
Your system currently has:
Retrieval = vector only
You need:
Retrieval = vector + lexical + metadata
👉 If you want next step
I can help you:
Write a Spring Boot repository layer for hybrid search
Design a clean SQL + JPA/native query setup
Or integrate this into your existing recommendation module
Just tell me 👍