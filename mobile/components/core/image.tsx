import { Image as RNImage, type ImageProps as RNImageProps } from 'react-native';

export type ImageProps = RNImageProps & {
  className?: string;
};

export function Image({ className, ...props }: ImageProps) {
  return <RNImage className={className} {...props} />;
}
