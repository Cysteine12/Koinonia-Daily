import { GOLD_GRADIENT } from '@/constants/theme';
import { cn } from '@/lib/utils';
import { LinearGradient } from 'expo-linear-gradient';
import { cssInterop } from 'nativewind';
import type React from 'react';
import { useColorScheme } from 'react-native';

interface GoldGradientProps {
  children: React.ReactNode;
  className?: string;
  start?: { x: number; y: number };
  end?: { x: number; y: number };
}

cssInterop(LinearGradient, {
  className: { target: 'style' },
});

const GoldGradient = ({ children, className, start = { x: 0.2, y: 0 }, end = { x: 1, y: 1 } }: GoldGradientProps) => {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === 'dark';

  const colors = isDark ? GOLD_GRADIENT.dark : GOLD_GRADIENT.light;

  return (
    <LinearGradient colors={colors} className={cn('rounded-md', className)} start={start} end={end}>
      {children}
    </LinearGradient>
  );
};

export default GoldGradient;
