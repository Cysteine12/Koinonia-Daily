import { Text, View } from '@/components/core';
import { Colors, FontFamily, FontSize } from '@/constants';
import { useTheme } from '@/features/theme-context';
import { useAppTheme } from '@/hooks/use-app-theme';
import { Ionicons } from '@expo/vector-icons';
import { BlurView } from 'expo-blur';
import { LinearGradient } from 'expo-linear-gradient';
import React, { useState } from 'react';
import { StatusBar, StyleSheet, TouchableOpacity } from 'react-native';

// ─── Tokens ──────────────────────────────────────────────────────────────────

const COLORS = {
  base: '#0B0C10', // Near-black background
  surface: '#0F1117', // Slightly lighter for layering
  goldCore: '#C9973A', // Warm gold — light source centre
  goldMid: '#8B6820', // Deeper amber — mid falloff
  goldEdge: '#4A3A1A', // Very dark amber — edge of bloom
  textPrimary: '#F5F0E8', // Warm white — greeting
  textVerse: '#DDD5C4', // Slightly dimmed warm — verse body
  textRef: '#9A8F7E', // Muted gold-grey — verse reference
  divider: '#2A2520', // Subtle warm divider
  tagBg: 'rgba(201,151,58,0.10)',
  tagBorder: 'rgba(201,151,58,0.22)',
  tagText: '#C9973A',
};

// ─── Helpers ─────────────────────────────────────────────────────────────────

function getGreeting(): string {
  const hour = new Date().getHours();
  if (hour < 12) return 'Good morning';
  if (hour < 17) return 'Good afternoon';
  return 'Good evening';
}

// ─── Component ───────────────────────────────────────────────────────────────

export default function HeroSection() {
  const { theme, isDark } = useAppTheme();
  const { setThemeMode } = useTheme();

  const userName = 'Emmanuel';
  const verse = 'For I know the thoughts that I think towards you, says the Lord, thoughts of peace and not of evil.';
  const verseReference = 'Jeremiah 29:11';

  const [headerHeight, setHeaderHeight] = useState(0);

  // Format display date
  const displayDate = React.useMemo(() => {
    const d = new Date();
    return d.toLocaleDateString('en-US', {
      weekday: 'long',
      month: 'long',
      day: 'numeric',
      year: 'numeric',
    });
  }, []);

  return (
    <View style={styles.container} onLayout={(e) => setHeaderHeight(e.nativeEvent.layout.height)}>
      <StatusBar />

      {/* ── Layer 1: Solid dark base ── */}
      <View style={StyleSheet.absoluteFill} />

      {/* ── Layer 2: Top-right radial gold bloom (diagonal gradient) ── */}
      {/*
        Simulated with two overlapping LinearGradients:
        - Primary: from top-right corner diagonally to lower-left, gold → transparent
        - Secondary: from top edge downward, tighter, to reinforce the top-right source
        Together they create a convincing top-right corner light bloom.
      */}
      <LinearGradient
        colors={[COLORS.goldCore, COLORS.goldMid, COLORS.goldEdge, 'transparent']}
        locations={[0, 0.22, 0.42, 0.72]}
        start={{ x: 1, y: 0 }}
        end={{ x: 0.1, y: 0.85 }}
        style={[StyleSheet.absoluteFill, styles.bloomPrimary]}
      />

      {/* Secondary bloom: softens the top edge, reinforces corner */}
      <LinearGradient
        colors={['rgba(180,120,30,0.55)', 'rgba(140,90,20,0.20)', 'transparent']}
        locations={[0, 0.35, 0.65]}
        start={{ x: 0.85, y: 0 }}
        end={{ x: 0.4, y: 0.6 }}
        style={[StyleSheet.absoluteFill, styles.bloomSecondary]}
      />

      {/* ── Layer 3: Bottom dark veil — ensures text contrast ── */}
      <LinearGradient
        colors={['transparent', 'rgba(11,12,16,0.82)', COLORS.base]}
        locations={[0.2, 0.58, 1]}
        start={{ x: 0.5, y: 0 }}
        end={{ x: 0.5, y: 1 }}
        style={[StyleSheet.absoluteFill, styles.textVeil]}
      />

      {/* ── Content ── */}
      <View style={styles.content}>
        <View className="flex-row justify-between mt-5">
          {/* Date pill */}
          <View style={styles.datePill}>
            <Text style={styles.datePillText}>{displayDate.toUpperCase()}</Text>
          </View>

          <TouchableOpacity
            onPress={() => setThemeMode(isDark ? 'light' : 'dark')}
            className="mt-[-7px]"
            accessibilityRole="button"
            accessibilityLabel="Togggle theme"
          >
            <BlurView intensity={50} tint={theme} className="p-1.5 rounded-full">
              <Ionicons name={isDark ? 'sunny' : 'moon'} size={FontSize.lg} color={'#fff'} />
            </BlurView>
          </TouchableOpacity>
        </View>

        {/* Greeting */}
        <View style={styles.greetingBlock}>
          <Text style={styles.greetingLabel}>{getGreeting()},</Text>
          <Text style={[styles.greetingName, { color: Colors.dark.goldText }]}>{userName} 👋</Text>
        </View>

        {/* Divider */}
        <View style={styles.divider} />

        {/* Daily verse block */}
        <View style={styles.verseBlock}>
          <View style={styles.verseTag}>
            <Text style={styles.verseTagText}>VERSE OF THE DAY</Text>
          </View>

          <Text style={styles.verseText}>{`"${verse}"`}</Text>

          <Text style={styles.verseReference}>— {verseReference}</Text>
        </View>
      </View>
    </View>
  );
}

