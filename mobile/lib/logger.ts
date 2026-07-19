import * as Sentry from '@sentry/react-native';

Sentry.init({
  dsn: process.env.EXPO_PUBLIC_SENTRY_DSN,
  sendDefaultPii: false,
  tracesSampleRate: __DEV__ ? 1.0 : 0.2,
  environment: __DEV__ ? 'development' : 'production',
});

export default Sentry;
