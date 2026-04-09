import { Text, View } from '@/components/core';
import { Ionicons } from '@expo/vector-icons';
import React, { useEffect } from 'react';
import { StyleSheet, type ViewStyle } from 'react-native';
import Animated, { FadeInUp, FadeOutUp } from 'react-native-reanimated';

export type SnackbarVariant = 'info' | 'success' | 'warning' | 'error';

type SnackbarProps = {
  title?: string;
  message: string;
  variant?: SnackbarVariant;
  visible: boolean;
  onHide: () => void;
  style?: ViewStyle;
  autoHide?: boolean;
  duration?: number;
};

export default function Snackbar({
  title,
  message,
  variant = 'info',
  visible,
  onHide,
  style,
  autoHide = true,
  duration = 3000,
}: SnackbarProps) {
  useEffect(() => {
    if (visible && autoHide) {
      const timer = setTimeout(() => {
        onHide();
      }, duration);
      return () => clearTimeout(timer);
    }
  }, [visible, autoHide, duration, onHide]);

  if (!visible) return null;

  const getVariantStyles = () => {
    switch (variant) {
      case 'success':
        return {
          backgroundColor: '#F0FDF4',
          iconColor: '#16B364',
          icon: 'checkmark-circle' as const,
          titleColor: '#166534',
          textColor: '#15803D',
          defaultTitle: 'Success',
        };
      case 'error':
        return {
          backgroundColor: '#FDF2F2',
          iconColor: '#D92D20',
          icon: 'close-circle' as const,
          titleColor: '#991B1B',
          textColor: '#B91C1C',
          defaultTitle: 'Error',
        };
      case 'warning':
        return {
          backgroundColor: '#FFFAEB',
          iconColor: '#F79009',
          icon: 'alert-circle' as const,
          titleColor: '#92400E',
          textColor: '#B45309',
          defaultTitle: 'Warning',
        };
      default:
        return {
          backgroundColor: '#F2F4F7',
          iconColor: '#667085',
          icon: 'information-circle' as const,
          titleColor: '#101828',
          textColor: '#344054',
          defaultTitle: 'Info',
        };
    }
  };

  const { backgroundColor, iconColor, icon, titleColor, textColor, defaultTitle } = getVariantStyles();

  return (
    <Animated.View
      entering={FadeInUp.duration(400)}
      exiting={FadeOutUp.duration(300)}
      style={[
        styles.container,
        {
          backgroundColor,
          borderColor: iconColor + '20',
        },
        style,
      ]}
    >
      <View style={styles.content}>
        <Ionicons name={icon} size={24} color={iconColor} style={styles.icon} />
        <View style={styles.textContainer}>
          <Text style={[styles.title, { color: titleColor }]}>{title || defaultTitle}</Text>
          <Text style={[styles.message, { color: textColor }]}>{message}</Text>
        </View>
      </View>
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    left: 20,
    right: 20,
    padding: 16,
    borderRadius: 16,
    borderWidth: 1,
    zIndex: 9999,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 12,
    elevation: 5,
  },
  content: {
    flexDirection: 'row',
    alignItems: 'flex-start',
  },
  icon: {
    marginRight: 12,
    marginTop: 2,
  },
  textContainer: {
    flex: 1,
  },
  title: {
    fontSize: 15,
    fontWeight: '700',
    marginBottom: 2,
  },
  message: {
    fontSize: 14,
    lineHeight: 20,
    fontWeight: '500',
  },
});
