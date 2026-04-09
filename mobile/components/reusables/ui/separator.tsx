import { useAppTheme } from '@/hooks/use-app-theme';
import { cn } from '@/lib/utils';
import * as SeparatorPrimitive from '@rn-primitives/separator';

function Separator({
  className,
  style,
  orientation = 'horizontal',
  decorative = true,
  ...props
}: SeparatorPrimitive.RootProps & React.RefAttributes<SeparatorPrimitive.RootRef>) {
  const { color } = useAppTheme();

  return (
    <SeparatorPrimitive.Root
      decorative={decorative}
      orientation={orientation}
      className={cn('bg-border shrink-0', orientation === 'horizontal' ? 'h-[1px] w-full' : 'h-full w-[1px]', className)}
      style={[{ backgroundColor: color.border }, style]}
      {...props}
    />
  );
}

export { Separator };
