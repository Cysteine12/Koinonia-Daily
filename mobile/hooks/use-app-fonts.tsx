import type { FontFamily } from '@/constants';
import { Lora_400Regular, Lora_400Regular_Italic, Lora_500Medium } from '@expo-google-fonts/lora';
import { Outfit_300Light, Outfit_400Regular, Outfit_500Medium, Outfit_600SemiBold } from '@expo-google-fonts/outfit';
import { useFonts } from 'expo-font';

export default function useAppFonts() {
  const [fontsLoaded, fontError] = useFonts({
    Lora_500Medium,
    Outfit_300Light,
    Outfit_400Regular,
    Outfit_500Medium,
    Outfit_600SemiBold,
    Lora_400Regular,
    Lora_400Regular_Italic,
    'SpaceMono-Regular': require('@/assets/fonts/SpaceMono-Regular.ttf'),
  } satisfies Record<keyof typeof FontFamily, number>);

  return [fontsLoaded, fontError];
}
