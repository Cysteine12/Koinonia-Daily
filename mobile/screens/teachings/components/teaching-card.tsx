import { Icon, Image, type ImageSourcePropType, Text, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import TeachingTypeLabel from '@/components/ui/teaching-type-label';
import { Colors, FontFamily, FontSize } from '@/constants';
import type { APP_SETTINGS_TYPE } from '@/features/settings-context';
import { type TeachingType } from '@/features/teaching';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';
import Animated, { FadeIn, FadeOut } from 'react-native-reanimated';

// Must match contentContainerStyle paddingHorizontal (12) and card margin (4) in index.tsx
const CONTAINER_H_PADDING = 24; // 12px * 2 sides
const CARD_SIDE_MARGIN = 4; // margin:4 on each card → 8px consumed per card slot

interface TeachingCardProps {
  id: string;
  title: string;
  thumbnailUrl: ImageSourcePropType;
  type: TeachingType;
  taughtAt: string;
  seriesPart: number | null;
  isNew: boolean;
  settings: APP_SETTINGS_TYPE;
  screenWidth: number;
}

export default function TeachingCard({
  id,
  title,
  thumbnailUrl,
  type,
  taughtAt,
  seriesPart,
  isNew,
  settings,
  screenWidth,
}: TeachingCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();
  const isGrid = settings.TEACHING_LIBRARY_LAYOUT === 'grid';

  // The width of one FlatList column slot (half the content area)
  const gridItemMaxWidth = (screenWidth - CONTAINER_H_PADDING) / 2;
  // 4:3-ish landscape ratio — compact without feeling cramped
  // Image uses width:'100%' so height alone drives the ratio
  const gridImageHeight = (gridItemMaxWidth - CARD_SIDE_MARGIN * 2) * 0.72;

  if (isGrid) {
    return (
      <Animated.View
        entering={FadeIn.duration(220)}
        exiting={FadeOut.duration(150)}
        style={{
          flex: 1,
          // Cap at exactly one column slot so the last odd card never stretches full-width
          maxWidth: gridItemMaxWidth,
          margin: CARD_SIDE_MARGIN,
          alignSelf: 'stretch',
        }}
      >
        <OpacityPressable
          onPress={() => router.replace('/teachings')}
          style={{
            flex: 1,
            borderRadius: 12,
            overflow: 'hidden',
            backgroundColor: color.cardBackground,
            borderWidth: 1,
            borderColor: color.cardBorder,
            borderCurve: 'continuous',
          }}
        >
          {/* Thumbnail — width:'100%' works because parent has explicit maxWidth */}
          <Image source={thumbnailUrl} style={{ width: '100%', height: gridImageHeight }} resizeMode="cover" />

          {/* Info */}
          <View style={{ padding: 10, gap: 4 }}>
            {/* Type label + NEW badge */}
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <TeachingTypeLabel type={type} />
              {isNew && (
                <Text
                  size={FontSize.xs}
                  style={{
                    marginLeft: 6,
                    paddingHorizontal: 4,
                    paddingVertical: 1,
                    borderRadius: 4,
                    fontFamily: FontFamily.Outfit_500Medium,
                    backgroundColor: color.goldBorder,
                    color: color.background,
                  }}
                >
                  NEW
                </Text>
              )}
            </View>

            {/* Title */}
            <Text
              variant="title"
              numberOfLines={2}
              ellipsizeMode="tail"
              style={{
                fontFamily: FontFamily.Outfit_500Medium,
                fontSize: FontSize.sm + 1,
                lineHeight: 18,
              }}
            >
              {title}
            </Text>

            {/* Date + Series */}
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <Text variant="label" size={FontSize.xs}>
                {taughtAt}
              </Text>
              {seriesPart && (
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <Text variant="label" style={{ marginHorizontal: 3 }}>
                    ·
                  </Text>
                  <Icon name="series" size={FontSize.xs + 1} color={Colors.series} />
                  <Text variant="label" size={FontSize.xs} style={{ color: Colors.series, marginLeft: 2 }}>
                    Series
                  </Text>
                </View>
              )}
            </View>
          </View>
        </OpacityPressable>
      </Animated.View>
    );
  }

  // ── List layout (original, preserved) ────────────────────────────────────
  return (
    <Animated.View entering={FadeIn.duration(220)} exiting={FadeOut.duration(150)} style={{ marginVertical: 6 }}>
      <OpacityPressable onPress={() => router.replace('/teachings')}>
        <View className="flex flex-row rounded-md w-full items-center">
          <View>
            <Image source={thumbnailUrl} className="size-[70px] rounded-md" />
          </View>
          <View className="flex-1 flex-col pl-4">
            <View className="flex-row items-center">
              <TeachingTypeLabel type={type} />
              {isNew && (
                <Text
                  size={FontSize.xs}
                  className="mx-2 p-0.3 px-1 rounded font-bold"
                  style={{ backgroundColor: color.goldBorder, color: color.background }}
                >
                  NEW
                </Text>
              )}
            </View>
            <Text
              variant="title"
              numberOfLines={2}
              ellipsizeMode="tail"
              className="my-1 text-wrap text-base leading-snug"
              style={{ fontFamily: FontFamily.Outfit_500Medium }}
            >
              {title}
            </Text>
            <View className="mt-auto flex-row items-center">
              <Text variant="label">{taughtAt}</Text>
              {seriesPart && (
                <View className="flex-row items-center">
                  <Text variant="label" className="mx-1">
                    •
                  </Text>
                  <Icon name="series" size={FontSize.sm} color={Colors.series} className="mt-0.5" />
                  <Text variant="label" className="mx-0.5" style={{ color: Colors.series }}>
                    Series
                  </Text>
                </View>
              )}
            </View>
          </View>
          <View className="my-auto m-0">
            <Icon name="chevron.right" size={FontSize.lg} color={color.icon} />
          </View>
        </View>
      </OpacityPressable>
    </Animated.View>
  );
}
