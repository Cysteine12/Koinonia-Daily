import { Icon, Image, type ImageSourcePropType, Text, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';

interface DiscoveryCardProps {
  id: string;
  title: string;
  thumbnailUrl: ImageSourcePropType;
  type: string;
  createdAt: string;
}

export default function DiscoveryCard({ id, title, thumbnailUrl, type, createdAt }: DiscoveryCardProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <OpacityPressable className="my-1.5" onPress={() => router.push('/(tabs)/search')}>
      <View className="flex flex-row rounded-md w-full">
        <View>
          <Image source={thumbnailUrl} className="size-16 rounded-md" />
        </View>
        <View className="flex-1 flex-col py-0.5 pl-4">
          <Text className="uppercase" size={FontSize.xs} style={{ color: color.goldText }}>
            {type}
          </Text>
          <Text
            variant="title"
            numberOfLines={1}
            ellipsizeMode="tail"
            className="my-1 text-wrap text-base"
            style={{ fontFamily: FontFamily.Outfit_500Medium }}
          >
            {title}
          </Text>
          <Text variant="label" className="mt-auto">
            Added {createdAt}
          </Text>
        </View>
        <View className="my-auto m-0">
          <Icon name="chevron.right" size={FontSize.lg} color={color.text} />
        </View>
      </View>
    </OpacityPressable>
  );
}
