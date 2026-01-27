import { Stack, useRouter } from 'expo-router';
import { useEffect } from 'react';
import { useAuth } from '../../features/auth/auth-context';
import { screenOptions } from '../_layout';

const GuestLayout = () => {
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated) router.replace('/home');
  }, [isAuthenticated, router]);

  return (
    <Stack screenOptions={screenOptions}>
      <Stack.Screen name="login" options={{ headerShown: false }} />
      <Stack.Screen name="register" options={{ headerShown: false }} />
      <Stack.Screen name="verify-email" options={{ headerShown: false }} />
    </Stack>
  );
};

export default GuestLayout;
