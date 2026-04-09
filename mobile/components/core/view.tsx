import { View as RNView, type ViewProps as RNViewProps } from 'react-native';

export type ViewProps = RNViewProps & {
  className?: string;
};

export function View({ className, ...props }: ViewProps) {
  return <RNView className={className} {...props} />;
}
