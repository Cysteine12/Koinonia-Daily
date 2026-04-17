import { useFocusEffect } from '@react-navigation/native';
import { useCallback } from 'react';
import { BackHandler } from 'react-native';

interface UseBackToggleOptions {
  isActive: boolean;
  handler: () => void;
}

export default function useBackHandler({ isActive, handler }: UseBackToggleOptions) {
  useFocusEffect(
    useCallback(() => {
      const onBackPress = () => {
        if (isActive) {
          handler();
          return true;
        }
        return false;
      };

      const subscription = BackHandler.addEventListener('hardwareBackPress', onBackPress);

      return () => subscription.remove();
    }, [isActive, handler])
  );
}
