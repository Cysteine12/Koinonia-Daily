import type { ApiResponse } from '@/lib/types';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { useRouter } from 'expo-router';
import { Alert } from 'react-native';
import { login, logout, register } from './api';
import { useAuth } from './auth-context';
import type { LoginSchema, LogoutSchema, RegisterSchema } from './schema';

const useRegister = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (payload: RegisterSchema) => register(payload),
    onSuccess: (data: ApiResponse) => {
      queryClient.invalidateQueries({ queryKey: ['auth'] });
    },
    onError: (data: AxiosError<ApiResponse>) => {
      Alert.alert('Registration failed', data.response?.data?.message || data.message);
    },
  });
};

const useLogin = () => {
  const queryClient = useQueryClient();
  const router = useRouter();
  const { login: authLogin } = useAuth();

  return useMutation({
    mutationFn: (payload: LoginSchema) => login(payload),
    onSuccess: (data: ApiResponse<{ accessToken: string; refreshToken: string }>) => {
      queryClient.invalidateQueries({ queryKey: ['auth'] });
      if (!data.data) {
        return Alert.alert('Server Error', 'No response data');
      }
      authLogin(data.data.accessToken, data.data.refreshToken);

      router.replace('/home');
    },
    onError: (data: AxiosError<ApiResponse>) => {
      if (data.response?.data?.message === 'verify-email') {
        router.push('/verify-email');
        return;
      }
      Alert.alert('Login failed', data.response?.data?.message || data.message);
    },
  });
};

const useLogout = () => {
  const queryClient = useQueryClient();
  const router = useRouter();
  const { logout: authLogout } = useAuth();

  return useMutation({
    mutationFn: (payload: LogoutSchema) => logout(payload),
    onSuccess: (data: ApiResponse) => {
      queryClient.invalidateQueries({ queryKey: ['auth'] });

      authLogout();
      router.replace('/login');
    },
    onError: (data: AxiosError<ApiResponse>) => {
      Alert.alert('Logout failed', data.response?.data?.message || data.message);
      authLogout();
      router.replace('/login');
    },
  });
};

export { useLogin, useLogout, useRegister };

