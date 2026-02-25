import { DarkTheme, DefaultTheme, ThemeProvider } from '@react-navigation/native';
import { Stack } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import { PortalHost } from '@rn-primitives/portal';
import 'react-native-reanimated';
import '../global.css';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import { useColorScheme } from '@/hooks/use-color-scheme';
import { StyleSheet } from 'react-native';
import { AuthProvider } from '@/features/auth/auth-context';

const queryClient = new QueryClient();

/**
 * Provides the application's root layout including global providers, theming, navigation, status bar, and portal host.
 *
 * Renders React Query and authentication providers, applies a theme based on the system color scheme, and defines the top-level navigation screens "(auth)", "(tabs)", and "+not-found".
 *
 * @returns The root React element for the application's layout.
 */
export default function RootLayout() {
  const colorScheme = useColorScheme();

  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <ThemeProvider value={colorScheme === 'dark' ? DarkTheme : DefaultTheme}>
          <Stack>
            <Stack.Screen name="(auth)" options={{ headerShown: false }} />
            <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
            <Stack.Screen name="+not-found" />
          </Stack>
          <StatusBar style="auto" />
          <PortalHost />
        </ThemeProvider>
      </AuthProvider>
    </QueryClientProvider>
  );
}

const styles = StyleSheet.create({
  header: {
    backgroundColor: '#000',
  },
});

export const screenOptions = {
  headerStyle: styles.header,
  headerTintColor: '#fff',
  headerTitleStyle: { fontSize: 20, fontWeight: 'bold' as 'bold' },
  headerTitleAlign: 'center' as 'center',
};
