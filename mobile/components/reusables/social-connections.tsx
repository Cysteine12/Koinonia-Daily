import { Button } from '@/components/reusables/ui/button';
import { useAppTheme } from '@/hooks/use-app-theme';
import { cn } from '@/lib/utils';
import { Image, Platform, View } from 'react-native';

const SOCIAL_CONNECTION_STRATEGIES = [
  {
    type: 'oauth_google',
    label: 'Continue with Google',
    source: require('@/assets/images/google.png'),
    useTint: false,
  },
];

export function SocialConnections({ handleSocialSignIn }: { handleSocialSignIn: (type: string) => void }) {
  const { theme, color } = useAppTheme();

  return (
    <View className="gap-2 sm:flex-row sm:gap-3">
      {SOCIAL_CONNECTION_STRATEGIES.map((strategy) => {
        return (
          <Button
            key={strategy.type}
            variant="outline"
            className="sm:flex-1"
            accessibilityLabel={strategy.label}
            onPress={() => handleSocialSignIn(strategy.type)}
            style={{ backgroundColor: color.background, borderColor: color.border }}
          >
            <Image
              className={cn('size-4', strategy.useTint && Platform.select({ web: 'dark:invert' }))}
              tintColor={Platform.select({
                native: strategy.useTint ? (theme === 'dark' ? 'white' : 'black') : undefined,
              })}
              source={strategy.source}
            />
          </Button>
        );
      })}
    </View>
  );
}
