import { Colors } from '@/constants/theme';
import { useColorScheme } from '@/hooks/use-color-scheme';

export function useAppTheme() {
  const theme = useColorScheme() ?? 'light';

  return { theme, color: Colors[theme] };
}
