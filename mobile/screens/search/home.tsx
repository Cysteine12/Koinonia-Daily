import { Screen, Text, View } from '@/components/core';
import Tag from '@/components/ui/tag';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import DiscoveryCard from './components/discovery-card';
import SectionTitle from './components/section-title';
import TrendingCard from './components/trending-card';

import SearchButton from './components/search-button';
import { discoveryTeachingsData, trendingTeachingsData } from './data';

interface SearchHomeScreenProps {
  toggleSearchState: () => void;
}

export default function SearchHomeScreen({ toggleSearchState }: SearchHomeScreenProps) {
  const { color } = useAppTheme();

  const trendingTeachings = trendingTeachingsData;

  const discoveryTeachings = discoveryTeachingsData;

  return (
    <Screen scrollable edges={[]} stickyHeaderIndices={[1]} contentContainerClassName="px-4 pt-2">
      <View className="mt-4">
        <Text variant="title" size={FontSize.xl + 4} family={FontFamily.Lora_500Medium} style={{ fontWeight: 'bold' }}>
          Search
        </Text>
      </View>

      <View className="my-2" style={{ backgroundColor: color.containerBackground }}>
        <SearchButton toggleSearchState={toggleSearchState} />
      </View>

      <View className="mt-6">
        <SectionTitle title="Trending" link={'/search'} linkTitle="See all" />

        <View className="mt-[-4px] flex-row items-center">
          <Tag
            text="THIS WEEK"
            color={color.goldBorder}
            icon="flame"
            iconColor="#ff6434"
            borderWidth={0.2}
            textColor={color.goldText}
          />

          <View>
            <Text variant="label" size={FontSize.xs} className="ml-2">
              Based on reads and saves
            </Text>
          </View>
        </View>

        <View className="my-2">
          {trendingTeachings.map((item, index) => (
            <TrendingCard
              key={item.id}
              index={index}
              id={item.id}
              thumbnailUrl={item.thumbnailUrl}
              title={item.title}
              type={item.type}
              taughtAt={item.taughtAt}
              viewCount={item.viewCount}
            />
          ))}
        </View>
      </View>

      <View className="mt-4">
        <SectionTitle title="Discover New" link={'/search'} />

        <View className="my-2">
          {discoveryTeachings.map((item) => (
            <DiscoveryCard
              key={item.id}
              id={item.id}
              thumbnailUrl={item.thumbnailUrl}
              title={item.title}
              type={item.type}
              createdAt={item.createdAt}
            />
          ))}
        </View>
      </View>
    </Screen>
  );
}
