package org.eni.koinoniadaily.modules.teachingchunk;

public enum EmbeddingStatus {
    PENDING,        // Default for newly created TeachingChunk
    PROCESSING,     // Set right before sending to the embedding provider
    EMBEDDED,       // Set right after the provider responses and embedding is saved
    FAILED          // Set when embedding provider response fails
}
