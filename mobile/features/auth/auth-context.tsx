import API from '@/lib/api';
import { attachAuthInterceptors } from '@/lib/authInterceptor';
import { deleteSecure, getSecure, saveSecure } from '@/lib/storage';
import React, { createContext, useContext, useEffect, useRef, useState } from 'react';
import { useAccountProfile } from '../account/hook';
import { User } from '../user/types';
import { router } from 'expo-router';

type AuthContextType = {
  token: string | null;
  login: (acccessToken: string, refreshToken: string) => Promise<void>;
  logout: () => Promise<void>;
  isAuthenticated: boolean;
};

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const { data: userData, refetch } = useAccountProfile();
  const interceptorCleanup = useRef<() => void>(null);

  useEffect(() => {
    (async () => {
      const token = await getSecure('accessToken');
      if (token) setToken(token);
      if (!user) refetch();
    })();
  }, []);

  useEffect(() => {
    if (userData?.data) setUser(userData.data);
  }, [userData]);

  const login = async (accessToken: string, refreshToken: string) => {
    setToken(accessToken);
    await saveSecure('accessToken', accessToken);
    await saveSecure('refreshToken', refreshToken);
    refetch();
  };

  const logout = async () => {
    setToken(null);
    await deleteSecure('accessToken');
    await deleteSecure('refreshToken');
  };

  const refreshToken = async (): Promise<string> => {
    const refresh = await getSecure('refreshToken');
    if (!refresh) {
      // redirect to login
      router.replace('/login');
    }

    const { data } = await API.post('/api/auth/refresh-token', {
      refreshToken: refresh,
    });

    setToken(data.accessToken);
    await saveSecure('refreshToken', data.refreshToken);

    return data.accessToken;
  };

  useEffect(() => {
    interceptorCleanup.current = attachAuthInterceptors(API, {
      getAccessToken: () => token,
      refreshToken,
      onLogout: logout,
    });

    return () => interceptorCleanup.current?.();
  }, [token]);

  return (
    <AuthContext.Provider
      value={{
        token,
        login,
        logout,
        isAuthenticated: !!token,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');

  return ctx;
};
