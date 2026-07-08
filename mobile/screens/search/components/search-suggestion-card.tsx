import { Icon, Image, type ImageSourcePropType, Text, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';

interface SearchSuggestionCardProps {
  id: string;
  title: string;
  thumbnailUrl: ImageSourcePropType;
  type: string;
  createdAt: string;
}

export default function SearchSuggestionCard({ id, title, thumbnailUrl, type, createdAt }: SearchSuggestionCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <OpacityPressable onPress={() => router.push('/(tabs)/search?searchState=ACTIVE')} className="my-2">
      <View className="flex flex-row rounded-sm w-full">
        <View>
          <Image source={thumbnailUrl} className="size-12 rounded-md" />
        </View>
        <View className="flex-1 flex-col px-3">
          <Text
            variant="title"
            numberOfLines={1}
            ellipsizeMode="tail"
            className="my-1 text-wrap text-base"
            style={{ fontFamily: FontFamily.Outfit_500Medium }}
          >
            {title}
          </Text>
          <View className="flex-row items-center">
            <Icon name="clock.outline" size={FontSize.xs} color={color.textMuted} className="mr-1" />
            <Text variant="label" size={FontSize.xs} style={{ color: color.textMuted }}>
              {createdAt}
            </Text>
            <Text className="mx-1.5" style={{ color: color.textMuted }}>
              •
            </Text>
            <Text className="uppercase" size={FontSize.xs} style={{ color: color.textMuted }}>
              {type}
            </Text>
          </View>
        </View>
        <View className="my-auto">
          <OpacityPressable onPress={() => {}} className="my-auto m-0">
            <Icon name="menu.horizontal" size={FontSize.xl} color={color.text} />
          </OpacityPressable>
        </View>
      </View>
    </OpacityPressable>
  );
}
