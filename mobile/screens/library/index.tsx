import { Screen, View, Text } from '@/components/core';
import { FontFamily, FontSize } from '@/constants';
import LibraryCard from './components/library-card';
import { FlatList } from 'react-native';
import RecentlyUpdatedCard from './components/recently-updated-card';

const recentlyUpdateds = [
  {
    id: 1,
    title: 'Commanding The Supernatural',
    thumbnailUrl: require('@/assets/images/p1.jpg'),
    createdAt: new Date('2026-07-01'),
    type: 'SERIES' as const,
  },
  {
    id: 2,
    title: 'Complete Deliverance',
    thumbnailUrl: require('@/assets/images/p2.jpg'),
    createdAt: new Date('2026-06-30'),
    type: 'COLLECTION' as const,
  },
  {
    id: 3,
    title: 'Striving for Mastery',
    thumbnailUrl: require('@/assets/images/p3.jpg'),
    createdAt: new Date('2026-07-05'),
    type: 'SERIES' as const,
  },
  {
    id: 4,
    title: 'Weapons of Warfare: Destroying the stronghold of the Wicked',
    thumbnailUrl: require('@/assets/images/p4.jpg'),
    createdAt: new Date('2026-06-10'),
    type: 'COLLECTION' as const,
  },
];

export default function LibraryScreen() {
  return (
    <Screen scrollable edges={['top']} contentContainerClassName="px-4 pt-2">
      <View className="my-4">
        <Text variant="title" size={FontSize.xl + 4} family={FontFamily.Lora_500Medium} style={{ fontWeight: 'bold' }}>
          Library
        </Text>
      </View>

      <View className="my-2">
        <Text variant="title" size={FontSize.lg} family={FontFamily.Lora_500Medium} style={{ fontWeight: 'bold' }}>
          Browse
        </Text>

        <View className="flex-row gap-3 w-full my-2">
          <LibraryCard
            icon="library.outline"
            title="All Teachings"
            label="Browse everything, filter by type"
            color={'#3B82F6'}
            link={'/library'}
          />
          <LibraryCard
            icon="calendar.outline"
            title="Sunday Services"
            label="Browse chronologically by year"
            color={'#22C55E'}
            link={'/library'}
          />
        </View>

        <View className="flex-row gap-3 w-full my-2">
          <LibraryCard icon="series" title="Series" label="Sequential teaching journeys" color={'#8B5CF6'} link={'/library'} />
          <LibraryCard icon="folder" title="Collections" label="Curated related teachings" color={'#F59E0B'} link={'/library'} />
        </View>
      </View>

      <View className="my-2">
        <Text variant="title" size={FontSize.lg} family={FontFamily.Lora_500Medium} style={{ fontWeight: 'bold' }}>
          My Library
        </Text>

        <View className="flex-row gap-3 w-full my-2">
          <LibraryCard
            icon="bookmark.outline"
            title="My Bookmarks"
            label="4 collections • 30 saved"
            color={'#EC4899'}
            link={'/library'}
          />
          <LibraryCard icon="download.outline" title="My Downloads" label="6 teachings" color={'#06B6D4'} link={'/library'} />
        </View>
      </View>

      <View className="mt-4">
        <Text variant="title" size={FontSize.lg} family={FontFamily.Lora_500Medium} style={{ fontWeight: 'bold' }}>
          Recently Updated
        </Text>

        <FlatList
          data={recentlyUpdateds}
          keyExtractor={(item) => item.id.toString()}
          horizontal={true}
          showsHorizontalScrollIndicator={false}
          contentContainerClassName="my-2"
          renderItem={({ item }) => (
            <RecentlyUpdatedCard
              id={item.id}
              thumbnailUrl={item.thumbnailUrl}
              title={item.title}
              createdAt={item.createdAt}
              tagColor={item.type === 'SERIES' ? '#8B5CF6' : '#F59E0B'}
              type={item.type}
            />
          )}
        />
      </View>
    </Screen>
  );
}
