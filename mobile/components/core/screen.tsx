import { useAppTheme } from '@/hooks/use-app-theme';
import React, { type ReactNode } from 'react';
import type { ViewStyle } from 'react-native';
import { KeyboardAvoidingView, Platform, ScrollView, View } from 'react-native';
import { SafeAreaView, type SafeAreaViewProps } from 'react-native-safe-area-context';

interface ScreenProps {
  children: ReactNode;
  scrollable?: boolean;
  keyboard?: boolean;
  style?: '';
  contentContainerStyle?: ViewStyle;
  contentContainerClassName?: string;
  keyboardDismissMode?: 'none' | 'interactive' | 'on-drag';
  keyboardShouldPersistTaps?: boolean | 'handled' | 'always' | 'never';
  edges?: SafeAreaViewProps['edges'];
}

export function Screen({
  children,
  scrollable = false,
  keyboard = false,
  style,
  contentContainerStyle,
  contentContainerClassName,
  keyboardDismissMode,
  keyboardShouldPersistTaps,
  edges = ['top', 'bottom'],
}: ScreenProps) {
  const { color } = useAppTheme();
  const Container = scrollable ? ScrollView : View;

  const content = (
    <SafeAreaView style={[{ flex: 1 }, style]} edges={edges}>
      <Container
        style={[{ backgroundColor: color.containerBackground }, !scrollable && { flex: 1 }]}
        contentContainerStyle={scrollable ? [{ flexGrow: 1 }, contentContainerStyle] : undefined}
        contentContainerClassName={scrollable ? contentContainerClassName : undefined}
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
