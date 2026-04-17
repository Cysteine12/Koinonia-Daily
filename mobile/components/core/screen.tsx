import { useThemeColor } from '@/hooks/use-theme-color';
import { type ReactNode } from 'react';
import type { StyleProp, ViewStyle } from 'react-native';
import { KeyboardAvoidingView, Platform, ScrollView, View } from 'react-native';
import { SafeAreaView, type SafeAreaViewProps } from 'react-native-safe-area-context';

interface ScreenProps {
  children: ReactNode;
  scrollable?: boolean;
  keyboard?: boolean;
  keyboardDismissMode?: 'none' | 'interactive' | 'on-drag';
  keyboardShouldPersistTaps?: boolean | 'handled' | 'always' | 'never';
  edges?: SafeAreaViewProps['edges'];
  style?: StyleProp<ViewStyle>;
  contentContainerStyle?: ViewStyle;
  className?: string;
  contentContainerClassName?: string;
  secondaryBackground?: boolean;
  stickyHeaderIndices?: number[];
}

export function Screen({
  children,
  scrollable = false,
  keyboard = false,
  keyboardDismissMode,
  keyboardShouldPersistTaps,
  edges = ['top', 'bottom'],
  style,
  contentContainerStyle,
  className,
  contentContainerClassName,
  secondaryBackground,
  stickyHeaderIndices,
}: ScreenProps) {
  const backgroundColor = useThemeColor({}, secondaryBackground ? 'secondaryBackground' : 'containerBackground');
  const Container = scrollable ? ScrollView : View;

  const content = (
    <SafeAreaView className={className} style={[{ flex: 1, backgroundColor }, style]} edges={edges}>
      <Container
        style={[!scrollable && { flex: 1 }]}
        contentContainerClassName={scrollable ? contentContainerClassName : undefined}
        contentContainerStyle={scrollable ? [{ flexGrow: 1 }, contentContainerStyle] : undefined}
        stickyHeaderIndices={scrollable ? stickyHeaderIndices : undefined}
        showsVerticalScrollIndicator={false}
        keyboardDismissMode={scrollable ? keyboardDismissMode : undefined}
        keyboardShouldPersistTaps={scrollable ? keyboardShouldPersistTaps : undefined}
      >
        {children}
      </Container>
    </SafeAreaView>
  );

  if (keyboard) {
    return (
      <KeyboardAvoidingView style={{ flex: 1 }} behavior={Platform.OS === 'ios' ? 'padding' : 'height'}>
        {content}
      </KeyboardAvoidingView>
    );
  }

  return content;
}
