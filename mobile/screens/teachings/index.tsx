import { Screen, Text, View, FlashList } from '@/components/core';
import BackButton from '@/components/ui/back-button';
import { FontSize } from '@/constants';
import type { TeachingType } from '@/features/teaching';
import { useAppTheme } from '@/hooks/use-app-theme';
import SearchBox from './components/search-box';
import { useState } from 'react';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { TEACHING_TYPE_TAGS } from '@/features/teaching/types';
import { FlatList, useWindowDimensions, type ImageSourcePropType } from 'react-native';
import Tag from '@/components/ui/tag';
import { teachings } from '../search/data';
import TeachingFilters from './components/teaching-filters';
import TeachingCard from './components/teaching-card';
import { useAppSettings } from '@/features/settings-context';
import ActiveFiltersRow from './components/active-filters-row';

export interface SearchResult {
  id: number;
  title: string;
  thumbnailUrl: ImageSourcePropType;
  summary: string;
  type: TeachingType;
  taughtAt: Date;
}

export default function TeachingsScreen() {
  const { color } = useAppTheme();
  const { settings } = useAppSettings();
  const { width: screenWidth } = useWindowDimensions();
  const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedSearchTag, setSelectedSearchTag] = useState<TeachingType | 'ALL'>('ALL');
  const [selectedDateRange, setSelectedDateRange] = useState<[Date | null, Date | null]>([null, null]);
  const [selectedFilterTags, setSelectedFilterTags] = useState<string[]>([]);

  // Extract unique tags from all teachings, sorted alphabetically
  const seen = new Set<string>();
  teachings.forEach((teaching) =>
    teaching.tags.split(', ').forEach((tag) => seen.add(tag.trim()))
  );
  const tags = Array.from(seen).sort();

  const handleSearch = () => { };

  const handleFilterSubmit = (
    filterTags: string[],
    dateRange: [Date | null, Date | null],
  ) => {
    // TODO: trigger filtered fetch / local filter with filterTags & dateRange
  };

  const handleRemoveTag = (tag: string) =>
    setSelectedFilterTags(selectedFilterTags.filter((t) => t !== tag));

  const handleClearDateRange = () => setSelectedDateRange([null, null]);

  const handleClearAll = () => {
    setSelectedFilterTags([]);
    setSelectedDateRange([null, null]);
  };

  return (
    <Screen keyboard>
      <View className="px-4 pb-2">
        <View className="relative h-10 flex-row items-center mb-1.5">
          <BackButton size={FontSize.lg} className="z-10 h-10 w-10 item-center justify-center rounded-full" />
          <Text variant="title" className="absolute left-16 right-16 text-center text-xl font-bold">
            Teaching Library
          </Text>
        </View>
        <SearchBox
          searchQuery={searchQuery}
          setSearchQuery={setSearchQuery}
          handleSearch={handleSearch}
          setSearchResults={setSearchResults}
        />
      </View>

      <FlashList
        key={settings.TEACHING_LIBRARY_LAYOUT}
        data={teachings}
        keyExtractor={(item) => item.id}
        numColumns={settings.TEACHING_LIBRARY_LAYOUT === 'grid' ? 2 : 1}
        contentContainerStyle={{ paddingHorizontal: 12, paddingBottom: 24 }}
        scrollEnabled={true}
        keyboardDismissMode="on-drag"
        keyboardShouldPersistTaps="always"
        estimatedItemSize={30}
        contentInsetAdjustmentBehavior="automatic"
        ListHeaderComponent={
          <>
            <FlatList
              horizontal={true}
              showsHorizontalScrollIndicator={false}
              keyboardShouldPersistTaps="always"
              data={TEACHING_TYPE_TAGS}
              renderItem={({ item }) => (
                <OpacityPressable key={item.type} activeScale={1} onPress={() => setSelectedSearchTag(item.type)}>
                  <Tag
                    text={item.text}
                    color={selectedSearchTag === item.type ? color.goldBorder : color.border}
                    backgroundColor={selectedSearchTag === item.type ? color.goldBorder : color.cardBorder}
                    textColor={selectedSearchTag === item.type ? color.background : color.text}
                    fontSize={FontSize.xs + 2}
                    className="mr-2 py-1 px-3 rounded-xl"
                  />
                </OpacityPressable>
              )}
            />

            <TeachingFilters
              teachingsCount={teachings.length}
              tags={tags}
              selectedFilterTags={selectedFilterTags}
              setSelectedFilterTags={setSelectedFilterTags}
              selectedDateRange={selectedDateRange}
              setSelectedDateRange={setSelectedDateRange}
              onFilterSubmit={handleFilterSubmit}
            />

            <ActiveFiltersRow
              selectedFilterTags={selectedFilterTags}
              selectedDateRange={selectedDateRange}
              onRemoveTag={handleRemoveTag}
              onClearDateRange={handleClearDateRange}
              onClearAll={handleClearAll}
            />
          </>
        }
        renderItem={({ item }) => (
          <TeachingCard
            id={item.id}
            thumbnailUrl={item.thumbnailUrl}
            title={item.title}
            type={item.type}
            taughtAt={item.taughtAt}
            seriesPart={item.seriesPart}
            isNew={item.isNew}
            settings={settings}
            screenWidth={screenWidth}
          />
        )}
      />
    </Screen>
  );
}
