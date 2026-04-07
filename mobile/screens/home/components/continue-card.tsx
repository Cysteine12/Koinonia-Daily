import { Text, View } from '@/components/core';
import ScalePressable from '@/components/reusables/ui/scale-pressable';
import { Colors, FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useThemeColor } from '@/hooks/use-theme-color';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { Image } from 'react-native';

export default function ContinueCard() {
  const router = useRouter();
  const { color } = useAppTheme();
  const backgroundColor = useThemeColor({ light: '#0F0D0A' }, 'cardBackground');

  return (
    <ScalePressable className="w-full" onPress={() => router.push('/home')}>
      <View className="flex flex-row rounded-2xl border w-full" style={{ backgroundColor, borderColor: color.cardBorder }}>
        <View className="">
          <Image source={require('@/assets/images/i4.jpg')} className="size-24 rounded-l-2xl" />
        </View>
        <View className="flex-1 flex-col h-24 py-2 pl-4">
          <Text className="uppercase" size={FontSize.xs} style={{ color: Colors.dark.goldText }}>
            Sunday Service
          </Text>
          <Text
            variant="title"
            numberOfLines={2}
            ellipsizeMode="tail"
            className="my-1 text-wrap text-base"
            style={{ color: Colors.dark.text, fontFamily: FontFamily.Lora_500Medium }}
          >
            Commanding the Supernatural: The Priniciples of Dominion
          </Text>
          <Text variant="label" className="mt-auto">
            Last read 2 days ago • 8 min read
          </Text>
        </View>
        <View className="my-auto m-0">
          <Ionicons name="chevron-forward" size={FontSize.xxl} color={Colors.dark.text} />
        </View>
      </View>
    </ScalePressable>
  );
}
