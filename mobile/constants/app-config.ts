/**
 * Application behavior configuration
 *
 * Splash screen timings, API settings, and other non-UI constants
 *
 * Note: For visual constants (spacing, colors), use theme/ files
 */
export const AppConfig = {
  // Loading behavior
  LOADING: {
    /**
     * Minimum duration splash screen must remain visible (ms)
     * Prevents flicker during fast loads
     */
    SPLASH_MIN_DISPLAY_MS: __DEV__ ? 0 : 200, // Skip in dev for faster reloads

    /**
     * Additional buffer time for font loading (ms)
     * Safety margin to ensure fonts are fully ready
     */
    FONT_LOADING_BUFFER_MS: 100,
  },

  // Animation durations
  ANIMATION: {
    SHORT: 150,
    MEDIUM: 300,
  },
} as const;

// Type exports for autocomplete
export type AppConfigKey = keyof typeof AppConfig;
