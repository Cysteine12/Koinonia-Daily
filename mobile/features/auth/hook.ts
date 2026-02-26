import type { ErrorResponse } from '@/lib/types';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { useRouter } from 'expo-router';
import { Alert } from 'react-native';
import { login, logout, register, requestOtp, verifyEmail } from './api';
import { useAuth } from './auth-context';
import type { LoginSchema, LogoutSchema, RegisterSchema, VerifyEmailSchema } from './schema';
import { useAuthStore } from './store';

const useRegister = () => {
  const { setCredentials } = useAuthStore();
  const router = useRouter();

  return useMutation({
    mutationFn: (payload: RegisterSchema) => {
      setCredentials(payload);
      return register(payload);
    },
    onSuccess: (data) => {
      router.replace('/verify-email');
    },
    onError: (data: AxiosError<ErrorResponse>) => {
      setCredentials(null);
      Alert.alert('Registration failed', data.response?.data?.message || data.message);
    },
  });
};

const useLogin = () => {
  const queryClient = useQueryClient();
  const router = useRouter();
  const { login: authLogin } = useAuth();
  const { setCredentials } = useAuthStore();

  return useMutation({
    mutationFn: (payload: LoginSchema) => {
      setCredentials(payload);
      return login(payload);
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['auth'] });

      setCredentials(null);
      if (!data.data) {
        return Alert.alert('Server Error', 'No response data');
      }

      authLogin(data.data.accessToken, data.data.refreshToken);

      router.replace('/home');
    },
    onError: (data: AxiosError<ErrorResponse>) => {
      if (data.response?.data?.code === 'USER_NOT_VERIFIED') {
        router.push('/verify-email');
        return;
      }
      setCredentials(null);
      Alert.alert('Login failed', data.response?.data?.message || data.message);
    },
  });
};

const useVerifyEmail = () => {
  const { mutate: login } = useLogin();
  const { credentials } = useAuthStore();
  const router = useRouter();

  return useMutation({
    mutationFn: (payload: VerifyEmailSchema) => verifyEmail(payload),
    onSuccess: (data) => {
      if (!credentials) {
        Alert.alert('Error', 'No credentials found for login');
        router.push('/login');
        return;
      }
      login({ email: credentials.email, password: credentials.password });
    },
    onError: (data: AxiosError<ErrorResponse>) => {
      Alert.alert('Verification failed', data.response?.data?.message || data.message);
    },
  });
};

const useRequestOtp = () => {
  const { credentials } = useAuthStore();
  const router = useRouter();

  return useMutation({
    mutationFn: () => {
      if (!credentials) {
        router.push('/register');
        throw new Error('No credentials found for requesting OTP');
      }
      return requestOtp({ email: credentials.email });
    },
    onSuccess: (data) => {
      Alert.alert('OTP Sent', 'A new OTP has been sent to your email.');
      return data;
    },
    onError: (data: AxiosError<ErrorResponse>) => {
      Alert.alert('Request OTP failed', data.response?.data?.message || data.message);
    },
  });
};

const useLogout = () => {
  const queryClient = useQueryClient();
  const { logout: authLogout } = useAuth();

  return useMutation({
    mutationFn: (payload: LogoutSchema) => logout(payload),
    onSuccess: (data) => {
      queryClient.clear();

      authLogout();
    },
    onError: (data: AxiosError<ErrorResponse>) => {
      queryClient.clear();
      Alert.alert('Logout failed', data.response?.data?.message || data.message);
      authLogout();
    },
  });
};

export { useLogin, useLogout, useRegister, useRequestOtp, useVerifyEmail };
