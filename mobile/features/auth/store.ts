import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { LoginSchema } from './schema';

type AuthState = {
  credentials: any;
  setCredentials: (credentials: LoginSchema | null) => void;
};

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      credentials: null,
      setCredentials: (credentials: LoginSchema | null) => {
        set({ credentials });
      },
    }),
    {
      name: 'auth',
    }
  )
);
