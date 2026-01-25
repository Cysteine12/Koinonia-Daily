import { Stack, useRouter } from 'expo-router';
import { useAuth } from '../../features/auth/auth-context';
import { screenOptions } from '../_layout';
import { useEffect } from 'react';

const GuestLayout = () => {
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated) return router.replace('/home');
  }, [isAuthenticated]);

  return (
    <Stack screenOptions={screenOptions}>
      <Stack.Screen name="login" options={{ title: 'Login' }} />
      <Stack.Screen name="verify-email" options={{ title: 'Verify Email' }} />
    </Stack>
  );
};

export default GuestLayout;
