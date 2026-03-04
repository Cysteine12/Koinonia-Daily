import { useScreenOptions } from '@/hooks/use-screen-options';
import { Stack, useRouter } from 'expo-router';
import { useEffect } from 'react';
import { useAuth } from '../../features/auth/auth-context';

const GuestLayout = () => {
  const { isAuthenticated } = useAuth();
  const { screenOptions } = useScreenOptions();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated) router.replace('/home');
  }, [isAuthenticated, router]);

  if (isAuthenticated) {
    return null;
  }

  return (
    <Stack screenOptions={screenOptions}>
      <Stack.Screen name="login" options={{ headerShown: false }} />
      <Stack.Screen name="register" options={{ headerShown: false }} />
      <Stack.Screen name="verify-email" options={{ headerShown: false }} />
      <Stack.Screen name="forgot-password" options={{ headerTitle: '', headerShown: true }} />
      <Stack.Screen name="reset-password" options={{ headerTitle: '', headerShown: true }} />
    </Stack>
  );
};

export default GuestLayout;
