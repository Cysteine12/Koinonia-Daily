import { Text, TextClassContext } from '@/components/reusables/ui/text';
import { cn } from '@/lib/utils';
import { View, type ViewProps } from 'react-native';

/**
 * Card container that applies card background, border, spacing, rounded corners, and shadow while providing a text-foreground context for its children.
 *
 * @param className - Additional class names to merge with the component's default card classes.
 * @param props - Additional View props forwarded to the underlying View.
 * @returns The rendered card element with card styling and text foreground context applied.
 */
function Card({ className, ...props }: ViewProps & React.RefAttributes<View>) {
  return (
    <TextClassContext.Provider value="text-card-foreground">
      <View
        className={cn(
          'bg-card border-border flex flex-col gap-6 rounded-xl border py-6 shadow-sm shadow-black/5',
          className
        )}
        {...props}
      />
    </TextClassContext.Provider>
  );
}

/**
 * Header section container for a Card component.
 *
 * Applies the card header's default layout and spacing classes and forwards remaining props to the underlying View.
 *
 * @param className - Additional class names appended to the header's default classes
 * @returns The rendered View element for the card header
 */
function CardHeader({ className, ...props }: ViewProps & React.RefAttributes<View>) {
  return <View className={cn('flex flex-col gap-1.5 px-6', className)} {...props} />;
}

/**
 * Renders a card title with heading semantics and title styling.
 *
 * @returns A Text element with role="heading", aria-level=3, and font weight/line-height styling; accepts and applies an optional `className`.
 */
function CardTitle({ className, ...props }: React.ComponentProps<typeof Text> & React.RefAttributes<Text>) {
  return <Text role="heading" aria-level={3} className={cn('font-semibold leading-none', className)} {...props} />;
}

/**
 * Renders a card description text element with muted, small styling.
 *
 * @param className - Additional class names appended to the default muted small text styles
 * @returns A Text element styled for card descriptions
 */
function CardDescription({ className, ...props }: React.ComponentProps<typeof Text> & React.RefAttributes<Text>) {
  return <Text className={cn('text-muted-foreground text-sm', className)} {...props} />;
}

/**
 * Container for a card's main content that applies horizontal padding.
 *
 * @returns A View element with horizontal padding (`px-6`) and any additional `className` merged in.
 */
function CardContent({ className, ...props }: ViewProps & React.RefAttributes<View>) {
  return <View className={cn('px-6', className)} {...props} />;
}

/**
 * Footer container for a Card that arranges children horizontally and centers them.
 *
 * @param className - Additional class names to apply to the footer container
 * @returns A React Native View element rendered as the card footer
 */
function CardFooter({ className, ...props }: ViewProps & React.RefAttributes<View>) {
  return <View className={cn('flex flex-row items-center px-6', className)} {...props} />;
}

export { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle };
