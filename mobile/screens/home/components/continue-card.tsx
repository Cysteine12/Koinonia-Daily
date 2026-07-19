import { Image, type ImageSourcePropType, Text, View } from '@/components/core';
import ScalePressable from '@/components/ui/scale-pressable';
import TeachingTypeLabel from '@/components/ui/teaching-type-label';
import { Colors, FontFamily, FontSize } from '@/constants';
import type { TeachingType } from '@/features/teaching';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useThemeColor } from '@/hooks/use-theme-color';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';

interface ContinueCardProps {
  title: string;
  thumbnailUrl: ImageSourcePropType;
  type: TeachingType;
  lastRead: string;
}

export default function ContinueCard({ title, thumbnailUrl, type, lastRead }: ContinueCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();
  const backgroundColor = useThemeColor({ light: '#0F0D0A' }, 'cardBackground');

  return (
    <ScalePressable className="w-full" onPress={() => router.push('/home')}>
      <View className="flex flex-row rounded-2xl border w-full" style={{ backgroundColor, borderColor: color.cardBorder }}>
        <View className="">
          <Image source={thumbnailUrl} className="size-24 rounded-l-2xl" />
        </View>
        <View className="flex-1 flex-col h-24 py-2 pl-4">
          <TeachingTypeLabel type={type} />
          <Text
            variant="title"
            numberOfLines={2}
            ellipsizeMode="tail"
            className="my-1 text-wrap text-base"
            style={{ color: Colors.dark.text, fontFamily: FontFamily.Lora_500Medium }}
          >
            {title}
          </Text>
          <Text variant="label" className="mt-auto">
            {lastRead}
          </Text>
        </View>
        <View className="my-auto m-0">
          <Ionicons name="chevron-forward" size={FontSize.xxl} color={Colors.dark.text} />
        </View>
      </View>
    </ScalePressable>
  );
}
