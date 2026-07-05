import AsyncStorage from '@react-native-async-storage/async-storage';
import { create } from 'zustand';
import { createJSONStorage, persist } from 'zustand/middleware';
import type { LoginSchema } from './schema';

type AuthState = {
  credentials: LoginSchema | null;
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
      storage: createJSONStorage(() => AsyncStorage)
    }
  )
);
