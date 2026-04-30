#Recommendations (context-driven, system-initiated)
While viewing Teaching A:
- “People who listened to this also liked…”
- “Similar teachings you may enjoy”

~~Similarity is not search~~.
Here, the question is:
*“If someone liked this teaching, what else feels spiritually aligned?”*
**This strongly favors semantic embeddings.**
- Similarity = distance in vector space
- Recommendations = nearest neighbors
###### Source
- gotten from the teaching entity (markdown)
- embeddings are generated when Teaching is uploaded with the shouldEmbed flag on or when later manually triggered via dedicated endpoint.
- relevant fields are : 
	- title
	- summary
	- message

Recommendation system could be progressively enhanced by engagement signals such as 
- views
- likes
- bookmarks
- Histories

2 categories in Recommendation System
1. People who listened to this also liked....
	- built from user behaviour / engagement signals.
2. Similar teachings you may enjoy....
	- built from embeddings similarity and relevance.
N.B Worth noting that if Teaching is a series, other Teachings in series take precedence in recommendations.

*Note that doesn't have any lexical (keyword) search system. Only semantics(meaning-based/context-aware)*

**QUESTION**: should the recommendations be computed & stored on Teaching upload/update or computed in real time with requests. 
##### How do we order and suggest results?
This is where you:
- Blend relevance scores (embeddings ranks)
- Boost freshness (Latest uploaded/Recently accessed)
- Boost popularity (Most frequently accessed/bookmarked)
- Penalize near-duplicates
- Boost topic overlap
- Boost by frequency of same teaching chunks
- Exclude series in results of series teachings

This layer evolves over time by feedback loops


### Spring Boot–specific architectural thinking
- PostgreSQL + pgvector (vector storage)
- LangChain4j (orchestration, chunking, embeddings)
- OpenAI text-embedding-3-small (1536 dimensions, $0.02 per 1M tokens)
- Spring Boot async for embedding pipeline


**Recommendation module**
- Similarity computation
- Caching
**Content indexing pipeline**
- Runs async when a teaching is added/updated
- Indexing should be event-driven, not request-driven
- Index Strategy
	pgvector indexes:
	-- *HNSW index (faster reads, slower writes)*
	`CREATE INDEX ON teachings USING hnsw (message_embedding vector_cosine_ops);`
	
	-- *IVFFlat index (faster writes, good reads)*
	`CREATE INDEX ON teachings USING ivfflat (message_embedding vector_cosine_ops);`
	Recommendation: HNSW because:
	- You write rarely (new teachings are infrequent)
	- Read-heavy workload (search queries)
	- Better accuracy

##### Database Schema

```
CREATE TABLE teaching_chunks (
  id BIGSERIAL PRIMARY KEY,
  teaching_id BIGINT NOT NULL REFERENCES teachings(id) ON DELETE CASCADE,
  
  -- Chunk content
  content TEXT NOT NULL,
  chunk_index INT NOT NULL,-- Order within teaching
  
  -- Metadata for search results display
  section_title VARCHAR(255),
  character_start INT,
  character_end INT,
  
  -- Vector embedding
  embedding vector(1536),
  
  -- Timestamps
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  
  CONSTRAINT unique_teaching_chunk UNIQUE(teaching_id, chunk_index)
);
```
-- HNSW index for fast vector search
```
CREATE INDEX teaching_chunks_embedding_idx 
  ON teaching_chunks 
  USING hnsw (embedding vector_cosine_ops);
```

-- Index for teaching lookup
```
CREATE INDEX teaching_chunks_teaching_id_idx 
  ON teaching_chunks(teaching_id);
```
2. Update teachings table
```
ALTER TABLE teachings 
  ADD COLUMN embedding_status VARCHAR(20) DEFAULT 'pending',
  ADD COLUMN embedding_triggered_at TIMESTAMP,
```

-- `Status values: 'pending', 'in_progress', 'completed', 'failed'`


#### Embedding Pipeline
##### Workflow
Admin/System Action
    ↓
[Toggle transcript.should_embed = true]
    ↓
