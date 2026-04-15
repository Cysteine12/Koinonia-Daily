import { Icon, Screen, Text, View } from '@/components/core';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { FlatList, TouchableOpacity } from 'react-native';
import DiscoveryCard from './components/discovery-card';
import SectionTitle from './components/section-title';
import TrendingCard from './components/trending-card';

interface SearchHomeScreenProp {
  toggleSearchState: () => void;
}

export default function SearchHomeScreen({ toggleSearchState }: SearchHomeScreenProp) {
  const { color } = useAppTheme();

  const trendingTeachings = [
    {
      id: '1',
      title: 'Complete Deliverance: The Power of the Blood',
      thumbnailUrl: require('@/assets/images/p2.jpg'),
      type: 'SUNDAY SERVICE',
      taughtAt: 'April 7, 2023',
      viewCount: '2.4k',
    },
    {
      id: '2',
      title: 'The Sound of Revival: An Awakening to True Spirituality',
      thumbnailUrl: require('@/assets/images/p3.jpg'),
      type: 'CONFERENCE',
      taughtAt: 'Mar 6, 2025',
      viewCount: '2.3k',
    },
    {
      id: '3',
      title: 'Making your Life Count',
      thumbnailUrl: require('@/assets/images/p1.jpg'),
      type: 'EXTERNAL MINISTRATION',
      taughtAt: 'April 15, 2026',
      viewCount: '2.2k',
    },
  ];

  const discoveryTeachings = [
    {
      id: '1',
      title: 'Walking in the Counsel of God',
      thumbnailUrl: require('@/assets/images/p2.jpg'),
      type: 'EXTERNAL MINISTRATION',
      createdAt: 'April 7, 2023',
    },
    {
      id: '2',
      title: 'Foundations of Kingdom Authority',
      thumbnailUrl: require('@/assets/images/p3.jpg'),
      type: 'SUNDAY SERVICE',
      createdAt: 'Mar 6, 2025',
    },
    {
      id: '3',
      title: 'The Spirit of Wisdom and Revelation',
      thumbnailUrl: require('@/assets/images/p1.jpg'),
      type: 'CONFERENCE',
      createdAt: 'April 15, 2026',
    },
    {
      id: '4',
      title: 'Walking in the Counsel of God',
      thumbnailUrl: require('@/assets/images/p2.jpg'),
      type: 'EXTERNAL MINISTRATION',
      createdAt: 'April 7, 2023',
    },
    {
      id: '5',
      title: 'Foundations of Kingdom Authority',
      thumbnailUrl: require('@/assets/images/p3.jpg'),
      type: 'SUNDAY SERVICE',
      createdAt: 'Mar 6, 2025',
    },
    {
      id: '6',
      title: 'The Spirit of Wisdom and Revelation',
      thumbnailUrl: require('@/assets/images/p1.jpg'),
      type: 'CONFERENCE',
      createdAt: 'April 15, 2026',
    },
    {
      id: '7',
      title: 'Ever Increasing Glory: A Desperate Cry for Hunger',
      thumbnailUrl: require('@/assets/images/p2.jpg'),
      type: 'CONFERENCE',
      createdAt: 'April 15, 2026',
    },
  ];

  return (
    <Screen scrollable edges={['top']} stickyHeaderIndices={[1]} contentContainerClassName="px-4 pt-2">
      <View className="mt-4 mb-2">
        <Text variant="title" size={FontSize.xl + 4} family={FontFamily.Lora_500Medium} style={{ fontWeight: 'bold' }}>
          Search
        </Text>
      </View>
      <View className="mb-4">
        <TouchableOpacity
          onPress={toggleSearchState}
          activeOpacity={0.7}
          className="flex-row items-center border-2 rounded-xl p-4"
          style={{ backgroundColor: color.cardBackground, borderColor: color.border }}
        >
          <Icon name="search" size={20} color={color.textMuted} className="mr-2" />
          <Text variant="label" size={FontSize.base}>
            Search teachings, topics, scriptures, and more
          </Text>
        </TouchableOpacity>
      </View>

      <View className="mt-4">
        <SectionTitle title="Trending" link={'/search'} linkTitle="See all" />

        <View className="mt-[-4px] flex-row items-center">
          <View
            className="flex-row items-center rounded-lg py-0.5 px-2"
            style={{ borderColor: color.goldBorder, backgroundColor: color.goldTextMuted, borderWidth: 0.2 }}
          >
            <Icon name="flame" size={FontSize.xxs} color={'#ff6434'} className="mr-0.5" />
            <Text variant="label" weight="semibold" size={FontSize.xxs} style={{ color: color.goldText }}>
              THIS WEEK
            </Text>
          </View>
          <View>
            <Text variant="label" size={FontSize.xs} className="ml-2">
              Based on reads and saves
            </Text>
          </View>
        </View>

        <FlatList
          data={trendingTeachings}
          renderItem={({ item, index }) => (
            <TrendingCard
              index={index}
              id={item.id}
              thumbnailUrl={item.thumbnailUrl}
              title={item.title}
              type={item.type}
              taughtAt={item.taughtAt}
              viewCount={item.viewCount}
            />
          )}
          keyExtractor={(item) => item.id}
          className="my-2"
        />
      </View>

      <View className="mt-4">
        <SectionTitle title="Discover New" link={'/search'} />

        <FlatList
          data={discoveryTeachings}
          renderItem={({ item }) => (
            <DiscoveryCard
              id={item.id}
              thumbnailUrl={item.thumbnailUrl}
              title={item.title}
              type={item.type}
              createdAt={item.createdAt}
            />
          )}
          keyExtractor={(item) => item.id}
          className="my-2"
        />
      </View>
    </Screen>
  );
}
