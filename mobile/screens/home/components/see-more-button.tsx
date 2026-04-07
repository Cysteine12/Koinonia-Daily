import { Icon, Text, View } from '@/components/core';
import { FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import type { Href } from 'expo-router';
import { useRouter } from 'expo-router';
import React from 'react';
import { TouchableOpacity } from 'react-native';

export default function SeeMoreButton({ link }: { link: Href }) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <View className="mx-8 my-auto">
      <TouchableOpacity onPress={() => router.push(link)}>
        <View
          className="border rounded-full p-4"
          style={{ backgroundColor: color.cardBackground, borderColor: color.cardBorder }}
        >
          <Icon name="chevron.right" size={FontSize.xl} />
        </View>
      </TouchableOpacity>
      <Text className="mt-1 text-center">See more</Text>
    </View>
  );
}
