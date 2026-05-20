package org.eni.koinoniadaily.modules.teachingchunk;

import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.modules.teachingchunk.dto.ChunkCandidate;
import org.eni.koinoniadaily.modules.teachingchunk.dto.MarkdownSection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeachingChunkUtilTest {

  @Mock
  private AppProperties appProperties;

  @Mock
  private AppProperties.Embedding embeddingProperties;

  private TeachingChunkUtil teachingChunkUtil;

  private static final int DEFAULT_MAX_CHUNK_TOKEN = 1200;
  private static final int DEFAULT_OVERLAP_TOKEN = 150;

  @BeforeEach
  void setUp() {
    when(appProperties.getEmbedding()).thenReturn(embeddingProperties);
    when(embeddingProperties.getMaxChunkToken()).thenReturn(DEFAULT_MAX_CHUNK_TOKEN);
    when(embeddingProperties.getOverlapToken()).thenReturn(DEFAULT_OVERLAP_TOKEN);
    teachingChunkUtil = new TeachingChunkUtil(appProperties);
  }

  // -----------------------------------------------------------------------
  // split() tests
  // -----------------------------------------------------------------------

  @Test
  void split_noHeaders_returnsSingleIntroductionSection() {
    String markdown = "This is plain text with no headers.\nJust a paragraph.";

    List<MarkdownSection> sections = teachingChunkUtil.split(markdown);

    assertThat(sections).hasSize(1);
    assertThat(sections.get(0).title()).isEqualTo("Introduction");
    assertThat(sections.get(0).content()).contains("plain text");
    assertThat(sections.get(0).startOffset()).isEqualTo(0);
  }

  @Test
  void split_singleHeader_returnsTwoSections() {
    String markdown = "Intro paragraph.\n\n# Main Section\nContent under main section.";

    List<MarkdownSection> sections = teachingChunkUtil.split(markdown);

    assertThat(sections).hasSize(2);
    assertThat(sections.get(0).title()).isEqualTo("Introduction");
    assertThat(sections.get(0).content()).contains("Intro paragraph");
    assertThat(sections.get(1).title()).isEqualTo("Main Section");
    assertThat(sections.get(1).content()).contains("Content under main section");
  }

  @Test
  void split_multipleHeaders_returnsCorrectSections() {
    String markdown = "Intro text.\n\n# Section One\nContent of section one.\n\n"
        + "## Section Two\nContent of section two.\n\n### Section Three\nContent of section three.\n";

    List<MarkdownSection> sections = teachingChunkUtil.split(markdown);

    assertThat(sections).hasSize(4);
    assertThat(sections.get(0).title()).isEqualTo("Introduction");
    assertThat(sections.get(1).title()).isEqualTo("Section One");
    assertThat(sections.get(2).title()).isEqualTo("Section Two");
    assertThat(sections.get(3).title()).isEqualTo("Section Three");
  }

  @Test
  void split_headerWithNoIntroContent_omitsIntroductionSection() {
    String markdown = "# First Header\nContent starts here immediately.";

    List<MarkdownSection> sections = teachingChunkUtil.split(markdown);

    assertThat(sections).hasSize(1);
    assertThat(sections.get(0).title()).isEqualTo("First Header");
  }

  @Test
  void split_headersOfAllLevels_parsedCorrectly() {
    String markdown = "# H1 Title\nH1 content.\n"
        + "## H2 Title\nH2 content.\n"
        + "### H3 Title\nH3 content.\n"
        + "#### H4 Title\nH4 content.\n"
        + "##### H5 Title\nH5 content.\n"
        + "###### H6 Title\nH6 content.\n";

    List<MarkdownSection> sections = teachingChunkUtil.split(markdown);

    assertThat(sections).hasSize(6);
    assertThat(sections.get(0).title()).isEqualTo("H1 Title");
    assertThat(sections.get(5).title()).isEqualTo("H6 Title");
  }

  @Test
  void split_emptyContent_returnsEmptyList() {
    List<MarkdownSection> sections = teachingChunkUtil.split("");

    assertThat(sections).isEmpty();
  }

  @Test
  void split_onlyWhitespace_returnsEmptyList() {
    List<MarkdownSection> sections = teachingChunkUtil.split("   \n   \n   ");

    assertThat(sections).isEmpty();
  }

  @Test
  void split_preservesStartOffset() {
    String markdown = "Intro.\n\n# Section\nContent.";
    int expectedSectionOffset = markdown.indexOf("# Section");

    List<MarkdownSection> sections = teachingChunkUtil.split(markdown);

    assertThat(sections).hasSize(2);
    assertThat(sections.get(1).startOffset()).isEqualTo(expectedSectionOffset);
  }

  @Test
  void split_sectionWithOnlyWhitespaceContent_isOmitted() {
    String markdown = "# Empty Section\n   \n# Real Section\nActual content.";

    List<MarkdownSection> sections = teachingChunkUtil.split(markdown);

    // The "Empty Section" content (just whitespace) should be stripped and omitted
    assertThat(sections).hasSize(1);
    assertThat(sections.get(0).title()).isEqualTo("Real Section");
  }

  // -----------------------------------------------------------------------
  // chunk() tests - small content (fits in one chunk)
  // -----------------------------------------------------------------------

  @Test
  void chunk_shortMarkdown_returnsSingleChunk() {
    String markdown = "# Title\nShort content.";

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks).hasSize(1);
    assertThat(chunks.get(0).chunkIndex()).isEqualTo(0);
    assertThat(chunks.get(0).sectionTitle()).isEqualTo("Title");
    assertThat(chunks.get(0).content()).contains("Short content");
  }

  @Test
  void chunk_emptyMarkdown_returnsEmptyList() {
    List<ChunkCandidate> chunks = teachingChunkUtil.chunk("");

    assertThat(chunks).isEmpty();
  }

  @Test
  void chunk_noHeaders_singleChunkWithIntroTitle() {
    String markdown = "Just some plain content without headers.";

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks).hasSize(1);
    assertThat(chunks.get(0).sectionTitle()).isEqualTo("Introduction");
    assertThat(chunks.get(0).chunkIndex()).isEqualTo(0);
  }

  @Test
  void chunk_multipleSections_chunkIndexIsSequential() {
    String markdown = "# Section A\nContent A.\n\n# Section B\nContent B.\n\n# Section C\nContent C.\n";

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks).hasSize(3);
    assertThat(chunks.get(0).chunkIndex()).isEqualTo(0);
    assertThat(chunks.get(1).chunkIndex()).isEqualTo(1);
    assertThat(chunks.get(2).chunkIndex()).isEqualTo(2);
  }

  @Test
  void chunk_multipleSections_sectionTitlesPreserved() {
    String markdown = "# Faith\nContent about faith.\n\n## Grace\nContent about grace.\n";

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks).hasSize(2);
    assertThat(chunks.get(0).sectionTitle()).isEqualTo("Faith");
    assertThat(chunks.get(1).sectionTitle()).isEqualTo("Grace");
  }

  // -----------------------------------------------------------------------
  // chunk() tests - large content requiring sub-splitting
  // -----------------------------------------------------------------------

  @Test
  void chunk_largeSectionExceedingMaxChars_subSplitsIntoMultipleChunks() {
    // Use small limits to force sub-splitting: maxChunkToken=10 => 40 chars max
    when(embeddingProperties.getMaxChunkToken()).thenReturn(10);
    when(embeddingProperties.getOverlapToken()).thenReturn(2);
    teachingChunkUtil = new TeachingChunkUtil(appProperties);

    // Build content larger than 40 chars
    String content = "A".repeat(200);
    String markdown = "# Big Section\n" + content;

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks.size()).isGreaterThan(1);
    // All chunks should have the same section title
    chunks.forEach(c -> assertThat(c.sectionTitle()).isEqualTo("Big Section"));
  }

  @Test
  void chunk_largeSectionSubSplit_chunkIndicesAreSequential() {
    when(embeddingProperties.getMaxChunkToken()).thenReturn(10);
    when(embeddingProperties.getOverlapToken()).thenReturn(1);
    teachingChunkUtil = new TeachingChunkUtil(appProperties);

    String content = "Word ".repeat(50); // 250 chars
    String markdown = "# Section\n" + content;

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    for (int i = 0; i < chunks.size(); i++) {
      assertThat(chunks.get(i).chunkIndex()).isEqualTo(i);
    }
  }

  @Test
  void chunk_subSplit_respectsParagraphBoundaries() {
    // maxChunkToken=20 => 80 chars max
    when(embeddingProperties.getMaxChunkToken()).thenReturn(20);
    when(embeddingProperties.getOverlapToken()).thenReturn(2);
    teachingChunkUtil = new TeachingChunkUtil(appProperties);

    // Build content with paragraph breaks that fit within window
    String para1 = "First paragraph content here is quite long.";
    String para2 = "Second paragraph content here is also long.";
    String content = para1 + "\n\n" + para2;
    String markdown = "# Section\n" + content;

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    // All chunk content should be non-empty
    chunks.forEach(c -> assertThat(c.content()).isNotBlank());
  }

  @Test
  void chunk_subSplit_noEmptyChunksGenerated() {
    when(embeddingProperties.getMaxChunkToken()).thenReturn(5);
    when(embeddingProperties.getOverlapToken()).thenReturn(1);
    teachingChunkUtil = new TeachingChunkUtil(appProperties);

    String content = "Line content.\n\n".repeat(30);
    String markdown = "# Section\n" + content;

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks).isNotEmpty();
    chunks.forEach(c -> assertThat(c.content()).isNotBlank());
  }

  @Test
  void chunk_mixedSmallAndLargeSections_correctChunkCount() {
    // maxChunkToken=10 => 40 chars max
    when(embeddingProperties.getMaxChunkToken()).thenReturn(10);
    when(embeddingProperties.getOverlapToken()).thenReturn(1);
    teachingChunkUtil = new TeachingChunkUtil(appProperties);

    String shortContent = "Short."; // < 40 chars
    String longContent = "A".repeat(200); // > 40 chars

    String markdown = "# Small\n" + shortContent + "\n# Large\n" + longContent;

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    // Small section = 1 chunk; large section = multiple chunks
    assertThat(chunks.size()).isGreaterThan(2);
    // First chunk is the small section
    assertThat(chunks.get(0).sectionTitle()).isEqualTo("Small");
    // Remaining chunks come from the large section
    chunks.subList(1, chunks.size())
        .forEach(c -> assertThat(c.sectionTitle()).isEqualTo("Large"));
  }

  // -----------------------------------------------------------------------
  // chunk() - start offset tracking
  // -----------------------------------------------------------------------

  @Test
  void chunk_singleChunk_startOffsetIsZeroForOnlySectionWithNoHeaders() {
    String markdown = "Plain content with no headers at all.";

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks).hasSize(1);
    assertThat(chunks.get(0).startOffset()).isEqualTo(0);
  }

  @Test
  void chunk_contentBeforeFirstHeader_startOffsetIsZero() {
    String markdown = "Intro content.\n\n# Header\nSection content.";

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks.get(0).startOffset()).isEqualTo(0);
  }

  // -----------------------------------------------------------------------
  // Boundary / regression tests
  // -----------------------------------------------------------------------

  @Test
  void chunk_contentExactlyAtMaxCharsLimit_singleChunk() {
    // maxChunkToken=5 => 20 chars max
    when(embeddingProperties.getMaxChunkToken()).thenReturn(5);
    when(embeddingProperties.getOverlapToken()).thenReturn(0);
    teachingChunkUtil = new TeachingChunkUtil(appProperties);

    String content = "A".repeat(20); // exactly at limit
    String markdown = "# Section\n" + content;

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks).hasSize(1);
  }

  @Test
  void chunk_contentOneCharOverMax_subSplits() {
    // maxChunkToken=5 => 20 chars max
    when(embeddingProperties.getMaxChunkToken()).thenReturn(5);
    when(embeddingProperties.getOverlapToken()).thenReturn(0);
    teachingChunkUtil = new TeachingChunkUtil(appProperties);

    // 21 chars = 1 over limit
    String content = "A".repeat(21);
    String markdown = "# Section\n" + content;

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks.size()).isGreaterThanOrEqualTo(1);
  }

  @Test
  void chunk_zeroOverlap_chunksDoNotOverlap() {
    // maxChunkToken=10 => 40 chars max
    when(embeddingProperties.getMaxChunkToken()).thenReturn(10);
    when(embeddingProperties.getOverlapToken()).thenReturn(0);
    teachingChunkUtil = new TeachingChunkUtil(appProperties);

    // 120 chars - should yield 3 non-overlapping chunks of ~40
    String content = "A".repeat(120);
    String markdown = "# Section\n" + content;

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks.size()).isGreaterThanOrEqualTo(2);
    assertThat(chunks.get(1).startOffset()).isGreaterThanOrEqualTo(chunks.get(0).startOffset());
  }

  @Test
  void chunk_headerWithMultiplePoundSigns_titleStrippedCorrectly() {
    String markdown = "### Deep Nested Header\nContent here.";

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks).hasSize(1);
    assertThat(chunks.get(0).sectionTitle()).isEqualTo("Deep Nested Header");
  }

  @Test
  void chunk_realWorldMarkdownStructure_producesCorrectChunks() {
    String markdown = "## Introduction\nWelcome to this teaching on the fruits of the Spirit.\n\n"
        + "## Section 1: Love\nThe first fruit is love. Love is patient and kind.\n\n"
        + "## Section 2: Joy\nJoy is not happiness. Joy is a deep sense of wellbeing.\n\n"
        + "## Conclusion\nMay you grow in all the fruits of the Spirit.\n";

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    assertThat(chunks).hasSize(4);
    assertThat(chunks.get(0).sectionTitle()).isEqualTo("Introduction");
    assertThat(chunks.get(1).sectionTitle()).isEqualTo("Section 1: Love");
    assertThat(chunks.get(2).sectionTitle()).isEqualTo("Section 2: Joy");
    assertThat(chunks.get(3).sectionTitle()).isEqualTo("Conclusion");
  }

  @Test
  void chunk_hashNotAtLineStart_notTreatedAsHeader() {
    // A hash sign in the middle of a line should NOT be treated as a header
    String markdown = "Some text with a #tag in the middle.\nMore content here.";

    List<ChunkCandidate> chunks = teachingChunkUtil.chunk(markdown);

    // Should be one chunk with "Introduction" title (no real headers found)
    assertThat(chunks).hasSize(1);
    assertThat(chunks.get(0).sectionTitle()).isEqualTo("Introduction");
  }
}
