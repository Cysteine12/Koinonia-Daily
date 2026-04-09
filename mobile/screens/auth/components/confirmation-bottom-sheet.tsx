import { Icon } from '@/components/core';
import { Button } from '@/components/reusables/ui/button';
import { ThemedText } from '@/components/themed-text';
import BottomSheet from '@/components/ui/bottom-sheet';
import GoldGradient from '@/components/ui/gold-gradient';
import { Colors } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { ActivityIndicator, Text, View } from 'react-native';

type ConfirmationBottomSheetProps = {
  visible: boolean;
  onClose: () => void;
  title: string;
  isPending?: boolean;
};

export default function ConfirmationBottomSheet({ visible, onClose, title, isPending }: ConfirmationBottomSheetProps) {
  const { color } = useAppTheme();

  return (
    <BottomSheet visible={visible} onClose={() => !isPending && onClose()}>
      <GoldGradient className="w-16 h-16 rounded-full items-center justify-center self-center mb-8">
        <Icon name="check" size={40} color={color.background} />
      </GoldGradient>

      <ThemedText className="text-center text-2xl">{title}</ThemedText>

      <Button
        onPress={onClose}
        disabled={isPending}
        className="mt-8 self-center w-full border"
        style={{ borderColor: color.goldBorder, backgroundColor: 'transparent' }}
      >
        {isPending ? (
          <ActivityIndicator color={Colors.goldIcon} />
        ) : (
          <View className="flex-row items-center justify-center gap-1">
            <Text className="font-semibold" style={{ color: color.goldText }}>
              Continue
            </Text>
            <Icon name="arrow.forward" size={16} color={Colors.goldIcon} />
          </View>
        )}
      </Button>
    </BottomSheet>
  );
}
