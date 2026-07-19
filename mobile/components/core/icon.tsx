import { useAppTheme } from '@/hooks/use-app-theme';
import { Ionicons, MaterialIcons } from '@expo/vector-icons';
import type { SymbolWeight } from 'expo-symbols';
import type { ComponentProps } from 'react';
import type { OpaqueColorValue, StyleProp, TextStyle } from 'react-native';

type IconFamily = 'MaterialIcons' | 'Ionicons';

type IconFamilyMap = {
  MaterialIcons: typeof MaterialIcons;
  Ionicons: typeof Ionicons;
};

type IconMappingEntry = {
  [K in IconFamily]: {
    name: ComponentProps<IconFamilyMap[K]>['name'];
    family: K;
  };
}[IconFamily];

type IconMapping = Record<string, IconMappingEntry>;

const MAPPING = {
  home: { name: 'home', family: 'Ionicons' },
  'home.outline': { name: 'home-outline', family: 'Ionicons' },
  search: { name: 'search-sharp', family: 'Ionicons' },
  'search.outline': { name: 'search-outline', family: 'Ionicons' },
  library: { name: 'library', family: 'Ionicons' },
  'library.outline': { name: 'library-outline', family: 'Ionicons' },
  activity: { name: 'trending-up', family: 'MaterialIcons' },
  'activity.outline': { name: 'trending-up', family: 'MaterialIcons' },
  profile: { name: 'person', family: 'Ionicons' },
  'profile.outline': { name: 'person-outline', family: 'Ionicons' },
  'chevron.left': { name: 'chevron-back', family: 'Ionicons' },
  'chevron.right': { name: 'chevron-forward', family: 'Ionicons' },
  'chevron.up': { name: 'chevron-up', family: 'Ionicons' },
  'chevron.down': { name: 'chevron-down', family: 'Ionicons' },
  'arrow.forward': { name: 'arrow-forward', family: 'MaterialIcons' },
  'arrow.backward': { name: 'arrow-back', family: 'MaterialIcons' },
  close: { name: 'close', family: 'Ionicons' },
  'close.circle': { name: 'cancel', family: 'MaterialIcons' },
  check: { name: 'check', family: 'MaterialIcons' },
  add: { name: 'add-circle', family: 'Ionicons' },
  rocket: { name: 'rocket', family: 'MaterialIcons' },
  'timer.outline': { name: 'timer-outline', family: 'Ionicons' },
  'clock.outline': { name: 'time-outline', family: 'Ionicons' },
  'book.outline': { name: 'book-outline', family: 'Ionicons' },
  'check.circle.outline': { name: 'check-circle-outline', family: 'MaterialIcons' },
  flame: { name: 'flame', family: 'Ionicons' },
  'menu.horizontal': { name: 'ellipsis-horizontal', family: 'Ionicons' },
  'calendar.outline': { name: 'calendar-outline', family: 'Ionicons' },
  series: { name: 'playlist-play', family: 'MaterialIcons' },
  folder: { name: 'folder-open-outline', family: 'Ionicons' },
  'bookmark.outline': { name: 'bookmark-outline', family: 'Ionicons' },
  'download.outline': { name: 'download-outline', family: 'Ionicons' },
  filter: { name: 'filter', family: 'Ionicons' },
  list: { name: 'list-sharp', family: 'Ionicons' },
  grid: { name: 'grid', family: 'Ionicons' },
  'grid.outline': { name: 'grid-outline', family: 'Ionicons' },
  'radio.on': { name: 'radio-button-on', family: 'Ionicons' },
  'radio.off': { name: 'radio-button-off', family: 'Ionicons' },
} satisfies IconMapping;

export type IconSymbolName = keyof typeof MAPPING;

/**
 * An icon component that uses Vector Icons on Android and native SF Symbols on iOS.
 * This ensures a consistent look across platforms, and optimal resource usage.
 * Icon `name`s are based on Vector Icons and require manual mapping to SF Symbols.
 */
export function Icon({
  name,
  size = 24,
  color,
  style = {},
  className = '',
}: {
  name: IconSymbolName;
  size?: number;
  color?: string | OpaqueColorValue;
  style?: StyleProp<TextStyle>;
  weight?: SymbolWeight;
  className?: string;
}) {
  const { color: iconColor } = useAppTheme();

  if (!color) color = iconColor.icon;

  if (!MAPPING[name]) {
    return <MaterialIcons name="help-outline" size={size} color={color} style={style} />;
  }

  switch (MAPPING[name]['family']) {
    case 'Ionicons':
      return <Ionicons color={color} size={size} name={MAPPING[name].name} style={style} className={className} />;
    case 'MaterialIcons':
    default:
      return <MaterialIcons color={color} size={size} name={MAPPING[name].name} style={style} className={className} />;
  }
}
