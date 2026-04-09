import { useAppTheme } from '@/hooks/use-app-theme';
import { cn } from '@/lib/utils';
import { Platform, TextInput, type TextInputProps } from 'react-native';

function Input({ className, style, ...props }: TextInputProps & React.RefAttributes<TextInput>) {
  const { color } = useAppTheme();

  return (
    <TextInput
      className={cn(
        'dark:bg-input/30 border-input bg-background text-foreground flex h-12 w-full min-w-0 flex-row items-center rounded-md border px-3 py-1 text-lg leading-5 shadow-sm shadow-black/5 sm:h-9',
        props.editable === false && 'opacity-50',
        Platform.select({
          native: 'placeholder:text-muted-foreground/50',
        }),
        className
      )}
      style={[{ color: color.text, borderColor: color.input, backgroundColor: color.background }, style]}
      {...props}
    />
  );
}

export { Input };
