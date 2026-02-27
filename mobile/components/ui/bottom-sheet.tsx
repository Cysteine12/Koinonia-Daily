import { Colors } from '@/constants/theme';
import type { ReactNode } from 'react';
import { Modal, Pressable, ScrollView, Text, useColorScheme, useWindowDimensions, View } from 'react-native';

interface BottomSheetProps {
  children: ReactNode;
  isOpen: boolean;
  onClose?: () => void;
  title?: string;
}

const BottomSheet = ({ children, isOpen, onClose, title }: BottomSheetProps) => {
  const colorScheme = useColorScheme();
  const { height: SCREEN_HEIGHT } = useWindowDimensions();

  return (
    <Modal transparent visible={isOpen} animationType="slide" onRequestClose={onClose}>
      <View style={{ flex: 1, justifyContent: 'flex-end' }}>
        {/* Backdrop */}
        <Pressable
          style={{ position: 'absolute', top: 0, bottom: 0, left: 0, right: 0, backgroundColor: 'rgba(0,0,0,0.5)' }}
          onPress={onClose}
        />

        {/* Sheet Content */}
        <View
          style={{
            backgroundColor: Colors[colorScheme ?? 'light'].background,
            borderTopLeftRadius: 24,
            borderTopRightRadius: 24,
            paddingTop: 12,
            paddingBottom: 40,
            maxHeight: SCREEN_HEIGHT * 0.6,
            width: '100%',
          }}
        >
          {/* Handle */}
          <View
            style={{
              width: 40,
              height: 4,
              borderRadius: 2,
              alignSelf: 'center',
              marginBottom: 16,
            }}
          />

          {title && (
            <View style={{ paddingHorizontal: 20, marginBottom: 16 }}>
              <Text className="text-xl font-bold">{title}</Text>
            </View>
          )}

          <ScrollView showsVerticalScrollIndicator={false} className="px-4">
            {children}
          </ScrollView>
        </View>
      </View>
    </Modal>
  );
};

export default BottomSheet;
