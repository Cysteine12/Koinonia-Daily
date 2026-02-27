import React, { useRef, type ReactNode } from 'react';
import { Animated, Pressable } from 'react-native';

const ScalePressable = ({
  onPressIn,
  onPressOut,
  children,
  ...props
}: React.ComponentProps<typeof Pressable> & React.RefAttributes<typeof Pressable>) => {
  const scale = useRef(new Animated.Value(1)).current;

  const animateIn = () => {
    Animated.spring(scale, {
      toValue: 0.9,
      useNativeDriver: true,
      speed: 20,
      bounciness: 0,
    }).start();
  };

  const animateOut = () => {
    Animated.spring(scale, {
      toValue: 1,
      useNativeDriver: true,
      speed: 20,
      bounciness: 4,
    }).start();
  };

  const handlePressIn: React.ComponentProps<typeof Pressable>['onPressIn'] = (event) => {
    animateIn();
    onPressIn?.(event);
  };

  const handlePressOut: React.ComponentProps<typeof Pressable>['onPressOut'] = (event) => {
    animateOut();
    onPressOut?.(event);
  };

  return (
    <Pressable onPressIn={handlePressIn} onPressOut={handlePressOut} {...props}>
      <Animated.View style={{ transform: [{ scale }] }}>{children as ReactNode}</Animated.View>
    </Pressable>
  );
};

export default ScalePressable;
