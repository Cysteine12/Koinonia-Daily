import { useSettingsStore } from './store';

export const useAppSettings = () => {
  const settings = useSettingsStore((s) => s.settings);
  const setSetting = useSettingsStore((s) => s.setSetting);
  const reset = useSettingsStore((s) => s.reset);

  return { settings, setSetting, reset };
};
