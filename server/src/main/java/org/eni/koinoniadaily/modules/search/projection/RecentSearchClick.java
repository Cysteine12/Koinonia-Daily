package org.eni.koinoniadaily.modules.search.projection;

import java.time.Instant;

import org.eni.koinoniadaily.modules.teaching.TeachingType;

public interface RecentSearchClick {

    Long getId();

    Long getTeachingId();

    String getTitle();

    String getThumbnailUrl();

    TeachingType getType();

    Instant getTaughtAt();
}
