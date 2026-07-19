import { FlashList as ShopifyFlashList, type FlashListProps } from '@shopify/flash-list';
import React, { forwardRef, useImperativeHandle } from 'react';
import Animated, { useAnimatedRef } from 'react-native-reanimated';
import ScrollJumpButton from '../ui/scroll-jump-button';
import { View } from './view';

const AnimatedFlashList = Animated.createAnimatedComponent(ShopifyFlashList) as any;

export interface CustomFlashListProps<T> extends FlashListProps<T> {
  showJumpButton?: boolean;
  estimatedItemSize?: number;
}

function FlashListInner<T>({ showJumpButton = true, ...props }: CustomFlashListProps<T>, ref: React.Ref<any>) {
  const scrollRef = useAnimatedRef<any>();

  useImperativeHandle(ref, () => scrollRef.current);

  return (
    <View style={{ flex: 1 }}>
      <AnimatedFlashList ref={scrollRef as any} {...props} />
      {showJumpButton && <ScrollJumpButton scrollRef={scrollRef} />}
    </View>
  );
}

export const FlashList = forwardRef(FlashListInner) as <T>(
  props: CustomFlashListProps<T> & { ref?: React.Ref<any> }
) => React.ReactElement;
