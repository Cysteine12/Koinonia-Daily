import { DarkTheme, DefaultTheme, ThemeProvider } from '@react-navigation/native';
import { PortalHost } from '@rn-primitives/portal';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { SplashScreen, Stack, useRouter } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import 'react-native-reanimated';
import '../global.css';

import { IconSymbol } from '@/components/ui/icon-symbol';
import { AppConfig } from '@/constants/app-config';
import { AuthProvider } from '@/features/auth/auth-context';
import { useAppTheme } from '@/hooks/use-app-theme';
import * as Sentry from '@sentry/react-native';
import { useEffect, useState } from 'react';
import { Pressable, View } from 'react-native';
import { useFonts } from 'expo-fonts';

SplashScreen.preventAutoHideAsync();

const queryClient = new QueryClient();

function AppLayout() {
  const [fontsLoaded] = useFonts({
    'Inter-Regular': require('../assets/fonts/Inter-Regular.ttf'),

  })
  const [isLayoutReady, setLayoutReady] = useState(false);
  const [hasPassedMinDelay, setHasPassedMinDelay] = useState(false);
  const [isAppReady, setAppReady] = useState(false);

  // Ensure splash shows for minimum time
  useEffect(() => {
    const timer = setTimeout(() => setHasPassedMinDelay(true), AppConfig.LOADING.SPLASH_MIN_DISPLAY_MS);
    return () => clearTimeout(timer);
  }, []);

  // Hide splash when all conditions are met
  useEffect(() => {
    const shouldHide = fontsLoaded && isLayoutReady && hasPassedMinDelay;

    if (shouldHide) {
      (async () => {
        try {
          await SplashScreen.hideAsync();
        } catch (error) {
          console.error('Splash screen error:', error);
          Sentry.captureException(error);
        } finally {
          setAppReady(true);
        }
      })();
    }
  }, [fontsLoaded, isLayoutReady, hasPassedMinDelay]);

  return (
    <View
      // style={{
      //   flex: 1,
      //   backgroundColor,
      // }}
      onLayout={() => setLayoutReady(true)}
      accessibilityLabel="app-root-view"
    >
      {isAppReady && (
        <>
          <Navigation />
          <StatusBar style="auto" />
          <PortalHost />
        </>
      )}
    </View>
  );
}

function Navigation() {
  return (
    <Stack>
      <Stack.Screen name="(auth)" options={{ headerShown: false }} />
      <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
      <Stack.Screen name="+not-found" />
    </Stack>
  );
}

export default function RootLayout() {
  const { theme } = useAppTheme();

  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <ThemeProvider value={theme === 'dark' ? DarkTheme : DefaultTheme}>
          <AppLayout />
        </ThemeProvider>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export const useScreenOptions = () => {
  const router = useRouter();
  const { color } = useAppTheme();

  const screenOptions = {
    headerStyle: {},
    headerTitleStyle: { fontSize: 20 },
    headerTitleAlign: 'center' as 'center',
    headerLeft: () => (
      <Pressable
        style={({ pressed }) => ({
          opacity: pressed ? 0.7 : 1,
          paddingVertical: 8,
          paddingRight: 16,
        })}
        onPress={() => router.back()}
      >
        <IconSymbol name="chevron.left" size={28} color={color.text} />
      </Pressable>
    ),
  };

  return { screenOptions };
};
