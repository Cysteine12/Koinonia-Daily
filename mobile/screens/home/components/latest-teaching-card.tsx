import { Image, type ImageSourcePropType, Text, View } from '@/components/core';
import ScalePressable from '@/components/ui/scale-pressable';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';

interface LatestTeachingCardProps {
  id: number;
  thumbnailUrl: ImageSourcePropType;
  title: string;
  date: string;
}

export default function LatestTeachingCard({ id, thumbnailUrl, title, date }: LatestTeachingCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <ScalePressable onPress={() => router.push('/home')}>
      <View
        className="mr-2 w-44 border rounded-xl"
        style={{ backgroundColor: color.cardBackground, borderColor: color.cardBorder }}
      >
        <Image source={thumbnailUrl} className="object-contain w-full h-28 rounded-t-xl" />
        <View className="p-2 h-24">
          <Text variant="title" numberOfLines={3} className="font-semibold">
            {title}
          </Text>
          <Text variant="label" className="mt-auto">
            {date}
          </Text>
        </View>
      </View>
    </ScalePressable>
  );
}
