import { AppConfig } from '@/constants/app-config';
import * as Sentry from '@sentry/react-native';
import { useFonts } from 'expo-font';
import { SplashScreen } from 'expo-router';
import { useEffect, useState } from 'react';

export function useSplashScreenReady() {
  const [fontsLoaded, fontError] = useFonts({
    'SpaceMono-Regular': require('@/assets/fonts/SpaceMono-Regular.ttf'),
  });
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
    if (fontError) {
      Sentry.captureException(fontError);
    }
    const shouldHide = (fontsLoaded || !!fontError) && isLayoutReady && hasPassedMinDelay && !isAppReady;

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
  }, [fontsLoaded, fontError, isAppReady, isLayoutReady, hasPassedMinDelay]);

  return { isAppReady, setLayoutReady };
}
