import { useAppTheme } from '@/hooks/use-app-theme';
import { cn } from '@/lib/utils';
import { useRouter } from 'expo-router';
import { TouchableOpacity } from 'react-native';
import { Icon } from '../core';

export default function BackButton({ className }: { className?: string }) {
  const { color } = useAppTheme();
  const router = useRouter();

  return (
    <TouchableOpacity
      onPress={() => router.back()}
      accessibilityRole="button"
      accessibilityLabel="Go back"
      className={cn('self-start p-2 rounded-full items-center justify-center bg-transparent border', className)}
      style={{ borderColor: color.border, backgroundColor: color.cardBackground }}
    >
      <Icon name="chevron.left" size={36} color={color.text} />
    </TouchableOpacity>
  );
}
