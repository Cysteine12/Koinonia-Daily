import { GOLD_GRADIENT } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { cn } from '@/lib/utils';
import { LinearGradient } from 'expo-linear-gradient';
import { cssInterop } from 'nativewind';
import type React from 'react';

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
  const { isDark } = useAppTheme();

  const colors = isDark ? GOLD_GRADIENT.dark : GOLD_GRADIENT.light;

  return (
    <LinearGradient colors={colors} className={cn('rounded-md', className)} start={start} end={end}>
      {children}
    </LinearGradient>
  );
};

export default GoldGradient;
