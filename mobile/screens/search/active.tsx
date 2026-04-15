import { Icon, Screen, Text, View } from '@/components/core';
import { FontSize } from '@/constants';
import type { TeachingType } from '@/features/teaching';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useState } from 'react';
import { FlatList, Switch, TextInput, TouchableOpacity } from 'react-native';

interface SearchActiveScreenProps {
  toggleSearchState: () => void;
}

export default function SearchActiveScreen({ toggleSearchState }: SearchActiveScreenProps) {
  const { color, isDark } = useAppTheme();
  const [searchQuery, setSearchQuery] = useState('');
  const [fullSearch, setFullSearch] = useState(false);
  const [selectedSearchTag, setSelectedSearchTag] = useState<TeachingType | 'ALL'>('ALL');
  const searchTags: { text: string; type: TeachingType | 'ALL' }[] = [
    {
      text: 'All',
      type: 'ALL',
    },
    {
      text: 'Sunday Service',
      type: 'SUNDAY_SERVICE',
    },
    {
      text: 'Conference',
      type: 'CONFERENCE',
    },
    {
      text: 'External Ministration',
      type: 'EXTERNAL_MINISTRATION',
    },
    {
      text: 'Special Service',
      type: 'SPECIAL_SERVICE',
    },
  ];

  return (
    <Screen scrollable keyboard edges={['top']} stickyHeaderIndices={[0]}>
      <View className="flex flex-row items-center" style={{ backgroundColor: color.border }}>
        <TouchableOpacity onPress={toggleSearchState} className="p-2">
          <Icon name="arrow.backward" size={FontSize.xl} color={color.text} />
        </TouchableOpacity>
        <TextInput
          autoFocus={true}
          placeholder="Search teachings, topics, scriptures and more"
          placeholderTextColor={color.textMuted}
          keyboardAppearance={isDark ? 'dark' : 'light'}
          inputMode="search"
          maxLength={100}
          value={searchQuery}
          onChangeText={setSearchQuery}
          className="text-lg rounded-lg w-fit h-16 px-2 items-center focus:outline-none leading-5 shadow-sm shadow-black/5"
          style={{ color: color.text }}
        />
        {searchQuery.length > 0 && (
          <TouchableOpacity onPress={() => setSearchQuery('')} className="p-2 ml-auto">
            <Icon name="cancel" size={FontSize.xl} color={color.text} />
          </TouchableOpacity>
        )}
      </View>

      <View>
        <FlatList
          contentContainerClassName="my-2 px-4"
          horizontal={true}
          showsHorizontalScrollIndicator={false}
          data={searchTags}
          renderItem={({ item }) => (
            <TouchableOpacity
              className="flex-1 border rounded-xl py-1 px-3 mr-2"
              style={{
                borderColor: selectedSearchTag === item.type ? color.goldBorder : color.border,
                backgroundColor: selectedSearchTag === item.type ? color.goldTextMuted : color.cardBorder,
              }}
              onPress={() => setSelectedSearchTag(item.type)}
            >
              <Text
                variant="label"
                weight="semibold"
                size={FontSize.xs}
                style={{ color: selectedSearchTag === item.type ? color.goldText : color.text }}
              >
                {item.text}
              </Text>
            </TouchableOpacity>
          )}
        />
      </View>

      <View
        className="flex-row items-center justify-between mb-2 mx-4 p-3 rounded-lg border"
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
        <View className="">
          <Switch value={fullSearch} onValueChange={setFullSearch} trackColor={{ true: color.goldBorder }} thumbColor={'#fff'} />
        </View>
      </View>

      <View className="px-2 my-4">
        <Text>SearchActiveScreen is now WIP</Text>
      </View>
    </Screen>
  );
}