// ─── Styles ──────────────────────────────────────────────────────────────────

// const STATUS_BAR_HEIGHT = Platform.OS === 'android' ? (StatusBar.currentHeight ?? 24) : 0;

const styles = StyleSheet.create({
  skeleton: {
    backgroundColor: COLORS.base,
    height: 280,
    width: '100%',
    borderRadius: 0,
  },

  container: {
    backgroundColor: COLORS.base,
    width: '100%',
    overflow: 'hidden',
  },

  // Gradient layers
  bloomPrimary: {
    opacity: 0.72,
  },
  bloomSecondary: {
    opacity: 1,
  },
  textVeil: {
    // covers bottom half to protect text contrast
  },

  // ── Content layout ──
  content: {
    // paddingTop: STATUS_BAR_HEIGHT, // used to have STATUS_BAR_HEIGHT+20
    paddingBottom: 28,
    paddingHorizontal: 22,
    gap: 0,
  },

  // Date pill
  datePill: {
    alignSelf: 'flex-start',
    marginBottom: 18,
  },
  datePillText: {
    color: COLORS.tagText,
    letterSpacing: 2,
  },
  // datePill: {
  //   alignSelf: 'flex-start',
  //   backgroundColor: 'rgba(255,255,255,0.07)',
  //   borderRadius: 20,
  //   paddingHorizontal: 12,
  //   paddingVertical: 4,
  //   marginBottom: 18,
  //   borderWidth: 1,
  //   borderColor: 'rgba(255,255,255,0.10)',
  // },
  // datePillText: {
  //   fontFamily: FontFamily.Outfit_400Regular,
  //   fontSize: 11.5,
  //   color: COLORS.textRef,
  //   letterSpacing: 0.4,
  // },

  // Greeting
  greetingBlock: {
    marginBottom: 20,
  },
  greetingLabel: {
    fontFamily: FontFamily.Outfit_300Light,
    fontSize: 20,
    color: COLORS.textPrimary,
    letterSpacing: 0.1,
    opacity: 0.88,
    lineHeight: 26,
  },
  greetingName: {
    fontFamily: FontFamily.Outfit_600SemiBold,
    fontSize: 28,
    letterSpacing: -0.3,
    lineHeight: 34,
  },

  // Divider
  divider: {
    height: 1,
    backgroundColor: COLORS.divider,
    marginBottom: 20,
    opacity: 0.9,
  },

  // Verse block
  verseBlock: {
    gap: 10,
  },
  verseTag: {
    alignSelf: 'flex-start',
    backgroundColor: COLORS.tagBg,
    borderRadius: 4,
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderWidth: 1,
    borderColor: COLORS.tagBorder,
    marginBottom: 2,
  },
  verseTagText: {
    fontFamily: FontFamily.Outfit_500Medium,
    fontSize: 9.5,
    color: COLORS.tagText,
    letterSpacing: 1.4,
  },
  verseText: {
    fontFamily: FontFamily.Lora_400Regular_Italic,
    fontSize: 15.5,
    color: COLORS.textVerse,
    lineHeight: 25,
    letterSpacing: 0.1,
  },
  verseReference: {
    fontFamily: FontFamily.Outfit_400Regular,
    fontSize: 12,
    color: COLORS.textRef,
    letterSpacing: 0.3,
    marginTop: 2,
  },
});
