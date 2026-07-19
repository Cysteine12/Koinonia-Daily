import { useThemeColor } from '@/hooks/use-theme-color';
import { type ReactNode } from 'react';
import type { StyleProp, ViewStyle } from 'react-native';
import { KeyboardAvoidingView, Platform, ScrollView, View } from 'react-native';
import Animated, { useAnimatedRef } from 'react-native-reanimated';
import { SafeAreaView, type SafeAreaViewProps } from 'react-native-safe-area-context';
import ScrollJumpButton from '../ui/scroll-jump-button';

interface ScreenProps {
  children: ReactNode;
  scrollable?: boolean;
  keyboard?: boolean;
  keyboardBehavior?: KeyboardAvoidingView['props']['behavior'];
  keyboardDismissMode?: 'none' | 'interactive' | 'on-drag';
  keyboardShouldPersistTaps?: boolean | 'handled' | 'always' | 'never';
  edges?: SafeAreaViewProps['edges'];
  style?: StyleProp<ViewStyle>;
  contentContainerStyle?: ViewStyle;
  className?: string;
  contentContainerClassName?: string;
  secondaryBackground?: boolean;
  stickyHeaderIndices?: number[];
  showJumpButton?: boolean;
}

export function Screen({
  children,
  scrollable = false,
  keyboard = false,
  keyboardBehavior,
  keyboardDismissMode,
  keyboardShouldPersistTaps,
  edges = ['top', 'bottom'],
  style,
  contentContainerStyle,
  className,
  contentContainerClassName,
  secondaryBackground,
  stickyHeaderIndices,
  showJumpButton = false,
}: ScreenProps) {
  const scrollRef = useAnimatedRef<Animated.ScrollView>();

  const backgroundColor = useThemeColor({}, secondaryBackground ? 'secondaryBackground' : 'containerBackground');
  const Container = scrollable ? ScrollView : View;

  const content = (
    <SafeAreaView className={className} style={[{ flex: 1, backgroundColor }, style]} edges={edges}>
      <Container
        ref={scrollable ? scrollRef : undefined}
        style={[{ height: '100%' }, !scrollable && { flex: 1 }]}
        contentContainerClassName={scrollable ? contentContainerClassName : undefined}
        contentContainerStyle={scrollable ? [{ flexGrow: 1 }, contentContainerStyle] : undefined}
        stickyHeaderIndices={scrollable ? stickyHeaderIndices : undefined}
        showsVerticalScrollIndicator={false}
        keyboardDismissMode={scrollable ? keyboardDismissMode : undefined}
        keyboardShouldPersistTaps={scrollable ? keyboardShouldPersistTaps : undefined}
      >
        {children}
        {scrollable && showJumpButton && <ScrollJumpButton scrollRef={scrollRef} />}
      </Container>
    </SafeAreaView>
  );

  if (keyboard) {
    return (
      <KeyboardAvoidingView style={{ flex: 1 }} behavior={keyboardBehavior ?? (Platform.OS === 'ios' ? 'padding' : undefined)}>
        {content}
      </KeyboardAvoidingView>
    );
  }

  return content;
}
