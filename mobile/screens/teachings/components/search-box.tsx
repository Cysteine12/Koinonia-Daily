import { Icon, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useEffect, useRef } from 'react';
import { TextInput } from 'react-native';
import type { SearchResult } from '../index.tsx';

interface SearchBoxProps {
  searchQuery: string;
  setSearchQuery: (text: string) => void;
  handleSearch: () => void;
  setSearchResults: (results: SearchResult[]) => void;
}

export default function SearchBox({ searchQuery, setSearchQuery, handleSearch, setSearchResults }: SearchBoxProps) {
  const { color, theme } = useAppTheme();
  const textInputRef = useRef<TextInput>(null);

  useEffect(() => {
    if (searchQuery === '') {
      setSearchResults([]);
    }
  }, [searchQuery, setSearchResults]);

  return (
    <View
      className="border rounded-xl py-2 flex flex-row items-center h-12 w-full"
      style={{ backgroundColor: color.background, borderColor: color.border }}
    >
      <View>
        <OpacityPressable onPressIn={() => textInputRef.current?.focus()} className="px-2 my-auto">
          <Icon name="search" size={FontSize.xl} className="border-gray-400" />
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
          placeholder="Search teachings..."
          placeholderTextColor={color.textMuted}
          className="text-lg rounded-lg h-full px-2 items-center focus:outline-none leading-5 shadow-sm shadow-black/5"
          style={{ color: color.text }}
        />
      </View>
      {searchQuery.length > 0 && (
        <View>
          <OpacityPressable
            onPressIn={() => {
              textInputRef.current?.focus();
              setSearchQuery('');
            }}
            className="px-2 my-auto"
          >
            <Icon name="close.circle" size={FontSize.xl} className="text-gray-400" />
          </OpacityPressable>
        </View>
      )}
    </View>
  );
}
