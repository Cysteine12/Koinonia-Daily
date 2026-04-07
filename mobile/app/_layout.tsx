import { DarkTheme, DefaultTheme, ThemeProvider } from '@react-navigation/native';
import { PortalHost } from '@rn-primitives/portal';
import * as Sentry from '@sentry/react-native';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { SplashScreen, Stack } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import 'react-native-reanimated';
import '../global.css';

import { AuthProvider } from '@/features/auth/auth-context';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useSplashScreenReady } from '@/hooks/use-splash-screen-ready';
import { useEffect, useRef } from 'react';
import { Animated, View } from 'react-native';
import { SafeAreaProvider } from 'react-native-safe-area-context';

void SplashScreen.preventAutoHideAsync().catch((err) => {
  Sentry.captureException(err);
});

const queryClient = new QueryClient();

function AppLayout() {
  const { isAppReady, setLayoutReady } = useSplashScreenReady();

  const opacity = useRef(new Animated.Value(0));

  useEffect(() => {
    if (isAppReady) {
      Animated.timing(opacity.current, {
        toValue: 1,
        duration: 350,
        useNativeDriver: true,
      }).start();
    }
  }, [isAppReady]);

  return (
    <View className="flex-1" onLayout={() => setLayoutReady(true)} accessibilityLabel="app-root-view">
      {isAppReady && (
        <Animated.View style={{ flex: 1, opacity: opacity.current }}>
          <Navigation />
          <StatusBar style="auto" />
          <PortalHost />
        </Animated.View>
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
        <SafeAreaProvider>
          <ThemeProvider value={theme === 'dark' ? DarkTheme : DefaultTheme}>
            <AppLayout />
          </ThemeProvider>
        </SafeAreaProvider>
      </AuthProvider>
    </QueryClientProvider>
  );
}
