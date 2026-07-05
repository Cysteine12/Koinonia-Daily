import { create } from 'zustand';
import type { LoginSchema } from './schema';

type AuthState = {
  credentials: LoginSchema | null;
  setCredentials: (credentials: LoginSchema | null) => void;
};

export const useAuthStore = create<AuthState>()(
  (set) => ({
    credentials: null,
    setCredentials: (credentials: LoginSchema | null) => {
      set({ credentials });
    },
  })
);
