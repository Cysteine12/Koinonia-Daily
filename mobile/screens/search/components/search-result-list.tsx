import { Text, View } from '@/components/core';
import { FontFamily } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import React from 'react';
import type { SearchResult } from '../active';
import SearchResultCard from './search-result-card';

interface SearchResultListProps {
  searchQuery: string;
  searchResults: SearchResult[];
}

export default function SearchResultList({ searchQuery, searchResults }: SearchResultListProps) {
  const { color } = useAppTheme();

  return (
    <View>
      <View className="flex-row items-center my-2">
        <Text variant="label" className="mr-1">
          Showing results for
        </Text>
        <Text
          numberOfLines={1}
          ellipsizeMode="tail"
          style={{ color: color.goldText, fontFamily: FontFamily.Lora_400Regular_Italic }}
        >{`"${searchQuery}"`}</Text>
      </View>

      <View>
        {searchResults.map((item) => (
          <SearchResultCard
            key={item.id}
            id={item.id}
            title={item.title}
            text={item.text}
            thumbnailUrl={item.thumbnailUrl}
            type={item.type}
            searchTag={item.searchTag}
          />
        ))}
      </View>
    </View>
  );
}
