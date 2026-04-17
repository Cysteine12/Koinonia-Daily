import { FontSize } from '@/constants';
import { cn, hexToRgba } from '@/lib/utils';
import { Icon, Text, View, type IconSymbolName } from '../core';

interface TagProps {
  text: string;
  color: string; // Default color to manage border, background, icon and text colors
  icon?: IconSymbolName;
  borderWidth?: number;
  borderColor?: string;
  iconColor?: string;
  textColor?: string;
  backgroundColor?: string;
  className?: string;
  fontSize?: number;
}

export default function Tag({
  text,
  color,
  icon,
  borderWidth = 1,
  borderColor,
  iconColor,
  textColor,
  backgroundColor,
  className,
  fontSize = FontSize.xxs,
}: TagProps) {
  return (
    <View
      className={cn('flex-row items-center rounded-lg py-0.5 px-2', className)}
      style={{ borderColor: borderColor ?? color, backgroundColor: backgroundColor ?? hexToRgba(color, 0.15), borderWidth }}
    >
      {icon && <Icon name={icon} size={fontSize} color={iconColor ?? color} className="mr-0.5" />}
      <Text variant="label" weight="semibold" size={fontSize} style={{ color: textColor ?? color }}>
        {text}
      </Text>
    </View>
  );
}
