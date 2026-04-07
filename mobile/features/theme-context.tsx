import { useColorScheme as useRNColorScheme } from '@/hooks/use-color-scheme';
import logger from '@/lib/logger';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { DarkTheme, DefaultTheme, ThemeProvider as NavigationThemeProvider } from '@react-navigation/native';
import React, { createContext, useContext, useEffect, useState } from 'react';

type ThemeMode = 'auto' | 'light' | 'dark';

interface ThemeContextType {
  isReady: boolean;
  themeMode: ThemeMode;
  setThemeMode: (mode: ThemeMode) => void;
  resolvedTheme: 'light' | 'dark';
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

const THEME_STORAGE_KEY = 'koinonia_daily_theme_mode';

export const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const systemColorScheme = useRNColorScheme();
  const [themeMode, setThemeModeState] = useState<ThemeMode>('auto');
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    // Load persisted theme
    const loadTheme = async () => {
      try {
        const savedTheme = await AsyncStorage.getItem(THEME_STORAGE_KEY);
        if (savedTheme) {
          setThemeModeState(savedTheme as ThemeMode);
        }
      } catch (e) {
        console.error('Failed to load theme preference', e);
        logger.captureException(e);
      } finally {
        setIsReady(true);
      }
    };
    loadTheme();
  }, []);

  const setThemeMode = async (mode: ThemeMode) => {
    try {
      setThemeModeState(mode);
      await AsyncStorage.setItem(THEME_STORAGE_KEY, mode);
    } catch (e) {
      console.error('Failed to save theme preference', e);
      logger.captureException(e);
    }
  };

  const resolvedTheme = themeMode === 'auto' ? (systemColorScheme ?? 'dark') : themeMode;

  const navigationTheme = resolvedTheme === 'dark' ? DarkTheme : DefaultTheme;

  if (!isReady) return null;

  return (
    <ThemeContext.Provider value={{ isReady, themeMode, setThemeMode, resolvedTheme }}>
      <NavigationThemeProvider value={navigationTheme}>{children}</NavigationThemeProvider>
    </ThemeContext.Provider>
  );
};

export const useTheme = () => {
  const context = useContext(ThemeContext);

  if (context === undefined) {
    throw new Error('useTheme must be used within a ThemeProvider');
  }

  return context;
};
