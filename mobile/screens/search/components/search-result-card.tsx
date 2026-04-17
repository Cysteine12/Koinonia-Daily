import { Image, type ImageSourcePropType, Text, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import Tag from '@/components/ui/tag';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';

interface SearchResultCardProps {
  id: string;
  title: string;
  text: string;
  thumbnailUrl: ImageSourcePropType;
  type: string;
  searchTag: string;
}

export default function SearchResultCard({ id, title, text, thumbnailUrl, type, searchTag }: SearchResultCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <OpacityPressable className="my-2" onPress={() => router.push(`/(tabs)/search?searchState=ACTIVE`)}>
      <View className="flex flex-row rounded-md w-full">
        <View>
          <Image source={thumbnailUrl} className="size-20 rounded-md" />
        </View>
        <View className="flex-1 flex-col pl-2">
          <View className="flex-row items-center">
            <Text className="uppercase" size={FontSize.xs} style={{ color: color.goldText }}>
              {type}
            </Text>
            <Tag text={searchTag} color="#6a5ca3" className="ml-2" />
          </View>
          <Text
            variant="title"
            numberOfLines={2}
            ellipsizeMode="tail"
            className="text-wrap text-base leading-tight"
            style={{ fontFamily: FontFamily.Outfit_500Medium }}
          >
            {title}
          </Text>
          <Text variant="label" numberOfLines={2} ellipsizeMode="tail" className="mt-auto">
            {text}
          </Text>
        </View>
      </View>
    </OpacityPressable>
  );
}
