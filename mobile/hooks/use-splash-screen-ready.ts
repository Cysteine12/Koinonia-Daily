import { AppConfig } from '@/constants';
import * as Sentry from '@sentry/react-native';
import { SplashScreen } from 'expo-router';
import { useEffect, useState } from 'react';
import useAppFonts from './use-app-fonts';

export function useSplashScreenReady() {
  const [fontsLoaded, fontError] = useAppFonts();
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