[Async Job Triggered]
    ↓
┌──────────────────────────────────────
│ Embedding Pipeline                      
├──────────────────────────────────────
│ 1. Update teaching.embedding_status = 'in_progress' & embedding_triggered_at = NOW()                     
│                                         
│ 2. Fetch transcript                     
│                            l             
│ 3. LangChain4j RecursiveSplitter→ Generate chunks with metadata      
│                                         
│ 4. Batch embed chunks (OpenAI API) → 20 chunks per request (parallel)   
│                                         
│ 5. Save chunks to teaching_chunks (prefer upsert to create incase of retry/rechunking: upsert by chunk_index)       
│                                         
│ 6. Embed summary (for whole-teaching    
│    semantic search)                     
│                                         
│ 7. Update teaching:                     
│    - embedding_status = 'completed'         
│                                         
│ 8. Reset transcript.should_embed=false  
└──────────────────────────────────────
##### Error Handling
If embedding fails:
  - Set embedding_status = 'failed'
  - Log error details
  - Notify admin (optional)
  - Keep should_embed = true for manual retry


#### LangChain4j Integration Points
Components to Use
1. Document Splitter:
```
	DocumentSplitter splitter = DocumentSplitters.recursive(
	  1000,  // chunk size in tokens
	  200    // overlap
	);
```
2. Embedding Model:
```
	EmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
	  .apiKey(System.getenv("OPENAI_API_KEY"))
	  .modelName("text-embedding-3-small")
	  .build();
```
3. Embedding Store:
	// Custom implementation backed by PostgreSQL + pgvector
	// LangChain4j has PgVectorEmbeddingStore but you might need custom
