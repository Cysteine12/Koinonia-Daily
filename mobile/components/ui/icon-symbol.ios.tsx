import { SymbolView, type SymbolViewProps, type SymbolWeight } from 'expo-symbols';
import type { StyleProp, ViewStyle } from 'react-native';

/**
 * Render a symbol icon using SymbolView.
 *
 * @param name - The identifier of the symbol to render.
 * @param size - The icon's width and height in pixels (defaults to 24).
 * @param color - Color applied to the symbol as its tint.
 * @param style - Optional additional view styles to merge with the size.
 * @param weight - Visual weight/style of the symbol (e.g., "regular"); defaults to "regular".
 * @returns A configured SymbolView element displaying the requested symbol.
 */
export function IconSymbol({
  name,
  size = 24,
  color,
  style,
  weight = 'regular',
}: {
  name: SymbolViewProps['name'];
  size?: number;
  color: string;
  style?: StyleProp<ViewStyle>;
  weight?: SymbolWeight;
}) {
  return (
    <SymbolView
      weight={weight}
      tintColor={color}
      resizeMode="scaleAspectFit"
      name={name}
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
