import { useFocusEffect } from '@react-navigation/native';
import { useCallback } from 'react';
import { BackHandler } from 'react-native';

interface UseBackHandlerOptions {
  isActive: boolean;
  handler: () => void;
}

export default function useBackHandler({ isActive, handler }: UseBackHandlerOptions) {
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
