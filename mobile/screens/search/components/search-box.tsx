import { Icon, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useEffect, useRef } from 'react';
import { TextInput } from 'react-native';
import type { SearchResult } from '../active';

interface SearchBoxProps {
  onBack: () => void;
  searchQuery: string;
  setSearchQuery: (text: string) => void;
  handleSearch: () => void;
  setSearchResults: (results: SearchResult[]) => void;
}

export default function SearchBox({ onBack, searchQuery, setSearchQuery, handleSearch, setSearchResults }: SearchBoxProps) {
  const { color, theme } = useAppTheme();
  const textInputRef = useRef<TextInput>(null);

  useEffect(() => {
    if (searchQuery === '') {
      setSearchResults([]);
    }
  }, [searchQuery]);

  return (
    <View className="flex flex-row items-center h-16 w-full" style={{ backgroundColor: color.border }}>
      <View>
        <OpacityPressable onPress={onBack} className="p-2 my-auto">
          <Icon name="arrow.backward" size={FontSize.xl} color={color.text} />
        </OpacityPressable>
      </View>
      <View className="flex-1">
        <TextInput
          ref={textInputRef}
          onSubmitEditing={handleSearch}
          value={searchQuery}
          onChangeText={setSearchQuery}
          keyboardAppearance={theme}
          returnKeyType="search"
          enterKeyHint="search"
          inputMode="search"
          maxLength={100}
          autoFocus={true}
          placeholder="Search teachings, topics, scriptures and more"
          placeholderTextColor={color.textMuted}
          className="text-lg rounded-lg h-16 px-2 items-center focus:outline-none leading-5 shadow-sm shadow-black/5"
          style={{ color: color.text }}
        />
      </View>
      {searchQuery.length > 0 && (
        <View>
          <OpacityPressable
            onPress={() => {
              textInputRef.current?.focus();
              setSearchQuery('');
            }}
            className="p-2 my-auto"
          >
            <Icon name="close.circle" size={FontSize.xl} color={color.textMuted} />
          </OpacityPressable>
        </View>
      )}
    </View>
  );
}
