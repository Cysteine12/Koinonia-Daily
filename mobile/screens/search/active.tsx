import { Screen, Text, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import Tag from '@/components/ui/tag';
import { FontSize } from '@/constants';
import type { TeachingType } from '@/features/teaching';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useState } from 'react';
import { ScrollView, Switch } from 'react-native';
import RecentSearchList from './components/recent-search-list';
import SearchBox from './components/search-box';
import SearchResultList from './components/search-result-list';
import { recentSearchesData, searchResultsData } from './data';

interface SearchActiveScreenProps {
  toggleSearchState: () => void;
}

export type SearchResult = {
  id: string;
  title: string;
  text: string;
  thumbnailUrl: any;
  type: string;
  searchTag: string;
};

const searchTags = [
  { text: 'All', type: 'ALL' },
  { text: 'Sunday Service', type: 'SUNDAY_SERVICE' },
  { text: 'Conference', type: 'CONFERENCE' },
  { text: 'External Ministration', type: 'EXTERNAL_MINISTRATION' },
  { text: 'Special Service', type: 'SPECIAL_SERVICE' },
] as const satisfies readonly { text: string; type: TeachingType | 'ALL' }[];

export default function SearchActiveScreen({ toggleSearchState }: SearchActiveScreenProps) {
  const { color } = useAppTheme();
  const [searchQuery, setSearchQuery] = useState('');
  const [fullSearch, setFullSearch] = useState(false);
  const [selectedSearchTag, setSelectedSearchTag] = useState<TeachingType | 'ALL'>('ALL');
  const [searchResults, setSearchResults] = useState<SearchResult[]>([]);

  const recentSearches = recentSearchesData;

  const handleSearch = () => {
    setSearchResults(searchResultsData);
  };

  return (
    <Screen
      scrollable
      keyboard
      keyboardDismissMode="on-drag"
      keyboardShouldPersistTaps="always"
      edges={[]}
      stickyHeaderIndices={[0]}
    >
      <SearchBox
        searchQuery={searchQuery}
        setSearchQuery={setSearchQuery}
        handleSearch={handleSearch}
        onBack={toggleSearchState}
        setSearchResults={setSearchResults}
      />

      <View>
        <ScrollView
          contentContainerClassName="my-2 px-4"
          horizontal={true}
          showsHorizontalScrollIndicator={false}
          keyboardShouldPersistTaps="always"
        >
          {searchTags.map((item) => (
            <OpacityPressable key={item.type} activeScale={1} onPress={() => setSelectedSearchTag(item.type)}>
              <Tag
                text={item.text}
                color={selectedSearchTag === item.type ? color.goldBorder : color.border}
                backgroundColor={selectedSearchTag === item.type ? color.goldTextMuted : color.cardBorder}
                textColor={selectedSearchTag === item.type ? color.goldText : color.text}
                fontSize={FontSize.xs}
                className="mr-2 py-1 px-3 rounded-xl"
              />
            </OpacityPressable>
          ))}
        </ScrollView>
      </View>

      <View
        className="flex-row items-center justify-between mb-2 mx-4 px-3 rounded-lg border"
        style={{ backgroundColor: color.cardBorder, borderColor: color.border }}
      >
        <View>
          <Text variant="title" className="font-bold">
            Search inside teachings
          </Text>
          <Text variant="label" size={FontSize.sm}>
            Looks through full teaching content
          </Text>
        </View>
        <View>
          <Switch value={fullSearch} onValueChange={setFullSearch} trackColor={{ true: color.goldBorder }} thumbColor={'#fff'} />
        </View>
      </View>

      <View className="my-3 mx-4">
        {searchResults.length > 0 ? (
          <SearchResultList searchQuery={searchQuery} searchResults={searchResults} />
        ) : (
          <RecentSearchList recentSearches={recentSearches} />
        )}
      </View>
    </Screen>
  );
}
