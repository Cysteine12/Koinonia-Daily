package org.eni.koinoniadaily.modules.teaching;

public enum TeachingStatus {
  PENDING,
  CHUNKED,
  EMBEDDING,
  EMBEDDED, // Only marked when all chunk embedding are successful
  FAILED // Only marked as failed when embedding has been attempted, and at least one teaching chunk failed
}
