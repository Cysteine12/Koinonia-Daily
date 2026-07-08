import { View, Text, Icon, type IconSymbolName } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { FontFamily, FontSize } from '@/constants';
import { hexToRgba } from '@/lib/utils';
import { useRouter, type Href } from 'expo-router';

interface LibraryCardProps {
  icon: IconSymbolName;
  title: string;
  label: string;
  color: string;
  link: Href;
}

export default function LibraryCard({ icon, label, title, color, link }: LibraryCardProps) {
  const router = useRouter();

  return (
    <OpacityPressable
      onPress={() => router.push(link)}
      activeOpacity={0.4}
      className="flex-1 justify-center rounded-xl border p-4"
      style={{
        backgroundColor: hexToRgba(color, 0.15),
        borderColor: hexToRgba(color, 0.3),
      }}
    >
      <View className="self-start mb-3 p-2 rounded-md" style={{ backgroundColor: color }}>
        <Icon name={icon} size={FontSize.lg} color={'#FFFFFF'} />
      </View>
      <Text size={FontSize.md} style={{ fontFamily: FontFamily.Outfit_500Medium }}>
        {title}
      </Text>
      <Text variant="label" size={FontSize.xs}>
        {label}
      </Text>
    </OpacityPressable>
  );
}
