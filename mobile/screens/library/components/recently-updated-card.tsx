import { Image, type ImageSourcePropType, Text, View } from '@/components/core';
import ScalePressable from '@/components/ui/scale-pressable';
import Tag from '@/components/ui/tag';
import { Colors, FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';
import { differenceInCalendarDays } from 'date-fns';

interface RecentlyUpdatedCardProps {
  id: number;
  thumbnailUrl: ImageSourcePropType;
  title: string;
  createdAt: Date;
  tagColor: string;
  type: 'SERIES' | 'COLLECTION';
}

export default function RecentlyUpdatedCard({ id, thumbnailUrl, title, createdAt, tagColor, type }: RecentlyUpdatedCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  const daysAgo = differenceInCalendarDays(new Date(), createdAt);
  const relativeTime = daysAgo <= 0 ? 'today' : daysAgo === 1 ? 'yesterday' : `${daysAgo} days ago`;

  return (
    <ScalePressable onPress={() => router.push('/library')} className="w-40 mr-3">
      <View
        className="relative border rounded-xl"
        style={{ backgroundColor: color.cardBackground, borderColor: color.cardBorder }}
      >
        <Image source={thumbnailUrl} className="object-cover w-40 h-32 rounded-xl" />
        <View className="absolute top-0 right-0 py-0.5 rounded-b-xl">
          <Tag color={tagColor} backgroundColor={tagColor} textColor={Colors.dark.text} text={type} className="capitalize" />
        </View>
      </View>

      <View className="py-3 px-1">
        <Text
          variant="title"
          size={FontSize.base - 1}
          numberOfLines={2}
          className="font-semibold text-wrap"
          style={{ fontFamily: FontFamily.Outfit_500Medium }}
        >
          {title}
        </Text>
        <Text variant="label" size={FontSize.xs} className="mt-auto">
          Updated {relativeTime}
        </Text>
      </View>
    </ScalePressable>
  );
}
