import { cn } from '@/lib/utils';
import * as SeparatorPrimitive from '@rn-primitives/separator';

/**
 * Render a styled separator line that adapts layout based on orientation.
 *
 * Renders a SeparatorPrimitive.Root with a computed className and forwarded props.
 *
 * @param orientation - 'horizontal' to render a full-width horizontal line, 'vertical' to render a full-height vertical line
 * @param decorative - When true, marks the separator as decorative for accessibility (no semantic role)
 * @returns The configured SeparatorPrimitive.Root element
 */
function Separator({
  className,
  orientation = 'horizontal',
  decorative = true,
  ...props
}: SeparatorPrimitive.RootProps & React.RefAttributes<SeparatorPrimitive.RootRef>) {
  return (
    <SeparatorPrimitive.Root
      decorative={decorative}
      orientation={orientation}
      className={cn(
        'bg-border shrink-0',
        orientation === 'horizontal' ? 'h-[1px] w-full' : 'h-full w-[1px]',
        className
      )}
      {...props}
    />
  );
}

export { Separator };
