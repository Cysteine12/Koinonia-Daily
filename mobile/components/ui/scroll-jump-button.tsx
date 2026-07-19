import { Icon } from '@/components/core';
import { FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { type FlatList, type ScrollView } from 'react-native';
import Animated, { useAnimatedStyle, useScrollOffset, withTiming, type AnimatedRef } from 'react-native-reanimated';
import OpacityPressable from './opacity-pressable';

interface ScrollJumpButtonProps {
  scrollRef: AnimatedRef<Animated.ScrollView> | AnimatedRef<Animated.FlatList<any>>;
}

export default function ScrollJumpButton({ scrollRef }: ScrollJumpButtonProps) {
  const { color } = useAppTheme();
  const scrollHandler = useScrollOffset(scrollRef as any);
  const buttonStyle = useAnimatedStyle(() => {
    return {
      opacity: scrollHandler.value > 800 ? withTiming(1) : withTiming(0),
    };
  });

  const scrollToTop = () => {
    const current = scrollRef.current;
    if (current) {
      if ('scrollToOffset' in current && typeof (current as any).scrollToOffset === 'function') {
        (current as any).scrollToOffset({ offset: 0, animated: true });
      } else if ('scrollTo' in current && typeof (current as any).scrollTo === 'function') {
        (current as any).scrollTo({ y: 0, animated: true });
      }
    }
  };

  return (
    <Animated.View style={[buttonStyle, { position: 'absolute', bottom: 30, right: 20 }]}>
      <OpacityPressable
        onPress={scrollToTop}
        className="rounded-full p-2"
        style={{ backgroundColor: color.goldBorder, borderColor: color.border }}
      >
        <Icon name="chevron.up" size={FontSize.xl} color={color.background} />
      </OpacityPressable>
    </Animated.View>
  );
}
