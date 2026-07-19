import { Image, type ImageSourcePropType, Text, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import Tag from '@/components/ui/tag';
import TeachingTypeLabel from '@/components/ui/teaching-type-label';
import { FontFamily } from '@/constants';
import type { TeachingType } from '@/features/teaching';
import { useRouter } from 'expo-router';

interface SearchResultCardProps {
  id: string;
  title: string;
  text: string;
  thumbnailUrl: ImageSourcePropType;
  type: TeachingType;
  searchTag: string;
}

export default function SearchResultCard({ id, title, text, thumbnailUrl, type, searchTag }: SearchResultCardProps) {
  const router = useRouter();

  return (
    <OpacityPressable className="my-2" onPress={() => router.push(`/search?searchState=ACTIVE`)}>
      <View className="flex flex-row rounded-md w-full">
        <View>
          <Image source={thumbnailUrl} className="size-20 rounded-md" />
        </View>
        <View className="flex-1 flex-col pl-2">
          <View className="flex-row items-center">
            <TeachingTypeLabel type={type} />
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
