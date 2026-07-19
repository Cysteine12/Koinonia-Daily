import { Icon, Text, View } from '@/components/core';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { FontFamily, FontSize } from '@/constants';
import { useAppSettings } from '@/features/settings-context';
import { useAppTheme } from '@/hooks/use-app-theme';
import SortButton from './sort-button';
import { useState } from 'react';
import FilterButton from './filter-button';

interface TeachingFiltersProp {
  teachingsCount: number;
  tags: string[];
  selectedFilterTags: string[];
  setSelectedFilterTags: (tags: string[]) => void;
  selectedDateRange: [Date | null, Date | null];
  setSelectedDateRange: (dateRanges: [Date | null, Date | null]) => void;
  onFilterSubmit: (filterTags: string[], dateRange: [Date | null, Date | null]) => void;
}

export default function TeachingFilters({
  teachingsCount,
  tags,
  selectedFilterTags,
  setSelectedFilterTags,
  selectedDateRange,
  setSelectedDateRange,
  onFilterSubmit,
}: TeachingFiltersProp) {
  const { color } = useAppTheme();
  const { settings, setSetting } = useAppSettings();
  const [selectedSortType, setSelectedSortType] = useState('newest');

  const layouts = [
    {
      type: 'list',
      icon: 'list',
      selectedIcon: 'list',
      className: 'rounded-l-lg',
      isSelected: 'list' === settings.TEACHING_LIBRARY_LAYOUT,
    } as const,
    {
      type: 'grid',
      icon: 'grid.outline',
      selectedIcon: 'grid',
      className: 'rounded-r-lg',
      isSelected: 'grid' === settings.TEACHING_LIBRARY_LAYOUT,
    } as const,
  ];

  return (
    <View className="flex-row items-center py-4 justify-between">
      <Text variant="subtitle" size={FontSize.sm + 2} style={{ fontFamily: FontFamily.Lora_400Regular_Italic }}>
        {teachingsCount} teachings
      </Text>

      <View className="flex-row">
        <FilterButton
          tags={tags}
          selectedDateRange={selectedDateRange}
          setSelectedDateRange={setSelectedDateRange}
          selectedFilterTags={selectedFilterTags}
          setSelectedFilterTags={setSelectedFilterTags}
          onSubmit={onFilterSubmit}
        />

        <SortButton selectedSortType={selectedSortType} setSelectedSortType={setSelectedSortType} />

        <View
          className="border rounded-lg flex-row items-center ml-1"
          style={{ backgroundColor: color.background, borderColor: color.border }}
        >
          {layouts.map((layout) => (
            <OpacityPressable
              key={layout.type}
              onPress={() => setSetting('TEACHING_LIBRARY_LAYOUT', layout.type)}
              className={`py-1 px-2 ${layout.className}`}
              style={{ backgroundColor: layout.isSelected ? color.inputBackground : color.background }}
            >
              <Icon
                name={layout.isSelected ? layout.selectedIcon : layout.icon}
                size={FontSize.sm + (layout.type === 'list' ? 2 : 0)}
                color={layout.isSelected ? color.goldBorder : color.icon}
              />
            </OpacityPressable>
          ))}
        </View>
      </View>
    </View>
  );
}
