import { Text } from '@/components/core';
import { FontSize } from '@/constants';
import { getTeachingTypeColor, getTeachingTypeText, type TeachingType } from '@/features/teaching';

export default function TeachingTypeLabel({ type }: { type: TeachingType }) {
  return (
    <Text className="uppercase" size={FontSize.xs} style={{ color: getTeachingTypeColor(type) }}>
      {getTeachingTypeText(type)}
    </Text>
  );
}
