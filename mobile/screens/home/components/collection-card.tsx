import { Text, View } from '@/components/core';
import ScalePressable from '@/components/reusables/ui/scale-pressable';
import { Colors, FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';
import { Image, type ImageSourcePropType } from 'react-native';

interface CollectionCardProps {
  id: number;
  imageUrl: ImageSourcePropType;
  title: string;
  total: number;
}

export default function CollectionCard({ id, imageUrl, title, total }: CollectionCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <ScalePressable onPress={() => router.push('/(tabs)/home')}>
      <View
        className="relative mr-2 w-32 h-40 border rounded-xl"
        style={{ backgroundColor: color.cardBackground, borderColor: color.cardBorder }}
      >
        <Image source={imageUrl} className="object-cover w-32 h-40 rounded-xl" />
        <View className="absolute bottom-0 p-2 rounded-b-xl w-full" style={{ backgroundColor: Colors.cardOverlay }}>
          <Text
            variant="title"
            size={FontSize.base - 1}
            lightColor={Colors.dark.text}
            numberOfLines={2}
            className="font-semibold text-wrap"
            style={{ fontFamily: FontFamily.Outfit_500Medium }}
          >
            {title}
          </Text>
          <Text variant="label" size={FontSize.xs} lightColor={Colors.dark.text} className="mt-auto">
            {total} teachings
          </Text>
        </View>
      </View>
    </ScalePressable>
  );
}
