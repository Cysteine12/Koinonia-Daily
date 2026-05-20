package org.eni.koinoniadaily.modules.teachingchunk.dto;

public record ChunkCandidate(
    int chunkIndex,
    String sectionTitle,
    String content,
    int startOffset
) {}
