import { SymbolView, type SymbolViewProps, type SymbolWeight } from 'expo-symbols';
import type { StyleProp, ViewStyle } from 'react-native';
import type { IconSymbolName } from './icon';

/**
 * Add your IconSymbol to SF Symbols mappings here.
 * - see SF Symbols in the [SF Symbols](https://developer.apple.com/sf-symbols/) app.
 */
const MAPPING: Record<IconSymbolName, SymbolViewProps['name']> = {
  home: 'house.fill',
  'home.outline': 'house',
  search: 'scanner.fill',
  'search.outline': 'scanner',
  library: 'book.closed.fill',
  'library.outline': 'book.closed',
  activity: 'chart.line.uptrend.xyaxis',
  'activity.outline': 'chart.line.uptrend.xyaxis',
  profile: 'person.fill',
  'profile.outline': 'person',
  'chevron.left': 'chevron.left',
  'chevron.right': 'chevron.right',
  'arrow.forward': 'arrow.forward',
  'arrow.backward': 'arrow.backward',
  close: 'xmark',
  'close.circle': 'xmark.circle',
  check: 'checkmark.circle',
  add: 'plus',
  rocket: 'fireworks',
  'timer.outline': 'timer',
  'clock.outline': 'clock',
  'book.outline': 'book',
  'check.circle.outline': 'checkmark.circle',
  flame: 'flame',
  'menu.horizontal': 'ellipsis',
};

export function Icon({
  name,
  size = 24,
  color,
  style,
  weight = 'regular',
}: {
  name: IconSymbolName;
  size?: number;
  color: string;
  style?: StyleProp<ViewStyle>;
  weight?: SymbolWeight;
}) {
  const symbolName = MAPPING[name] ?? 'questionmark';

  return (
    <SymbolView
      weight={weight}
      tintColor={color}
      resizeMode="scaleAspectFit"
      name={symbolName}
      style={[
        {
          width: size,
          height: size,
        },
        style,
      ]}
    />
  );
}
