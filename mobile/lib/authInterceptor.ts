import { AxiosError, type AxiosInstance, type InternalAxiosRequestConfig } from 'axios';
import type { ErrorResponse } from './types';

type AuthHandlers = {
  getAccessToken: () => string | null;
  refreshToken: () => Promise<string>;
  onLogout: () => Promise<void>;
};

export const attachAuthInterceptors = (client: AxiosInstance, handlers: AuthHandlers) => {
  let isRefreshing = false;
  let refreshSubscribers: { resolve: (token: string) => void; reject: (err: unknown) => void }[] = [];

  const subscribeTokenRefresh = (resolve: (token: string) => void, reject: (err: unknown) => void) => {
    refreshSubscribers.push({ resolve, reject });
  };

  const notifySubscribers = (token: string) => {
    for (const { resolve } of refreshSubscribers) {
      resolve(token);
    }
    refreshSubscribers = [];
  };

  const rejectSubscribers = (err: unknown) => {
    for (const { reject } of refreshSubscribers) {
      reject(err);
    }
    refreshSubscribers = [];
  };

  const reqInterceptor = client.interceptors.request.use((config) => {
    const accessToken = handlers.getAccessToken();
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  });

  const resInterceptor = client.interceptors.response.use(
    (response) => response,
    async (error: AxiosError<ErrorResponse>) => {
      const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

      // Log only during development
      if (__DEV__) {
        console.log('API Error:', error.response?.data || error.message);
      }

      if (
        error.response?.status === 401 &&
        error.response?.data?.code === 'ExpiredJwtException' &&
        !originalRequest._retry
      ) {
        originalRequest._retry = true;

        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            subscribeTokenRefresh((newAccessToken) => {
              originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
              resolve(client(originalRequest));
            }, reject);
          });
        }
        isRefreshing = true;

        try {
          const newAccessToken = await handlers.refreshToken();
          isRefreshing = false;

          notifySubscribers(newAccessToken);

          originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
          return client(originalRequest);
        } catch (err) {
          rejectSubscribers(err);
          isRefreshing = false;
          await handlers.onLogout();
          return Promise.reject(err);
        }
      }
      return Promise.reject(error);
    }
  );

  return () => {
    client.interceptors.request.eject(reqInterceptor);
    client.interceptors.response.eject(resInterceptor);
  };
};
