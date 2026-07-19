import { useAppTheme } from '@/hooks/use-app-theme';
import { Ionicons } from '@expo/vector-icons';
import { useEffect, useRef, useState } from 'react';
import {
  Animated,
  Modal,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  useWindowDimensions,
  View,
} from 'react-native';

type BottomSheetProps = {
  visible: boolean;
  title?: string;
  onClose: () => void;
  children: React.ReactNode;
  footer?: React.ReactNode;
  maxHeight?: number;
};

const BottomSheet = ({ visible, title, onClose, children, footer, maxHeight = 0.6 }: BottomSheetProps) => {
  const { color } = useAppTheme();
  const { height: SCREEN_HEIGHT } = useWindowDimensions();
  const sheetHeightRatio = maxHeight > 0 && maxHeight <= 1 ? maxHeight : 0.6;
  const [shouldRender, setShouldRender] = useState(visible);
  const anim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    if (visible) {
      setShouldRender(true);
      Animated.timing(anim, {
        toValue: 1,
        duration: 300,
        useNativeDriver: true,
      }).start();
    } else {
      Animated.timing(anim, {
        toValue: 0,
        duration: 250,
        useNativeDriver: true,
      }).start(({ finished }) => {
        if (finished) setShouldRender(false);
      });
    }
  }, [visible, anim]);

  if (!shouldRender) return null;

  return (
    <Modal transparent visible={shouldRender} animationType="none" onRequestClose={onClose}>
      <View style={styles.container}>
        {/* Backdrop: Orchestrated Fade */}
        <Animated.View
          style={[
            styles.overlay,
            {
              opacity: anim.interpolate({
                inputRange: [0, 1],
                outputRange: [0, 1],
              }),
            },
          ]}
        >
          <Pressable style={{ flex: 1 }} onPress={onClose} aria-hidden={true} />
        </Animated.View>

        {/* Content Sheet: Orchestrated Slide */}
        <Animated.View
          style={[
            styles.content,
            {
              maxHeight: SCREEN_HEIGHT * sheetHeightRatio,
              backgroundColor: color.containerBackground,
              transform: [
                {
                  translateY: anim.interpolate({
                    inputRange: [0, 1],
                    outputRange: [SCREEN_HEIGHT * sheetHeightRatio, 0],
                  }),
                },
              ],
            },
          ]}
        >
          {/* HANDLE */}
          <View
            style={{
              width: 40,
              height: 4,
              backgroundColor: color.border,
              borderRadius: 2,
              alignSelf: 'center',
              marginBottom: 16,
            }}
          />
          {/* Header */}
          <View style={styles.header}>
            <Text style={{ color: color.text, fontSize: 20 }}>{title}</Text>
            <TouchableOpacity
              onPress={onClose}
              accessibilityRole="button"
              accessibilityLabel="Close Modal"
              style={[styles.closeButton, { backgroundColor: color.background }]}
            >
              <Ionicons name="close" size={20} color={color.text} />
            </TouchableOpacity>
          </View>

          {/* Options */}
          <ScrollView
            showsVerticalScrollIndicator={false}
            keyboardShouldPersistTaps="always"
            contentContainerStyle={[styles.contentContainer, { maxHeight: SCREEN_HEIGHT * sheetHeightRatio - 10 }]}
          >
            {children}
          </ScrollView>

          {/* Footer */}
          {footer && <View style={styles.footer}>{footer}</View>}
        </Animated.View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-end',
  },
  overlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(0,0,0,0.5)',
  },
  content: {
    borderTopLeftRadius: 32,
    borderTopRightRadius: 32,
    paddingHorizontal: 24,
    paddingTop: 10,
    paddingBottom: 40,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 24,
  },
  closeButton: {
    width: 36,
    height: 36,
    borderRadius: 18,
    justifyContent: 'center',
    alignItems: 'center',
  },
  contentContainer: {
    gap: 2,
  },
  footer: {
    paddingTop: 12,
  },
});

export default BottomSheet;
