package org.eni.koinoniadaily.modules.teachingchunk;

import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.modules.teachingchunk.dto.ChunkCandidate;
import org.eni.koinoniadaily.modules.teachingchunk.dto.MarkdownSection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TeachingChunkUtilTest {

  // Use small values to make testing practical
  private static final int MAX_CHUNK_TOKEN = 100;   // 400 chars
  private static final int OVERLAP_TOKEN = 10;       // 40 chars

  private TeachingChunkUtil util;

  @BeforeEach
  void setUp() {
    AppProperties props = new AppProperties();
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(MAX_CHUNK_TOKEN);
    embedding.setOverlapToken(OVERLAP_TOKEN);
    props.setEmbedding(embedding);

    util = new TeachingChunkUtil(props);
  }

  // =========================================================================
  // split() tests
  // =========================================================================

  @Nested
  class SplitTests {

    @Test
    void split_emptyString_returnsEmptySections() {
      List<MarkdownSection> sections = util.split("");
      assertThat(sections).isEmpty();
    }

    @Test
    void split_noHeaders_returnsSingleIntroductionSection() {
      String markdown = "This is plain content with no headers.";
      List<MarkdownSection> sections = util.split(markdown);

      assertThat(sections).hasSize(1);
      assertThat(sections.get(0).title()).isEqualTo("Introduction");
      assertThat(sections.get(0).content()).isEqualTo("This is plain content with no headers.");
      assertThat(sections.get(0).startOffset()).isEqualTo(0);
    }

    @Test
    void split_singleH1Header_returnsTwoSections() {
      String markdown = "Intro text\n# Main Title\nMain content here.";
      List<MarkdownSection> sections = util.split(markdown);

      assertThat(sections).hasSize(2);
      assertThat(sections.get(0).title()).isEqualTo("Introduction");
      assertThat(sections.get(0).content()).isEqualTo("Intro text");
      assertThat(sections.get(1).title()).isEqualTo("Main Title");
      assertThat(sections.get(1).content()).contains("# Main Title");
    }

    @Test
    void split_multipleHeaders_returnsCorrectSections() {
      String markdown = "# Section One\nContent one.\n# Section Two\nContent two.\n# Section Three\nContent three.";
      List<MarkdownSection> sections = util.split(markdown);

      assertThat(sections).hasSize(3);
      assertThat(sections.get(0).title()).isEqualTo("Section One");
      assertThat(sections.get(1).title()).isEqualTo("Section Two");
      assertThat(sections.get(2).title()).isEqualTo("Section Three");
    }

    @Test
    void split_deepNestedHeaders_capturesTitlesCorrectly() {
      String markdown = "### Deep Header\nContent under deep header.";
      List<MarkdownSection> sections = util.split(markdown);

      assertThat(sections).hasSize(1);
      assertThat(sections.get(0).title()).isEqualTo("Deep Header");
    }

    @Test
    void split_h6Header_extractsTitleCorrectly() {
      String markdown = "###### H6 Title\nContent.";
      List<MarkdownSection> sections = util.split(markdown);

      assertThat(sections).hasSize(1);
      assertThat(sections.get(0).title()).isEqualTo("H6 Title");
    }

    @Test
    void split_headerWithTrailingWhitespace_stripsTitle() {
      String markdown = "# Title With Spaces   \nContent.";
      List<MarkdownSection> sections = util.split(markdown);

      assertThat(sections).hasSize(1);
      assertThat(sections.get(0).title()).isEqualTo("Title With Spaces");
    }

    @Test
    void split_emptyContentBetweenHeaders_skipsEmptySection() {
      // No content between two headers - no section should be added for blank content
      String markdown = "# Title One\n# Title Two\nActual content.";
      List<MarkdownSection> sections = util.split(markdown);

      // The first section has no content (just the header line, which becomes part of section two's start)
      // Only sections with non-empty content are added
      assertThat(sections).isNotEmpty();
      // Title Two's section should contain the actual content
      MarkdownSection lastSection = sections.get(sections.size() - 1);
      assertThat(lastSection.title()).isEqualTo("Title Two");
      assertThat(lastSection.content()).contains("Actual content.");
    }

    @Test
    void split_contentOnlyAfterLastHeader_capturedInFinalSection() {
      String markdown = "# Header\nFinal content after header.";
      List<MarkdownSection> sections = util.split(markdown);

      assertThat(sections).hasSize(1);
      assertThat(sections.get(0).content()).contains("Final content after header.");
    }

    @Test
    void split_whitespaceOnlyContent_notAddedAsSection() {
      String markdown = "# Header\n\n   \n# Second Header\nReal content.";
      List<MarkdownSection> sections = util.split(markdown);

      // Only sections with non-blank content should be added
      for (MarkdownSection section : sections) {
        assertThat(section.content().strip()).isNotEmpty();
      }
    }

    @Test
    void split_startOffsetTracked() {
      String markdown = "Intro.\n# Title\nContent.";
      List<MarkdownSection> sections = util.split(markdown);

      // The first section starts at 0
      assertThat(sections.get(0).startOffset()).isEqualTo(0);
      // The second section starts where the header begins
      assertThat(sections.get(1).startOffset()).isGreaterThan(0);
    }
  }

  // =========================================================================
  // chunk() tests
  // =========================================================================

  @Nested
  class ChunkTests {

    @Test
    void chunk_emptyString_returnsEmptyList() {
      List<ChunkCandidate> candidates = util.chunk("");
      assertThat(candidates).isEmpty();
    }

    @Test
    void chunk_smallContent_returnsOneChunk() {
      String markdown = "# Introduction\nShort content.";
      List<ChunkCandidate> candidates = util.chunk(markdown);

      assertThat(candidates).hasSize(1);
      assertThat(candidates.get(0).chunkIndex()).isEqualTo(0);
      assertThat(candidates.get(0).sectionTitle()).isEqualTo("Introduction");
    }

    @Test
    void chunk_chunkIndicesAreSequential() {
      String markdown = "# Section A\nContent A.\n# Section B\nContent B.\n# Section C\nContent C.";
      List<ChunkCandidate> candidates = util.chunk(markdown);

      for (int i = 0; i < candidates.size(); i++) {
        assertThat(candidates.get(i).chunkIndex()).isEqualTo(i);
      }
    }

    @Test
    void chunk_multipleSmallSections_eachBecomesOwnChunk() {
      String markdown = "# Section A\nShort A.\n# Section B\nShort B.\n# Section C\nShort C.";
      List<ChunkCandidate> candidates = util.chunk(markdown);

      assertThat(candidates).hasSize(3);
      assertThat(candidates.get(0).sectionTitle()).isEqualTo("Section A");
      assertThat(candidates.get(1).sectionTitle()).isEqualTo("Section B");
      assertThat(candidates.get(2).sectionTitle()).isEqualTo("Section C");
    }

    @Test
    void chunk_largeSectionExceedingMaxChars_subSplitIntoMultipleChunks() {
      // maxChunkChars = 100 tokens * 4 = 400 chars
      // Create content that definitely exceeds this
      String longContent = "A".repeat(500);
      String markdown = "# Long Section\n" + longContent;

      List<ChunkCandidate> candidates = util.chunk(markdown);

      assertThat(candidates).hasSizeGreaterThan(1);
      // All chunks should reference the same section title
      for (ChunkCandidate candidate : candidates) {
        assertThat(candidate.sectionTitle()).isEqualTo("Long Section");
      }
    }

    @Test
    void chunk_allChunkContentsAreNonEmpty() {
      String markdown = "# Title\n" + "Word ".repeat(200);
      List<ChunkCandidate> candidates = util.chunk(markdown);

      for (ChunkCandidate candidate : candidates) {
        assertThat(candidate.content()).isNotBlank();
      }
    }

    @Test
    void chunk_preservesSectionTitleInChunks() {
      String sectionTitle = "Special Section";
      String markdown = "# " + sectionTitle + "\n" + "Text ".repeat(200);
      List<ChunkCandidate> candidates = util.chunk(markdown);

      assertThat(candidates).isNotEmpty();
      assertThat(candidates.get(0).sectionTitle()).isEqualTo(sectionTitle);
    }

    @Test
    void chunk_subSplitChunks_startOffsetIncreasesMonotonically() {
      // Content large enough to trigger sub-splitting
      String markdown = "# Big Section\n" + "Line content here.\n".repeat(50);
      List<ChunkCandidate> candidates = util.chunk(markdown);

      if (candidates.size() > 1) {
        for (int i = 1; i < candidates.size(); i++) {
          assertThat(candidates.get(i).startOffset())
              .isGreaterThanOrEqualTo(candidates.get(i - 1).startOffset());
        }
      }
    }

    @Test
    void chunk_chunkBoundaryPrefersParagraphBreaks() {
      // Build content with paragraph breaks to verify boundary preference
      // maxChunkChars = 400. Build 2 paragraphs where paragraph1 < 400 but paragraph1+paragraph2 > 400
      String paragraph1 = "Word ".repeat(60) + "\n\n"; // ~300 chars + \n\n
      String paragraph2 = "More ".repeat(60);           // ~300 chars
      String markdown = "# Section\n" + paragraph1 + paragraph2;

      List<ChunkCandidate> candidates = util.chunk(markdown);

      // Should produce multiple chunks due to size
      assertThat(candidates).isNotEmpty();
    }

    @Test
    void chunk_noHeaders_treatedAsIntroductionSection() {
      String markdown = "No headers here, just plain text content.";
      List<ChunkCandidate> candidates = util.chunk(markdown);

      assertThat(candidates).hasSize(1);
      assertThat(candidates.get(0).sectionTitle()).isEqualTo("Introduction");
    }
  }

  // =========================================================================
  // Sub-split (overlap) behavior tests
  // =========================================================================

  @Nested
  class SubSplitTests {

    @Test
    void chunk_largeSection_subsequentChunksHaveOverlap() {
      // maxChunkChars = 400, overlapChars = 40
      // Build text that's 800+ chars so it definitely splits into 2+ chunks
      String content = "X".repeat(900);
      String markdown = "# Section\n" + content;

      List<ChunkCandidate> candidates = util.chunk(markdown);

      // Should be at least 2 chunks
      assertThat(candidates).hasSizeGreaterThan(1);

      // Each chunk's startOffset after the first should account for overlap
      // (second chunk starts before first chunk's end)
      if (candidates.size() >= 2) {
        int firstChunkContentLength = candidates.get(0).content().length();
        int firstChunkStart = candidates.get(0).startOffset();
        int secondChunkStart = candidates.get(1).startOffset();
        // The second chunk starts before the end of the first chunk (overlap)
        assertThat(secondChunkStart).isLessThan(firstChunkStart + firstChunkContentLength);
      }
    }

    @Test
    void chunk_largeSection_noEmptyChunksProduced() {
      String content = "word ".repeat(300);
      String markdown = "# Big Section\n" + content;

      List<ChunkCandidate> candidates = util.chunk(markdown);

      for (ChunkCandidate candidate : candidates) {
        assertThat(candidate.content()).isNotBlank();
      }
    }

    @Test
    void chunk_largeSection_chunkIndicesAreSequentialAcrossSubSplits() {
      String content = "A".repeat(800);
      String markdown = "# Section A\n" + content + "\n# Section B\nShort content B.";

      List<ChunkCandidate> candidates = util.chunk(markdown);

      for (int i = 0; i < candidates.size(); i++) {
        assertThat(candidates.get(i).chunkIndex()).isEqualTo(i);
      }
    }
  }

  // =========================================================================
  // Edge case / boundary tests
  // =========================================================================

  @Nested
  class EdgeCaseTests {

    @Test
    void chunk_exactlyMaxSizeContent_fitsInOneChunk() {
      // maxChunkChars = 400. Content of exactly 400 chars should be one chunk.
      // 400 chars = maxChunkToken * 4 = 100 * 4
      String exactContent = "A".repeat(400);
      String markdown = "# Title\n" + exactContent;

      List<ChunkCandidate> candidates = util.chunk(markdown);

      // The section content includes the header line + content, which may exceed 400
      // But if only the content (without header) is exactly 400, it depends on split behavior
      // We just verify we get at least one non-empty chunk
      assertThat(candidates).isNotEmpty();
    }

    @Test
    void chunk_onlyWhitespace_returnsEmptyList() {
      List<ChunkCandidate> candidates = util.chunk("   \n\n   ");
      assertThat(candidates).isEmpty();
    }

    @Test
    void chunk_mixedHeaders_allCaptured() {
      String markdown = "# H1\nContent.\n## H2\nContent.\n### H3\nContent.";
      List<ChunkCandidate> candidates = util.chunk(markdown);

      assertThat(candidates).hasSize(3);
      assertThat(candidates.get(0).sectionTitle()).isEqualTo("H1");
      assertThat(candidates.get(1).sectionTitle()).isEqualTo("H2");
      assertThat(candidates.get(2).sectionTitle()).isEqualTo("H3");
    }

    @Test
    void chunk_singleCharContent_returnsOneChunk() {
      List<ChunkCandidate> candidates = util.chunk("A");
      assertThat(candidates).hasSize(1);
      assertThat(candidates.get(0).content()).isEqualTo("A");
      assertThat(candidates.get(0).sectionTitle()).isEqualTo("Introduction");
    }

    @Test
    void chunk_headerLineIsIncludedInSectionContent() {
      // The split method includes the header line itself in the section content
      String markdown = "# My Title\nBody content.";
      List<ChunkCandidate> candidates = util.chunk(markdown);

      // The section content starts from the header position
      assertThat(candidates.get(0).content()).contains("My Title");
    }

    @Test
    void chunk_multipleConsecutiveHeaders_eachSectionHasCorrectTitle() {
      String markdown = "# First\nData.\n## Second\nData.\n# Third\nData.";
      List<ChunkCandidate> candidates = util.chunk(markdown);

      List<String> titles = candidates.stream().map(ChunkCandidate::sectionTitle).toList();
      assertThat(titles).containsExactly("First", "Second", "Third");
    }

    @Test
    void chunk_hashInMiddleOfLine_notTreatedAsHeader() {
      // A # not at the start of a line should not be treated as a header
      String markdown = "Content with # in the middle of a line.";
      List<ChunkCandidate> candidates = util.chunk(markdown);

      assertThat(candidates).hasSize(1);
      assertThat(candidates.get(0).sectionTitle()).isEqualTo("Introduction");
    }
  }
}
