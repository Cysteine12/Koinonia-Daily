import { Text, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter, type Href } from 'expo-router';

interface SectionTitleProps {
  title: string;
  link?: Href;
  linkTitle?: string;
}

export default function SectionTitle({ title, link, linkTitle = 'See more' }: SectionTitleProps) {
  const router = useRouter();
  const { color } = useAppTheme();

  return (
    <View className="mb-3 flex-row items-center">
      <Text size={FontSize.xl} className="font-semibold" style={{ fontFamily: FontFamily.Lora_500Medium }}>
        {title}
      </Text>

      {link && (
        <OpacityPressable onPress={() => router.push(link)} className="ml-auto">
          <Text style={{ color: color.goldText }}>{linkTitle}</Text>
        </OpacityPressable>
      )}
    </View>
  );
}
