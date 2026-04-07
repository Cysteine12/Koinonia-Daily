import { Colors } from '@/constants';
import { useColorScheme } from '@/hooks/use-color-scheme';

export function useAppTheme() {
  const theme = useColorScheme() ?? 'light';

  return { theme, color: Colors[theme], isDark: theme === 'dark' };
}
