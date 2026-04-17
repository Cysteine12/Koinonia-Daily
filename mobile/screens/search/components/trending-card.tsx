import { Image, type ImageProps, Text, View } from '@/components/core';
import ScalePressable from '@/components/ui/scale-pressable';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';

interface TrendingCardProps {
  index: number;
  id: string;
  thumbnailUrl: ImageProps;
  title: string;
  type: string;
  taughtAt: string;
  viewCount: string;
}

export default function TrendingCard({ index, id, thumbnailUrl, title, type, taughtAt, viewCount }: TrendingCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <ScalePressable className="w-full my-1" onPress={() => router.push('/(tabs)/search')}>
      <View
        className="flex flex-row items-center rounded-2xl border w-full p-2"
        style={{ backgroundColor: color.cardBackground, borderColor: color.cardBorder }}
      >
        <View>
          <Text
            variant="label"
            size={FontSize.xxl}
            className="pr-2"
            style={{ color: color.goldText, fontFamily: FontFamily.Lora_700Bold_Italic }}
          >
            #{index + 1}
          </Text>
        </View>
        <View>
          <Image source={thumbnailUrl} className="size-20 rounded-lg" />
        </View>
        <View className="flex-1 flex-col h-20 p-2">
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
            {taughtAt}
          </Text>
        </View>
        <View className="my-auto">
          <Text>{viewCount}</Text>
          <Text variant="label">reads</Text>
        </View>
      </View>
    </ScalePressable>
  );
}
