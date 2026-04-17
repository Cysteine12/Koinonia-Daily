import {
  Image as RNImage,
  type ImageProps as RNImageProps,
  type ImageSourcePropType as RNImageSourcePropType,
} from 'react-native';

export type ImageProps = RNImageProps;

export type ImageSourcePropType = RNImageSourcePropType;

export function Image(props: ImageProps) {
  return <RNImage {...props} />;
}
