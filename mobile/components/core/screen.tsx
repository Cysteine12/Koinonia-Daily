import { useThemeColor } from '@/hooks/use-theme-color';
import React, { type ReactNode } from 'react';
import type { StyleProp, ViewStyle } from 'react-native';
import { KeyboardAvoidingView, Platform, ScrollView, View } from 'react-native';
import { SafeAreaView, type SafeAreaViewProps } from 'react-native-safe-area-context';

interface ScreenProps {
  children: ReactNode;
  scrollable?: boolean;
  keyboard?: boolean;
  style?: StyleProp<ViewStyle>;
  contentContainerStyle?: ViewStyle;
  contentContainerClassName?: string;
  keyboardDismissMode?: 'none' | 'interactive' | 'on-drag';
  keyboardShouldPersistTaps?: boolean | 'handled' | 'always' | 'never';
  edges?: SafeAreaViewProps['edges'];
  secondaryBackground?: boolean;
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
  secondaryBackground,
}: ScreenProps) {
  const backgroundColor = useThemeColor({}, secondaryBackground ? 'secondaryBackground' : 'containerBackground');
  const Container = scrollable ? ScrollView : View;

  const content = (
    <SafeAreaView style={[{ flex: 1, backgroundColor }, style]} edges={edges}>
      <Container
        style={[!scrollable && { flex: 1 }]}
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
