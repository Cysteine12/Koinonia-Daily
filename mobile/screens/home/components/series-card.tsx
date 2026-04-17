import { Icon, Image, type ImageSourcePropType, Text, View } from '@/components/core';
import ScalePressable from '@/components/ui/scale-pressable';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';

interface SeriesCardProps {
  id: number;
  thumbnailUrl: ImageSourcePropType;
  title: string;
  description: string;
  total: number;
}

export default function SeriesCard({ id, thumbnailUrl, title, description, total }: SeriesCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <ScalePressable onPress={() => router.push('/(tabs)/home')}>
      <View
        className="mr-2 w-48 border rounded-xl"
        style={{ backgroundColor: color.cardBackground, borderColor: color.cardBorder }}
      >
        <Image source={thumbnailUrl} className="object-contain w-full h-28 rounded-t-xl" />
        <View className="py-3 px-2 h-[104px]">
          <Text variant="title" numberOfLines={2} family={FontFamily.Lora_500Medium}>
            {title}
          </Text>
          <Text variant="label" numberOfLines={2} size={FontSize.xs} className="mt-auto">
            {description}
          </Text>
          <View className="flex-row items-center mt-3">
            <Icon name={'book.outline'} size={FontSize.xs} color={color.goldText} />
            <Text variant="subtitle" size={FontSize.xs} style={{ color: color.goldText }} className="ml-2 mt-auto">
              {total} parts
            </Text>
          </View>
        </View>
      </View>
    </ScalePressable>
  );
}
