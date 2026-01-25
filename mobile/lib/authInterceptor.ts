import { AxiosInstance } from 'axios';

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
    refreshSubscribers.forEach(({ resolve }) => resolve(token));
    refreshSubscribers = [];
  };

  const rejectSubscribers = (err: unknown) => {
    refreshSubscribers.forEach(({ reject }) => reject(err));
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
    async (error) => {
      const originalRequest = error.config;

      if (error.response?.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;

        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            subscribeTokenRefresh((token) => {
              originalRequest.headers.Authorization = `Bearer ${token}`;
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
          await handlers.onLogout();
          isRefreshing = false;
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
