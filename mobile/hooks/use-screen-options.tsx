import { Icon } from '@/components/core';
import { useRouter } from 'expo-router';
import { Pressable } from 'react-native';
import { useAppTheme } from './use-app-theme';

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
        <Icon name="chevron.left" size={36} color={color.text} />
      </Pressable>
    ),
  };

  return { screenOptions };
};
