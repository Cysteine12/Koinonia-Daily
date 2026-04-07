import { Screen, View } from '@/components/core';
import React from 'react';
import { FlatList } from 'react-native';
import CollectionCard from './components/collection-card';
import ContinueCard from './components/continue-card';
import HeroSection from './components/hero-section';
import LatestTeachingCard from './components/latest-teaching-card';
import RecommendedTeachingCard from './components/recommended-teaching-card';
import SectionTitle from './components/section-title';
import SeeMoreButton from './components/see-more-button';
import SeriesCard from './components/series-card';
import StatsCard from './components/stats-card';

export default function HomeScreen() {
  const recentTeachings = [
    {
      id: 1,
      title: 'Commanding The Supernatural Part 1: The Mystery of Dominion',
      imageUrl: require('@/assets/images/p1.jpg'),
      date: 'Mar 6, 2025',
    },
    {
      id: 2,
      title: 'Commanding The Supernatural Part 1: The Mystery of Dominion',
      imageUrl: require('@/assets/images/p2.jpg'),
      date: 'Mar 6, 2025',
    },
    {
      id: 3,
      title: 'Commanding The Supernatural Part 1: The Mystery of Dominion',
      imageUrl: require('@/assets/images/p3.jpg'),
      date: 'Mar 6, 2025',
    },
  ];

  const recommendedTeachings = [
    {
      id: 1,
      title: 'How Kings Reign: The Power of Spoken Words',
      imageUrl: require('@/assets/images/p1.jpg'),
      type: 'Conference',
    },
    {
      id: 2,
      title: 'How Kings Reign: The Power of Spoken Words',
      imageUrl: require('@/assets/images/p2.jpg'),
      type: 'Sunday Service',
    },
    {
      id: 3,
      title: 'How Kings Reign: The Power of Spoken Words',
      imageUrl: require('@/assets/images/p3.jpg'),
      type: 'External Ministration',
    },
    {
      id: 4,
      title: 'How Kings Reign: The Power of Spoken Words',
      imageUrl: require('@/assets/images/p4.jpg'),
      type: 'Conference',
    },
  ];

  const collections = [
    {
      id: 1,
      title: 'Warfare & Deliverance',
      imageUrl: require('@/assets/images/p1.jpg'),
      total: 12,
    },
    {
      id: 2,
      title: 'New Creation Realities',
      imageUrl: require('@/assets/images/p2.jpg'),
      total: 8,
    },
    {
      id: 3,
      title: 'Establishing Dominion',
      imageUrl: require('@/assets/images/p3.jpg'),
      total: 15,
    },
    {
      id: 4,
      title: 'Manifesting The Supernatural',
      imageUrl: require('@/assets/images/p4.jpg'),
      total: 20,
    },
  ];

  const series = [
    {
      id: 1,
      title: 'Commanding The Supernatural',
      imageUrl: require('@/assets/images/p1.jpg'),
      description: 'Exploring the dynamics of faith in establishing kingdom ordinances',
      total: 2,
    },
    {
      id: 2,
      title: 'Complete Deliverance',
      imageUrl: require('@/assets/images/p2.jpg'),
      description: 'A deep dive on the assets for establish the reality of over victory experientially',
      total: 3,
    },
    {
      id: 3,
      title: 'Striving for Mastery',
      imageUrl: require('@/assets/images/p3.jpg'),
      description: 'Becoming the God-man that we have been created to be in our daily living',
      total: 2,
    },
  ];

  return (
    <Screen scrollable edges={['top']}>
      <HeroSection />

      <View className="py-4">
        {/* Stats Section */}
        <View className="flex flex-row justify-evenly w-full px-4">
          <StatsCard title="7" subtitle="DAY STREAK" icon="rocket" tagColor={'#1f92c7'} iconColor={'#1f92c7'} />
          <StatsCard title="33m" subtitle="TODAY" icon="timer.outline" tagColor={'#b600b6'} iconColor={'#b600b6'} />
          <StatsCard title="43" subtitle="READ" icon="book.outline" tagColor={'#bd910d'} iconColor={'#bd910d'} />
          <StatsCard title="18" subtitle="COMPLETED" icon="check.circle.outline" tagColor={'#13ad0d'} iconColor={'#13ad0d'} />
        </View>

        {/* Continue Reading Section */}
        <View className="py-4 px-4">
          <SectionTitle title="Continue Reading" />

          <ContinueCard />
        </View>

        {/* Latest Teachings Section */}
        <View className="py-4 px-4">
          <SectionTitle title="Latest Teachings" link={'/(tabs)/home'} />

          <FlatList
            data={recentTeachings}
            keyExtractor={(item) => item.id.toString()}
            horizontal={true}
            showsHorizontalScrollIndicator={false}
            renderItem={({ item }) => (
              <LatestTeachingCard id={item.id} imageUrl={item.imageUrl} title={item.title} date={item.date} />
            )}
            ListFooterComponent={() => <SeeMoreButton link={'/(tabs)/home'} />}
          />
        </View>

        {/* Recommendation Section */}
        <View className="py-4 px-4">
          <SectionTitle title="Recommended For You" link={'/(tabs)/home'} linkTitle="More" />

          <FlatList
            data={recommendedTeachings}
            keyExtractor={(item) => item.id.toString()}
            horizontal={true}
            showsHorizontalScrollIndicator={false}
            renderItem={({ item }) => (
              <RecommendedTeachingCard id={item.id} imageUrl={item.imageUrl} title={item.title} type={item.type} />
            )}
            ListFooterComponent={() => <SeeMoreButton link={'/(tabs)/home'} />}
          />
        </View>

        {/* Collection Section */}
        <View className="py-4 px-4">
          <SectionTitle title="Collections" link={'/(tabs)/home'} />

          <FlatList
            data={collections}
            keyExtractor={(item) => item.id.toString()}
            horizontal={true}
            showsHorizontalScrollIndicator={false}
            renderItem={({ item }) => (
              <CollectionCard id={item.id} imageUrl={item.imageUrl} title={item.title} total={item.total} />
            )}
            ListFooterComponent={() => <SeeMoreButton link={'/(tabs)/home'} />}
          />
        </View>

        {/* Series Section */}
        <View className="py-4 px-4">
          <SectionTitle title="Series" link={'/(tabs)/home'} />

          <FlatList
            data={series}
            keyExtractor={(item) => item.id.toString()}
            horizontal={true}
            showsHorizontalScrollIndicator={false}
            renderItem={({ item }) => (
              <SeriesCard
                id={item.id}
                imageUrl={item.imageUrl}
                title={item.title}
                description={item.description}
                total={item.total}
              />
            )}
            ListFooterComponent={() => <SeeMoreButton link={'/(tabs)/home'} />}
          />
        </View>
      </View>
    </Screen>
  );
}
