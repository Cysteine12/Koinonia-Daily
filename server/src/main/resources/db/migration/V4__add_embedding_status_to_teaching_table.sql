ALTER TABLE teachings
  ADD COLUMN embedding_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  ADD CONSTRAINT teachings_embedding_status_check
    CHECK (embedding_status IN ('PENDING', 'CHUNKED', 'EMBEDDING', 'EMBEDDED', 'FAILED'));