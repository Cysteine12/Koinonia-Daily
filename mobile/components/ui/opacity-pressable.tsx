import React, { useRef } from 'react';
import { Animated, Pressable, type PressableProps, type StyleProp, type ViewStyle } from 'react-native';

interface OpacityPressableProps extends PressableProps {
  children: React.ReactNode;
  style?: StyleProp<ViewStyle>;
  /** Scale down to this value on press. Default: 0.98 */
  activeScale?: number;
  /** Opacity on press. Default: 0.2 */
  activeOpacity?: number;
  /** Animation duration in ms. Default: 150 */
  duration?: number;
}

/**
 * A wrapper on pressable for making views respond properly to touches.
 * On press down, the opacity and scale of the wrapped view is decreased, dimming and reducing it.
 * This is done without actually changing the view hierarchy,
 * and in general is easy to add to an app without weird side-effects.
 *
 */
export default function OpacityPressable({
  children,
  className,
  style,
  activeScale = 0.98,
  activeOpacity = 0.2,
  duration = 150,
  onPressIn,
  onPressOut,
  ...rest
}: OpacityPressableProps) {
  const scale = useRef(new Animated.Value(1)).current;
  const opacity = useRef(new Animated.Value(1)).current;

  const animateIn = () => {
    Animated.parallel([
      Animated.timing(scale, {
        toValue: activeScale,
        duration,
        useNativeDriver: true,
      }),
      Animated.timing(opacity, {
        toValue: activeOpacity,
        duration,
        useNativeDriver: true,
      }),
    ]).start();
  };

  const animateOut = () => {
    Animated.parallel([
      Animated.timing(scale, {
        toValue: 1,
        duration,
        useNativeDriver: true,
      }),
      Animated.timing(opacity, {
        toValue: 1,
        duration,
        useNativeDriver: true,
      }),
    ]).start();
  };

  return (
    <Pressable
      onPressIn={(e) => {
        animateIn();
        onPressIn?.(e);
      }}
      onPressOut={(e) => {
        animateOut();
        onPressOut?.(e);
      }}
      {...rest}
    >
      <Animated.View className={className} style={[style, { transform: [{ scale }], opacity }]}>
        {children}
      </Animated.View>
    </Pressable>
  );
}
