import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import Sentry from '@/lib/logger';
import AsyncStorage from '@react-native-async-storage/async-storage';

const APP_SETTINGS_KEY = 'koinonia_daily_app_settings_key';

const APP_DEFAULT_SETTINGS = {
  TEACHING_LIBRARY_LAYOUT: 'list' as 'list' | 'grid',
};

export type APP_SETTINGS_TYPE = typeof APP_DEFAULT_SETTINGS;

interface SettingsState {
  settings: APP_SETTINGS_TYPE;
  setSetting: <K extends keyof APP_SETTINGS_TYPE>(key: K, value: APP_SETTINGS_TYPE[K]) => void;
  reset: () => void;
}

export const useSettingsStore = create<SettingsState>()(
  persist(
    (set) => ({
      settings: APP_DEFAULT_SETTINGS,
      setSetting: (key, value) => {
        try {
          set((state) => ({
            settings: { ...state.settings, [key]: value },
          }));
        } catch (e) {
          Sentry.captureException(e);
        }
      },
      reset: () => set({ settings: APP_DEFAULT_SETTINGS }),
    }),
    {
      name: APP_SETTINGS_KEY,
      storage: createJSONStorage(() => AsyncStorage),
      partialize: (state) => ({ settings: state.settings }),
      onRehydrateStorage: () => (_state, error) => {
        if (error) {
          Sentry.captureException(error);
        }
      },
    }
  )
);
