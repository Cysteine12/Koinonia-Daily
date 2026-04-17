import { Colors, FontSize } from '@/constants';
import { useTheme } from '@/features/theme-context';

export function useAppTheme() {
  const { themeMode, resolvedTheme, setThemeMode } = useTheme();

  return {
    theme: resolvedTheme,
    themeMode,
    setThemeMode,
    color: Colors[resolvedTheme],
    isDark: resolvedTheme === 'dark',
    typography: FontSize,
  };
}
