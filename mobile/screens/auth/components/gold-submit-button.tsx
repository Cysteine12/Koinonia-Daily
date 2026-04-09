import { Button } from '@/components/reusables/ui/button';
import GoldGradient from '@/components/ui/gold-gradient';
import { Colors, FontSize } from '@/constants';
import React from 'react';
import { ActivityIndicator, Text } from 'react-native';

interface GoldSubmitButtonProp {
  onPress: () => void;
  isPending: boolean;
  title: string;
  disabled?: boolean;
}

export default function GoldSubmitButton({ onPress, isPending, title, disabled }: GoldSubmitButtonProp) {
  return (
    <GoldGradient>
      <Button
        className="bg-transparent w-full font-semibold"
        onPress={onPress}
        disabled={disabled !== undefined ? disabled : isPending}
      >
        {isPending ? (
          <ActivityIndicator color={Colors.light.text} size={FontSize.md} />
        ) : (
          <Text className="font-semibold" style={{ color: '#000', fontSize: FontSize.base + 1 }}>
            {title}
          </Text>
        )}
      </Button>
    </GoldGradient>
  );
}
