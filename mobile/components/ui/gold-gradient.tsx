import { cn } from '@/lib/utils';
import type { ClassValue } from 'clsx';
import { LinearGradient } from 'expo-linear-gradient';
import type React from 'react';
import { useColorScheme } from 'react-native';

interface GoldGradientProps {
  children: React.ReactNode;
  className?: ClassValue;
  start?: { x: number; y: number };
  end?: { x: number; y: number };
}

const GoldGradient = ({ children, className, start = { x: 0.2, y: 0 }, end = { x: 1, y: 1 } }: GoldGradientProps) => {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === 'dark';

  const colors = isDark
    ? (['hsl(48 80% 50%)', 'hsl(48 70% 60%)', 'hsl(45 71% 36%)'] as const)
    : (['hsl(48 72% 65%)', 'hsl(45 66% 52%)', 'hsl(45 60% 44%)'] as const);

  return (
    <LinearGradient
      colors={colors}
      style={{
        borderRadius: 8,
      }}
      className={cn('rounded-md', className)}
      start={start}
      end={end}
    >
      {children}
    </LinearGradient>
  );
};

export default GoldGradient;
