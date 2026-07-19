import Sentry from '@/lib/logger';
import { deleteStorage, getStorage, saveStorage } from '@/lib/storage';
import { createContext, useContext, useEffect, useState, type ReactNode } from 'react';

const APP_SETTINGS_KEY = 'koinonia_daily_app_settings_key';

const APP_DEFAULT_SETTINGS = {
  TEACHING_LIBRARY_LAYOUT: 'list' as 'list' | 'grid',
};

export type APP_SETTINGS_TYPE = typeof APP_DEFAULT_SETTINGS;

interface SettingsContextType {
  settings: APP_SETTINGS_TYPE;
  setSetting: (key: keyof typeof APP_DEFAULT_SETTINGS, value: APP_SETTINGS_TYPE[keyof APP_SETTINGS_TYPE]) => void;
  handleReset: () => void;
}

const SettingsContext = createContext<SettingsContextType | undefined>(undefined);

export const SettingsProvider = ({ children }: { children: ReactNode }) => {
  const [settings, setSettings] = useState(APP_DEFAULT_SETTINGS);

  useEffect(() => {
    const loadSettings = async () => {
      try {
        const storedSettings = await getStorage(APP_SETTINGS_KEY);

        if (!storedSettings) return;

        setSettings(JSON.parse(storedSettings) as typeof settings);
      } catch (e) {
        console.error('Failed to load settings preference', e);
        Sentry.captureException(e);
      }
    };
    loadSettings();
  }, []);

  const setSetting = async (key: keyof typeof settings, value: any) => {
    try {
      setSettings((settings) => ({
        ...settings,
        [key]: value,
      }));

      const newSettings = { ...settings, [key]: value };
      await saveStorage(APP_SETTINGS_KEY, JSON.stringify(newSettings));
    } catch (e) {
      console.error('Failed to save settings preference', e);
      Sentry.captureException(e);
    }
  };

  const handleReset = () => {
    setSettings(APP_DEFAULT_SETTINGS);
    deleteStorage(APP_SETTINGS_KEY);
  };

  if (!settings) return null;

  return <SettingsContext.Provider value={{ settings, setSetting, handleReset }}>{children}</SettingsContext.Provider>;
};

export const useAppSettings = () => {
  const context = useContext(SettingsContext);

  if (context === undefined) {
    throw new Error('useAppSettings must be used within a SettingsProvider');
  }

  return context;
};
