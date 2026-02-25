import { Button } from '@/components/reusables/ui/button';
import { cn } from '@/lib/utils';
import { useColorScheme } from 'nativewind';
import { Image, Platform, View } from 'react-native';

const SOCIAL_CONNECTION_STRATEGIES = [
  {
    type: 'oauth_google',
    label: 'Continue with Google',
    source: { uri: 'https://img.clerk.com/static/google.png?width=160' },
    useTint: false,
  },
];

/**
 * Renders social sign-in buttons for the configured strategies and delegates selection to the provided handler.
 *
 * @param handleSocialSignIn - Called with the strategy `type` when a social button is pressed.
 * @returns A View containing one button per social strategy; each button displays the provider icon and invokes `handleSocialSignIn` with that strategy's `type` when pressed.
 */
export function SocialConnections({ handleSocialSignIn }: { handleSocialSignIn: (type: string) => void }) {
  const { colorScheme } = useColorScheme();

  return (
    <View className="gap-2 sm:flex-row sm:gap-3">
      {SOCIAL_CONNECTION_STRATEGIES.map((strategy) => {
        return (
          <Button
            key={strategy.type}
            variant="outline"
            size="sm"
            className="sm:flex-1"
            accessibilityLabel={strategy.label}
            onPress={() => handleSocialSignIn(strategy.type)}
          >
            <Image
              className={cn('size-4', strategy.useTint && Platform.select({ web: 'dark:invert' }))}
              tintColor={Platform.select({
                native: strategy.useTint ? (colorScheme === 'dark' ? 'white' : 'black') : undefined,
              })}
              source={strategy.source}
            />
          </Button>
        );
      })}
    </View>
  );
}
