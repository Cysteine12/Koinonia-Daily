import { Icon, Text, View } from '@/components/core';
import { FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { ScrollView, TouchableOpacity } from 'react-native';

interface ActiveFiltersRowProps {
  selectedFilterTags: string[];
  selectedDateRange: [Date | null, Date | null];
  onRemoveTag: (tag: string) => void;
  onClearDateRange: () => void;
  onClearAll: () => void;
}

/** Formats a date as "Jan 12, 2025" */
const fmt = (d: Date) =>
  d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });

/** Builds a human-readable date range label */
function buildDateLabel(from: Date | null, to: Date | null): string {
  if (from && to) return `${fmt(from)} – ${fmt(to)}`;
  if (from) return `From ${fmt(from)}`;
  if (to) return `To ${fmt(to)}`;
  return '';
}

export default function ActiveFiltersRow({
  selectedFilterTags,
  selectedDateRange,
  onRemoveTag,
  onClearDateRange,
  onClearAll,
}: ActiveFiltersRowProps) {
  const { color } = useAppTheme();
  const [from, to] = selectedDateRange;
  const hasDate = !!(from || to);
  const hasTags = selectedFilterTags.length > 0;

  if (!hasDate && !hasTags) return null;

  return (
    <View className="flex-row items-center pt-1.5 pb-2.5">
      {/* Scrollable chip area */}
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        keyboardShouldPersistTaps="always"
        contentContainerStyle={{ alignItems: 'center' }}
        style={{ flex: 1 }}
      >
        {/* Date range chip */}
        {hasDate && (
          <View
            className="flex-row items-center rounded-full py-1 px-3 mr-1.5 border gap-1"
            style={{ borderColor: color.goldBorder, backgroundColor: color.goldTextMuted }}
          >
            <Icon name="calendar.outline" size={FontSize.xs} color={color.goldText} />
            <Text
              size={FontSize.xs}
              weight="semibold"
              numberOfLines={1}
              style={{ color: color.goldText }}
            >
              {buildDateLabel(from, to)}
            </Text>
            <TouchableOpacity onPress={onClearDateRange} hitSlop={8}>
              <Icon name="close.circle" size={FontSize.xs + 1} color={color.goldText} />
            </TouchableOpacity>
          </View>
        )}

        {/* Topic tag chips */}
        {selectedFilterTags.map((tag) => (
          <View
            key={tag}
            className="flex-row items-center rounded-full py-1 px-3 mr-1.5 border gap-1"
            style={{ borderColor: color.goldBorder, backgroundColor: color.goldTextMuted }}
          >
            <Text
              size={FontSize.xs}
              weight="semibold"
              numberOfLines={1}
              style={{ color: color.goldText }}
            >
              {tag}
            </Text>
            <TouchableOpacity onPress={() => onRemoveTag(tag)} hitSlop={8}>
              <Icon name="close.circle" size={FontSize.xs + 1} color={color.goldText} />
            </TouchableOpacity>
          </View>
        ))}
      </ScrollView>

      {/* Clear-all × — pinned right, outside scroll */}
      <TouchableOpacity
        onPress={onClearAll}
        hitSlop={10}
        className="ml-2 p-1 rounded-full items-center justify-center"
        style={{ backgroundColor: color.cardBorder }}
      >
        <Icon name="close" size={FontSize.sm} color={color.textMuted} />
      </TouchableOpacity>
    </View>
  );
}
