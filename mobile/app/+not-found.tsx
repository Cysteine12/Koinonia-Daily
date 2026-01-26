import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';

const NotFoundScreen = () => {
  return (
    <ThemedView className="flex-1 justify-center align-center">
      <ThemedText type="title">Page Not Found</ThemedText>
    </ThemedView>
  );
};
export default NotFoundScreen;
