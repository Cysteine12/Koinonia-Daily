import { Icon, Text, View, type IconSymbolName } from '@/components/core';
import { FontFamily, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { hexToRgba } from '@/lib/utils';

interface StatsCardProps {
  icon: IconSymbolName;
  title: string;
  subtitle: string;
  tagColor: string;
  iconColor: string;
}

export default function StatsCard({ icon, title, subtitle, tagColor, iconColor }: StatsCardProps) {
  const { color } = useAppTheme();

  return (
    <View
      className="flex-1 justify-center items-center rounded-xl border"
      style={{
        backgroundColor: color.cardBackground,
        borderColor: color.cardBorder,
        borderBottomColor: tagColor,
        borderBottomWidth: 2,
        paddingVertical: 12,
        marginHorizontal: 3,
      }}
    >
      <View className="p-1 rounded-full" style={{ backgroundColor: hexToRgba(iconColor, 0.15) }}>
        <Icon name={icon} size={FontSize.lg} color={iconColor} />
      </View>
      <Text size={FontSize.xl} style={{ fontFamily: FontFamily.Lora_500Medium }}>
        {title}
      </Text>
      <Text variant="label" size={FontSize.xs}>
        {subtitle}
      </Text>
    </View>
  );
}
