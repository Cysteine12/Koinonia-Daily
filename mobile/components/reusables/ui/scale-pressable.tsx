import React, { useRef, type ReactNode } from 'react';
import { Animated, Pressable } from 'react-native';

const ScalePressable = ({
  onPressIn: _,
  onPressOut: __,
  style,
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

  return (
    <Pressable onPressIn={animateIn} onPressOut={animateOut} style={style} {...props}>
      <Animated.View style={{ transform: [{ scale }] }}>{children as ReactNode}</Animated.View>
    </Pressable>
  );
};

export default ScalePressable;
