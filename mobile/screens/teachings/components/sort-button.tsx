import { Icon, Text } from '@/components/core';
import BottomSheet from '@/components/ui/bottom-sheet';
import OpacityPressable from '@/components/ui/opacity-pressable';
import { Colors, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useState } from 'react';

interface SortButtonProps {
  selectedSortType: string;
  setSelectedSortType: (type: string) => void;
}

export default function SortButton({ selectedSortType, setSelectedSortType }: SortButtonProps) {
  const { color } = useAppTheme();
  const [isVisible, setVisible] = useState(false);
  const sorts = [
    {
      type: 'newest',
      name: 'Newest first',
    },
    {
      type: 'oldest',
      name: 'Oldest first',
    },
    {
      type: 'A-Z',
      name: 'Title A-Z',
    },
    {
      type: 'Z-A',
      name: 'Title Z-A',
    },
    {
      type: 'popular',
      name: 'Most popular',
    },
  ] as const;

  return (
    <>
      <OpacityPressable
        onPress={() => setVisible(true)}
        className="border py-1 px-2 rounded-lg flex-row items-center ml-1"
        style={{ backgroundColor: color.inputBackground, borderColor: color.goldBorder }}
      >
        <Icon name="filter" size={FontSize.sm} color={Colors.goldIcon} />
        <Text weight="semibold" size={FontSize.sm + 2} className="ml-1 capitalize" style={{ color: color.goldText }}>
          {selectedSortType}
        </Text>
      </OpacityPressable>

      <BottomSheet visible={isVisible} onClose={() => setVisible(false)} title="Sort By">
        {sorts.map((sort) => (
          <OpacityPressable
            key={sort.type}
            onPress={() => {
              setSelectedSortType(sort.type);
              setVisible(false);
            }}
            className="flex-row items-center justify-between my-2.5 pr-2"
          >
            <Text className="font-semibold" size={FontSize.md}>
              {sort.name}
            </Text>
            <Icon
              name={selectedSortType === sort.type ? 'radio.on' : 'radio.off'}
              size={FontSize.lg}
              color={selectedSortType === sort.type ? Colors.goldIcon : color.icon}
            />
          </OpacityPressable>
        ))}
      </BottomSheet>
    </>
  );
}
