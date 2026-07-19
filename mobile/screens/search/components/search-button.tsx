import { Icon, Text } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';

export default function SearchButton({ toggleSearchState }: { toggleSearchState: () => void }) {
  const { color } = useAppTheme();

  return (
    <OpacityPressable
      onPressIn={toggleSearchState}
      activeOpacity={0.5}
      activeScale={1}
      duration={0}
      accessibilityRole="button"
      accessibilityLabel="Open Search"
      className="flex-row items-center border-2 rounded-xl p-4"
      style={{ backgroundColor: color.cardBackground, borderColor: color.border }}
    >
      <Icon name="search" size={20} color={color.textMuted} className="mr-2" />
      <Text variant="label" size={FontSize.base}>
        Search teachings, topics, quotes, and more
      </Text>
    </OpacityPressable>
  );
}
