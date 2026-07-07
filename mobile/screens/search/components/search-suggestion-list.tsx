import { View, type ImageSourcePropType } from '@/components/core';
import React from 'react';
import SearchSuggestionCard from './search-suggestion-card';
import SectionTitle from './section-title';

interface SearchSuggestionListProps {
  searchSuggestions: {
    id: string;
    title: string;
    thumbnailUrl: ImageSourcePropType;
    type: string;
    createdAt: string;
  }[];
}

export default function SearchSuggestionList({ searchSuggestions }: SearchSuggestionListProps) {
  return (
    <View>
      <SectionTitle title="Search Suggestions" />

      <View>
        {searchSuggestions.map((teaching) => (
          <SearchSuggestionCard
            key={teaching.id}
            id={teaching.id}
            title={teaching.title}
            thumbnailUrl={teaching.thumbnailUrl}
            type={teaching.type}
            createdAt={teaching.createdAt}
          />
        ))}
      </View>
    </View>
  );
}
