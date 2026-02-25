import { cn } from '@/lib/utils';
import * as LabelPrimitive from '@rn-primitives/label';
import { Platform } from 'react-native';

/**
 * Render a label that wraps text and forwards interaction handlers and disabled state to the root.
 *
 * The component composes class names for root and text, forwards onPress, onLongPress, onPressIn, onPressOut, and disabled props, and applies reduced opacity when disabled.
 *
 * @returns A LabelPrimitive.Root containing a LabelPrimitive.Text with the provided props and forwarded interaction handlers
 */
function Label({
  className,
  onPress,
  onLongPress,
  onPressIn,
  onPressOut,
  disabled,
  ...props
}: LabelPrimitive.TextProps & React.RefAttributes<LabelPrimitive.TextRef>) {
  return (
    <LabelPrimitive.Root
      className={cn(
        'flex select-none flex-row items-center gap-2',
        Platform.select({
          web: 'cursor-default leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-50 group-data-[disabled=true]:pointer-events-none group-data-[disabled=true]:opacity-50',
        }),
        disabled && 'opacity-50'
      )}
      onPress={onPress}
      onLongPress={onLongPress}
      onPressIn={onPressIn}
      onPressOut={onPressOut}
      disabled={disabled}
    >
      <LabelPrimitive.Text
        className={cn('text-foreground text-sm font-medium', Platform.select({ web: 'leading-none' }), className)}
        {...props}
      />
    </LabelPrimitive.Root>
  );
}

export { Label };
