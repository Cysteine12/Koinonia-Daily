/**
 * Below are the colors that are used in the app. The colors are defined in the light and dark mode.
 * There are many other ways to style your app. For example, [Nativewind](https://www.nativewind.dev/), [Tamagui](https://tamagui.dev/), [unistyles](https://reactnativeunistyles.vercel.app), etc.
 */

import { Platform } from 'react-native';

export const GOLD_GRADIENT = {
  dark: ['hsl(48 80% 50%)', 'hsl(48 70% 60%)', 'hsl(45 71% 36%)'] as const,
  light: ['hsl(48 72% 65%)', 'hsl(45 66% 52%)', 'hsl(45 60% 44%)'] as const,
};

const tintColorLight = '#0a7ea4';
const tintColorDark = '#fff';

export const Colors = {
  light: {
    text: '#0F0D0A',
    textMuted: '#71717A',
    goldText: 'hsl(45 71% 36%)',
    goldTextMuted: '#F5EBCA',
    background: '#fff',
    containerBackground: 'hsl(0 0% 95%)',
    secondaryBackground: 'hsl(0 0% 95%)',
    inputBackground: '#F4F5F7',
    cardBackground: 'hsl(0 0% 100%)',
    cardBorder: '#E8E0D4',
    border: '#D1D5DB',
    goldBorder: 'hsl(45 60% 44%)',
    input: 'hsl(0 0% 89.8%)',
    tint: tintColorLight,
    icon: '#687076',
    tabIconDefault: '#687076',
    tabIconSelected: tintColorLight,
  } as const,
  dark: {
    text: '#F2EBE0',
    textMuted: '#A1A1AA',
    goldText: 'hsl(48 70% 60%)',
    goldTextMuted: 'rgba(196, 154, 60, 0.15)',
    background: '#151718',
    containerBackground: 'hsl(0 0% 5%)',
    secondaryBackground: 'hsl(0 0% 0%)',
    inputBackground: '#1E1E1E',
    cardBackground: 'hsl(0 0% 3.9%)',
    cardBorder: '#2A2720',
    border: '#2D2D2D',
    goldBorder: 'hsl(48 80% 50%)',
    input: 'hsl(0 0% 14.9%)',
    tint: tintColorDark,
    icon: '#9BA1A6',
    tabIconDefault: '#9BA1A6',
    tabIconSelected: tintColorDark,
  } as const,
  primary: '#6A5CA3',
  lightPrimary: '#EDEAF5',
  secondary: '#E6E2F3',
  tertiary: '#E03971',
  warning: '#F25C54',
  goldIcon: 'hsl(48 80% 50%)',
  tabBackground: '#0A0906',
  cardOverlay: 'rgba(0, 0, 0, 0.5)',
  purple: '#6A5CA3',
  series: '#8B5CF6',
  collections: '#F59E0B',
  bookmarks: '#EC4899',
  downloads: '#06B6D4',
} as const;

export const Fonts = Platform.select({
  ios: {
    /** iOS `UIFontDescriptorSystemDesignDefault` */
    sans: 'system-ui',
    /** iOS `UIFontDescriptorSystemDesignSerif` */
    serif: 'ui-serif',
    /** iOS `UIFontDescriptorSystemDesignRounded` */
    rounded: 'ui-rounded',
    /** iOS `UIFontDescriptorSystemDesignMonospaced` */
    mono: 'ui-monospace',
  },
  default: {
    sans: 'normal',
    serif: 'serif',
    rounded: 'normal',
    mono: 'monospace',
  },
});
