import { Text, View } from '@/components/core';
import ScalePressable from '@/components/reusables/ui/scale-pressable';
import { Colors, FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';
import { Image, type ImageSourcePropType } from 'react-native';

interface RecommendedTeachingCardProps {
  id: number;
  thumbnailUrl: ImageSourcePropType;
  title: string;
  type: string;
}

export default function RecommendedTeachingCard({ id, thumbnailUrl, title, type }: RecommendedTeachingCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <ScalePressable onPress={() => router.push('/(tabs)/home')}>
      <View
        className="flex-row mr-2 w-64 border rounded-xl"
        style={{ backgroundColor: color.cardBackground, borderColor: color.cardBorder }}
      >
        <Image source={thumbnailUrl} className="object-cover w-24 h-24 rounded-l-xl" />
        <View className="flex-1 p-2 h-24">
          <View className="flex-row">
            <View className="w-1.5 h-1.5 rounded-full bg-primary mr-1 mt-1" />
            <Text size={FontSize.xs - 1} className="font-bold" style={{ color: Colors.purple }}>
              SIMILAR TEACHINGS
            </Text>
          </View>
          <Text
            variant="title"
            size={FontSize.base - 1}
            numberOfLines={2}
            className="my-auto font-semibold text-wrap"
            family={FontFamily.Outfit_500Medium}
          >
            {title}
          </Text>
          <Text variant="label" size={FontSize.xs} className="mt-auto">
            {type}
          </Text>
        </View>
      </View>
    </ScalePressable>
  );
}
