import API from '@/lib/api';
import { attachAuthInterceptors } from '@/lib/authInterceptor';
import { deleteSecure, getSecure, saveSecure } from '@/lib/storage';
import React, { createContext, useContext, useEffect, useRef, useState } from 'react';
import { router } from 'expo-router';
import axios from 'axios';

type AuthContextType = {
  token: string | null;
  login: (accessToken: string, refreshToken: string) => Promise<void>;
  logout: () => Promise<void>;
  isAuthenticated: boolean;
};

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [token, setToken] = useState<string | null>(null);
  const interceptorCleanup = useRef<() => void>(null);
  const tokenRef = useRef<string | null>(null);

  const initialized = useRef<boolean | null>(null);

  useEffect(() => {
    if (initialized.current) return;
    initialized.current = true;
    (async () => {
      const token = await getSecure('accessToken');
      if (token) setToken(token);
    })();
  }, []);

  const login = async (accessToken: string, refreshToken: string) => {
    setToken(accessToken);
    await saveSecure('accessToken', accessToken);
    await saveSecure('refreshToken', refreshToken);
  };

  const logout = async () => {
    setToken(null);
    await deleteSecure('accessToken');
    await deleteSecure('refreshToken');
  };

  const refreshToken = async (): Promise<string> => {
    const refresh = await getSecure('refreshToken');
    if (!refresh) {
      router.replace('/login');
      throw new Error('No refresh token available');
    }

    const { data } = await axios.post('/api/auth/refresh-token', {
      refreshToken: refresh,
    });

    setToken(data.accessToken);
    await saveSecure('accessToken', data.accessToken);
    await saveSecure('refreshToken', data.refreshToken);

    return data.accessToken;
  };

  useEffect(() => {
    tokenRef.current = token;
  }, [token]);

  useEffect(() => {
    interceptorCleanup.current = attachAuthInterceptors(API, {
      getAccessToken: () => tokenRef.current,
      refreshToken,
      onLogout: logout,
    });

    return () => interceptorCleanup.current?.();
  }, []);

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
