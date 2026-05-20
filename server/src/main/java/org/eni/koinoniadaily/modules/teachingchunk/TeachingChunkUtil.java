package org.eni.koinoniadaily.modules.teachingchunk;

import jakarta.validation.constraints.NotNull;
import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.modules.teachingchunk.dto.ChunkCandidate;
import org.eni.koinoniadaily.modules.teachingchunk.dto.MarkdownSection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TeachingChunkUtil {

  // Matches any level header: # ## ### etc.
  private static final Pattern HEADER_PATTERN =
      Pattern.compile("(?m)^(#{1,6}\\s+.+)$");

  private final int maxChunkChars;
  private final int overlapChars;

  public TeachingChunkUtil(AppProperties appProperties) {

    // Roughly 1200 tokens ≈ 4800 characters (4 chars/token estimate)
    this.maxChunkChars = appProperties.getEmbedding().getMaxChunkToken() * 4;
    this.overlapChars = appProperties.getEmbedding().getOverlapToken() * 4;
  }

  public List<ChunkCandidate> chunk(@NotNull String markdown) {

    // 1. Split into sections
    List<MarkdownSection> sections = split(markdown);

    // 2. Build chunks from sections
    return buildChunkCandidates(sections);
  }

  public List<MarkdownSection> split(String markdown) {

    List<MarkdownSection> sections = new ArrayList<>();
    Matcher matcher = HEADER_PATTERN.matcher(markdown);

    int lastMatchStart = 0;
    String currentTitle = "Introduction"; // content before first header

    while (matcher.find()) {
      int headerStart = matcher.start();

      // Capture everything between previous header and this one
      if (headerStart > lastMatchStart) {
        String content = markdown.substring(lastMatchStart, headerStart).strip();
        if (!content.isEmpty()) {
          sections.add(new MarkdownSection(currentTitle, content, lastMatchStart));
        }
      }

      currentTitle = extractTitle(matcher.group(1));
      lastMatchStart = headerStart;
    }

    // Capture the final section (after last header)
    String remaining = markdown.substring(lastMatchStart).strip();
    if (!remaining.isEmpty()) {
      sections.add(new MarkdownSection(currentTitle, remaining, lastMatchStart));
    }

    return sections;
  }

  private String extractTitle(String headerLine) {
    // Strip the leading # characters and whitespace
    return headerLine.replaceAll("^#{1,6}\\s+", "").strip();
  }

  private List<ChunkCandidate> buildChunkCandidates(List<MarkdownSection> sections) {

    List<ChunkCandidate> candidates = new ArrayList<>();
    int chunkIndex = 0;

    for (MarkdownSection section : sections) {

      if (section.content().length() <= maxChunkChars) {
        // Section fits in one chunk
        candidates.add(new ChunkCandidate(
            chunkIndex++, section.title(), section.content(), section.startOffset()
        ));
      } else {
        // Section too large — sub-split with overlap
        List<ChunkCandidate> subChunks = subSplit(
            section, chunkIndex
        );
        candidates.addAll(subChunks);
        chunkIndex += subChunks.size();
      }
    }

    return candidates;
  }

  private List<ChunkCandidate> subSplit(
      MarkdownSection section, int startIndex) {

    List<ChunkCandidate> result = new ArrayList<>();
    String text = section.content();
    int cursor = 0;
    int localIndex = startIndex;

    while (cursor < text.length()) {
      int end = getEnd(cursor, text);

      String chunk = text.substring(cursor, end).strip();
      int absoluteStart = section.startOffset() + cursor;

      if (!chunk.isEmpty()) {
        result.add(new ChunkCandidate(
            localIndex++, section.title(), chunk, absoluteStart
        ));
      }

      // Move cursor forward with overlap
      cursor = Math.max(end - overlapChars, cursor + 1);
    }

    return result;
  }

  private int getEnd(int cursor, String text) {
    int end = Math.min(cursor + maxChunkChars, text.length());

    // Try to break at a paragraph boundary within the window
    if (end < text.length()) {
      int paragraphBreak = text.lastIndexOf("\n\n", end);
      if (paragraphBreak > cursor) {
        end = paragraphBreak;
      } else {
        // Fall back to line break
        int lineBreak = text.lastIndexOf("\n", end);
        if (lineBreak > cursor) {
          end = lineBreak;
        }
        // Otherwise just cut at MAX_CHUNK_CHARS
      }
    }
    return end;
  }
}
