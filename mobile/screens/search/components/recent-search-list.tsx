import { View, type ImageSourcePropType } from '@/components/core';
import React from 'react';
import RecentSearchCard from './recent-search-card';
import SectionTitle from './section-title';
import type { TeachingType } from '@/features/teaching';

interface RecentSearchListProps {
  recentSearches: {
    id: string;
    title: string;
    thumbnailUrl: ImageSourcePropType;
    type: TeachingType;
    createdAt: string;
  }[];
}

export default function RecentSearchList({ recentSearches }: RecentSearchListProps) {
  return (
    <View>
      <SectionTitle title="Recent Searches" />

      <View>
        {recentSearches.map((search) => (
          <RecentSearchCard
            key={search.id}
            id={search.id}
            title={search.title}
            thumbnailUrl={search.thumbnailUrl}
            type={search.type}
            createdAt={search.createdAt}
            onCancel={() => {}}
          />
        ))}
      </View>
    </View>
  );
}