Trade-off: LangChain4j's PgVectorEmbeddingStore vs Custom Implementation
	- LangChain4j's store: Quick setup, standardized
	- Custom: Better integration with existing schema, more control
	- Recommendation: Start with custom since you have specific schema needs (chunk metadata, teaching relationships

#### Semantic Chunking with Markdown Awareness
Instead of arbitrary token limits, use structure-aware chunking:
// LangChain4j configuration
```
DocumentSplitter splitter = DocumentSplitters.recursive(
  1200,  // max chunk size in tokens
  150,   // overlap tokens
  new String[] {
    "\n## ",      // H2 headers (primary split points)
    "\n### ",     // H3 headers (secondary)
    "\n#### ",    // H4 headers
    "\n\n",       // Paragraph boundaries
    "\n",         // Line breaks
    ". ",         // Sentences (last resort)
  }
);
```
**How it works:**
- First tries to split at H2 headers - natural topic boundaries
- If section too large (>1200 tokens), splits at H3 within that section
- If still too large, splits at paragraph breaks
- Always maintains 150-token overlap to preserve context across boundaries
**Why this works:**
- Respects your existing markdown structure
- Chunks align with actual teaching sections
- Section titles become chunk metadata naturally
- Maintains semantic coherence

###### Large section requiring split
**Power of the Holy Spirit**
[2000 tokens - exceeds max]
**Result: Split at paragraph boundaries**
- Chunk 1: First ~1200 tokens of section
- Chunk 2: Remaining ~800 tokens + 150 overlap from Chunk 1
- Both chunks tagged with same section title: "Power of the Holy Spirit"

###### **Embedding Status Enum**
```
package org.eni.koinoniadaily.modules.teaching;

public enum EmbeddingStatus {
  PENDING,
  IN_PROGRESS,
  COMPLETED,
  FAILED
}
```
###### **Updated Teaching Entity**
```
@Entity
@Table(name = "teachings")
public class Teaching extends BaseEntity {
  
  // ... existing fields ...
  
  @Column(name = "embedding_status", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private EmbeddingStatus embeddingStatus = EmbeddingStatus.PENDING;
  
  @Column(name = "embedding_triggered_at")
  private LocalDateTime embeddingTriggeredAt;
}
```
###### **Updated Transcript Entity**
```
@Entity
@Table(name = "transcripts")
public class Transcript extends BaseEntity {
  
  @Column(nullable = false, columnDefinition = "text")
  private String message;
  
  @Column(name = "should_embed", nullable = false)
  @Builder.Default
  private boolean shouldEmbed = false;
  
  @Column(name = "is_formatted", nullable = false)
  @Builder.Default
  private boolean isFormatted = false;
}
```
###### **TeachingChunk Entity**
```
package org.eni.koinoniadaily.modules.teaching;

import org.eni.koinoniadaily.entity.BaseEntity;
import com.pgvector.PGvector;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "teaching_chunks")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TeachingChunk extends BaseEntity {
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teaching_id", nullable = false)
  private Teaching teaching;
  
  @Column(nullable = false, columnDefinition = "text")
  private String content;
  
  @Column(name = "chunk_index", nullable = false)
  private int chunkIndex;
  
  @Column(name = "section_title", length = 255)
  private String sectionTitle;  // Extracted from markdown header
  
  @Column(name = "character_start")
  private int characterStart;
  
  @Column(name = "character_end")
  private int characterEnd;
  
  @Column(columnDefinition = "vector(1536)")
  private PGvector embedding;
}
```

###### Embedding Pipeline with Markdown Parsing
**Markdown Metadata Extraction**
```
@Service
@RequiredArgsConstructor
public class ChunkingService {
  
  private final EmbeddingModel embeddingModel;
  private final TeachingChunkRepository chunkRepository;
  
  @Transactional
  public void processAndEmbedTranscript(Teaching teaching, String markdownContent) {
    
    // 1. Parse markdown and create structured document
    Document document = parseMarkdown(markdownContent);
    
    // 2. Split into semantic chunks
    DocumentSplitter splitter = DocumentSplitters.recursive(
      1200,
      150,
      new String[] {"\n## ", "\n### ", "\n#### ", "\n\n", "\n", ". "}
    );
    
    List<TextSegment> segments = splitter.split(document);
    
    // 3. Process each chunk
    List<TeachingChunk> chunks = new ArrayList<>();
    for (int i = 0; i < segments.size(); i++) {
      TextSegment segment = segments.get(i);
      
      // Extract section title from markdown
      String sectionTitle = extractSectionTitle(segment.text());
      
      // Generate embedding
      Embedding embedding = embeddingModel.embed(segment.text()).content();
      
      // Create chunk entity
      TeachingChunk chunk = TeachingChunk.builder()
        .teaching(teaching)
        .content(segment.text())
        .chunkIndex(i)
        .sectionTitle(sectionTitle)
        .characterStart(segment.metadata().get("start_position"))
        .characterEnd(segment.metadata().get("end_position"))
        .embedding(new PGvector(embedding.vector()))
        .build();
      
      chunks.add(chunk);
    }
    
    // 4. Batch save
    chunkRepository.saveAll(chunks);
    
    // 5. Update teaching status
    teaching.setEmbeddingStatus(EmbeddingStatus.COMPLETED);
    teaching.setEmbeddingReady(true);
    teaching.setLastEmbeddedAt(LocalDateTime.now());
  }
  
  private String extractSectionTitle(String chunkText) {
    // Extract markdown header from beginning of chunk
    Pattern headerPattern = Pattern.compile("^#{1,6}\\s+(.+)$", Pattern.MULTILINE);
    Matcher matcher = headerPattern.matcher(chunkText);
    
    if (matcher.find()) {
      return matcher.group(1).trim();
    }
    
    return null;  // No header found
  }
  
  private Document parseMarkdown(String markdown) {
    // LangChain4j Document with metadata
    return new Document(markdown);
  }
}
```
###### **Admin Embedding Interface**
Controller Endpoints
```
@RestController
@RequestMapping("/api/admin/embeddings")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class EmbeddingAdminController {
  
  private final EmbeddingService embeddingService;
  
  // Trigger embedding for specific teaching
  @PostMapping("/teachings/{id}/trigger")
  public ResponseEntity<SuccessResponse<Void>> triggerEmbedding(@PathVariable Long id) {
    embeddingService.triggerEmbedding(id);
    return ResponseEntity.ok(SuccessResponse.message("Embedding job queued"));
  }
  
  // Bulk trigger
  @PostMapping("/teachings/bulk-trigger")
  public ResponseEntity<SuccessResponse<Void>> bulkTrigger(
    @RequestBody BulkEmbeddingRequest request
  ) {
    embeddingService.bulkTriggerEmbeddings(request.getTeachingIds());
    return ResponseEntity.ok(
      SuccessResponse.message(request.getTeachingIds().size() + " teachings queued")
    );
  }
  
  // Get embedding status
  @GetMapping("/teachings/{id}/status")
  public ResponseEntity<SuccessResponse<EmbeddingStatusResponse>> getStatus(@PathVariable Long id) {
    EmbeddingStatusResponse status = embeddingService.getEmbeddingStatus(id);
    return ResponseEntity.ok(SuccessResponse.data(status));
  }
  
  // List all pending/in-progress embeddings
  @GetMapping("/queue")
  public ResponseEntity<SuccessResponse<List<EmbeddingQueueItem>>> getQueue() {
    List<EmbeddingQueueItem> queue = embeddingService.getEmbeddingQueue();
    return ResponseEntity.ok(SuccessResponse.data(queue));
  }
  
  // Re-embed (delete old chunks, create new)
  @PostMapping("/teachings/{id}/re-embed")
  public ResponseEntity<SuccessResponse<Void>> reEmbed(@PathVariable Long id) {
    embeddingService.reEmbed(id);
    return ResponseEntity.ok(SuccessResponse.message("Re-embedding job queued"));
  }
}
```

###### **Response DTOs**
```
@Data
@Builder
public class EmbeddingStatusResponse {
  private Long teachingId;
  private EmbeddingStatus status;
  private boolean embeddingReady;
  private Integer totalChunks;
  private LocalDateTime embeddingTriggeredAt;
  private LocalDateTime lastEmbeddedAt;
  private String errorMessage;  // If status = FAILED
}
```

```
@Data
@Builder
public class EmbeddingQueueItem {
  private Long teachingId;
  private String title;
  private EmbeddingStatus status;
  private String progress;  // e.g., "12/28 chunks"
  private LocalDateTime triggeredAt;
}
```

###### **Async Embedding Service**
```
@Service
@RequiredArgsConstructor
public class EmbeddingService {
  
  private final TeachingRepository teachingRepository;
  private final TranscriptRepository transcriptRepository;
  private final TeachingChunkRepository chunkRepository;
  private final ChunkingService chunkingService;
  
  @Async
  @Transactional
  public void triggerEmbedding(Long teachingId) {
    
    Teaching teaching = teachingRepository.findById(teachingId)
      .orElseThrow(() -> new NotFoundException("Teaching not found"));
    
    Transcript transcript = teaching.getTranscript();
    if (transcript == null) {
      throw new ValidationException("No transcript found for teaching");
    }
    
    try {
      // Update status
      teaching.setEmbeddingStatus(EmbeddingStatus.IN_PROGRESS);
      teaching.setEmbeddingTriggeredAt(LocalDateTime.now());
      teachingRepository.save(teaching);
      
      // Process and embed
      chunkingService.processAndEmbedTranscript(teaching, transcript.getMessage());
      
      // Reset trigger flag
      transcript.setShouldEmbed(false);
      
    } catch (Exception e) {
      teaching.setEmbeddingStatus(EmbeddingStatus.FAILED);
      teachingRepository.save(teaching);
      throw e;
    }
  }
  
  @Transactional
  public void reEmbed(Long teachingId) {
    // Delete existing chunks
    chunkRepository.deleteByTeachingId(teachingId);
    
    // Reset status
    Teaching teaching = teachingRepository.findById(teachingId)
      .orElseThrow(() -> new NotFoundException("Teaching not found"));
    
    teaching.setEmbeddingStatus(EmbeddingStatus.PENDING);
    teaching.setEmbeddingReady(false);
    
    // Trigger new embedding
    triggerEmbedding(teachingId);
  }
}
```

