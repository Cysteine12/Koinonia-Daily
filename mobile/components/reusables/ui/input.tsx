import { cn } from '@/lib/utils';
import { Platform, TextInput, type TextInputProps } from 'react-native';

/**
 * A styled TextInput that applies shared base styles, augments styling for non-editable state, and adds platform-specific placeholder and focus classes.
 *
 * @param className - Additional class names to append to the computed input classes
 * @param props - All other TextInput props (and ref attributes) which are passed through to the underlying TextInput
 * @returns The rendered TextInput element with computed, platform-aware classes
 */
function Input({ className, ...props }: TextInputProps & React.RefAttributes<TextInput>) {
  return (
    <TextInput
      className={cn(
        'dark:bg-input/30 border-input bg-background text-foreground flex h-10 w-full min-w-0 flex-row items-center rounded-md border px-3 py-1 text-base leading-5 shadow-sm shadow-black/5 sm:h-9',
        props.editable === false &&
          cn('opacity-50', Platform.select({ web: 'disabled:pointer-events-none disabled:cursor-not-allowed' })),
        Platform.select({
          web: cn(
            'placeholder:text-muted-foreground selection:bg-primary selection:text-primary-foreground outline-none transition-[color,box-shadow] md:text-sm',
            'focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px]',
            'aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive'
          ),
          native: 'placeholder:text-muted-foreground/50',
        }),
        className
      )}
      {...props}
    />
  );
}

export { Input };
