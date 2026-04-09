import { useEffect, useRef } from 'react';
import { useAnimatedStyle, useSharedValue, withTiming } from 'react-native-reanimated';

export default function useOverlayOpacity(resolvedTheme: 'light' | 'dark') {
  const overlayOpacity = useSharedValue(0);
  const hasMountedRef = useRef(false);

  useEffect(() => {
    if (!hasMountedRef.current) {
      hasMountedRef.current = true;
      return;
    }
    overlayOpacity.value = 1;
    overlayOpacity.value = withTiming(0, { duration: 1000 });
  }, [resolvedTheme, overlayOpacity]);

  const overlayStyle = useAnimatedStyle(() => ({
    opacity: overlayOpacity.value,
  }));
  return { overlayStyle };
}
