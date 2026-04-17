import { FontFamily, FontSize } from '@/constants';
import { useThemeColor } from '@/hooks/use-theme-color';
import { cn } from '@/lib/utils';
import { Text as RNText, StyleSheet, type TextProps as RNTextProps } from 'react-native';

export type TextProps = RNTextProps & {
  variant?: 'default' | 'title' | 'subtitle' | 'link' | 'label';
  weight?: 'regular' | 'semibold' | 'bold';
  size?: number;
  sizeKey?: keyof typeof FontSize;
  family?: string;
  className?: string;
  lightColor?: string;
  darkColor?: string;
};

export function Text({
  style,
  className,
  lightColor,
  darkColor,
  variant = 'default',
  weight = 'regular',
  size,
  sizeKey,
  family,
  ...rest
}: TextProps) {
  const color = useThemeColor(
    { light: lightColor, dark: darkColor },
    variant === 'link' ? 'tint' : variant === 'label' ? 'textMuted' : 'text'
  );

  return (
    <RNText
      className={cn(className)}
      style={[
        { color },
        weightStyles[weight],
        sizeKey ? { fontSize: FontSize[sizeKey] } : undefined,
        size ? { fontSize: size } : undefined,
        family ? { fontFamily: family } : undefined,
        style,
      ]}
      {...rest}
    />
  );
}

const weightStyles = StyleSheet.create({
  regular: { fontFamily: 'regular' },
  semibold: { fontFamily: FontFamily.Outfit_600SemiBold },
  bold: { fontFamily: FontFamily.Outfit_700Bold },
});
