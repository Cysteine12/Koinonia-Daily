import { AxiosInstance } from 'axios';

type AuthHandlers = {
  getAccessToken: () => string | null;
  refreshToken: () => Promise<string>;
  onLogout: () => Promise<void>;
};

let isRefreshing = false;
let refreshSubscribers: ((token: string) => void)[] = [];

const subscribeTokenRefresh = (cb: (token: string) => void) => {
  refreshSubscribers.push(cb);
};

const notifySubscribers = (token: string) => {
  refreshSubscribers.forEach((cb) => cb(token));
  refreshSubscribers = [];
};

export const attachAuthInterceptors = (client: AxiosInstance, handlers: AuthHandlers) => {
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

      const refreshToken = await handlers.refreshToken();

      if (error.response?.status === 401 && refreshToken && !originalRequest._retry) {
        originalRequest._retry = true;

        if (isRefreshing) {
          return new Promise((resolve) => {
            subscribeTokenRefresh((token) => {
              originalRequest.headers.Authorization = `Bearer ${token}`;
              resolve(client(originalRequest));
            });
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
