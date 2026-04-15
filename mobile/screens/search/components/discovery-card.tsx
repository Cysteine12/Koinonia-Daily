import { Text, View } from '@/components/core';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import type { ImageProps } from 'react-native';
import { Image, TouchableOpacity } from 'react-native';

interface DiscoveryCardProps {
  id: string;
  title: string;
  thumbnailUrl: ImageProps;
  type: string;
  createdAt: string;
}

export default function DiscoveryCard({ id, title, thumbnailUrl, type, createdAt }: DiscoveryCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <TouchableOpacity className=" my-1.5" onPress={() => router.push('/(tabs)/search')}>
      <View className="flex flex-row rounded-xl w-full">
        <View className="">
          <Image source={thumbnailUrl} className="size-16 rounded-xl" />
        </View>
        <View className="flex-1 flex-col py-0.5 pl-4">
          <Text className="uppercase" size={FontSize.xs} style={{ color: color.goldText }}>
            {type}
          </Text>
          <Text
            variant="title"
            numberOfLines={1}
            ellipsizeMode="tail"
            className="my-1 text-wrap text-base"
            style={{ fontFamily: FontFamily.Outfit_500Medium }}
          >
            {title}
          </Text>
          <Text variant="label" className="mt-auto">
            Added {createdAt}
          </Text>
        </View>
        <View className="my-auto m-0">
          <Ionicons name="chevron-forward" size={FontSize.lg} color={color.text} />
        </View>
      </View>
    </TouchableOpacity>
  );
}
