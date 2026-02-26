import { Button } from '@/components/reusables/ui/button';
import { Text } from '@/components/reusables/ui/text';
import { ThemedView } from '@/components/themed-view';
import { useAuth } from '@/features/auth/auth-context';
import { useLogout } from '@/features/auth/hook';
import { TokenType } from '@/features/auth/types';
import { getSecure } from '@/lib/storage';

const Profile = () => {
  const { mutate: logout } = useLogout();
  const { logout: authLogout } = useAuth();

  const handleLogout = async () => {
    const refreshToken = await getSecure(TokenType.REFRESH_TOKEN);

    if (!refreshToken) {
      return authLogout();
    }
    logout({ refreshToken });
  };

  return (
    <ThemedView className="mt-20">
      <Button onPress={handleLogout}>
        <Text>Logout</Text>
      </Button>
    </ThemedView>
  );
};

export default Profile;
