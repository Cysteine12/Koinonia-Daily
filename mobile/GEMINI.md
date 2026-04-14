# Project Overview: Koinonia Daily

Koinonia Daily is a modern React Native application built with the Expo ecosystem (SDK 54). It serves as a platform for spiritual teachings, featuring audio/video content, collections, and user profiles. The application prioritizes a polished UI with a custom gold-themed aesthetic and supports both light and dark modes.

## Core Technologies

- **Framework:** [Expo](https://expo.dev/) (React Native)
- **Routing:** [Expo Router](https://docs.expo.dev/router/introduction/) (File-based routing)
- **Styling:** [NativeWind](https://www.nativewind.dev/) (Tailwind CSS for React Native) & Custom Theming
- **State Management:** [Zustand](https://github.com/pmndrs/zustand) (Client state) & [React Query](https://tanstack.com/query/latest) (Server state)
- **Networking:** [Axios](https://axios-http.com/) with interceptors for token management
- **Authentication:** Custom JWT-based flow with `expo-secure-store`
- **UI Components:** [RN Primitives](https://rn-primitives.vercel.app/) (Radix-inspired primitives)
- **Validation:** [Zod](https://zod.dev/)

## Directory Structure

- `app/`: Expo Router routes. Includes `(auth)` for authentication and `(tabs)` for the main application navigation.
- `components/`:
  - `core/`: Fundamental UI building blocks (View, Text, Screen).
  - `reusables/ui/`: Generic, reusable UI components (Button, Card, Input).
  - `ui/`: Specialized UI components (BottomSheets, Gradients).
- `features/`: Domain-specific logic, organized by feature (auth, user, teaching).
  - `api.ts`: Feature-specific API calls.
  - `hook.ts`: React Query hooks for data fetching.
  - `store.ts`: Zustand stores.
- `screens/`: Screen-level components and their specific sub-components.
- `lib/`: Utility libraries and shared configurations (API clients, storage wrappers, loggers).
- `constants/`: Global constants including `theme.ts` (colors, gradients) and `font.ts`.
- `hooks/`: Global custom hooks (theme, fonts, forms).

## Building and Running

### Development
```bash
# Install dependencies
npm install

# Start the Expo development server
npx expo start

# Run on specific platforms
npm run android
npm run ios
```

### Maintenance
```bash
# Linting
npm run lint

# Prettier
npm run prettier:check
npm run prettier:fix
```

## Development Conventions

- **File Naming:** Use `kebab-case` for all files and directories (e.g., `auth-context.tsx`, `social-connections.tsx`).
- **Styling & Layout:** Use NativeWind classes (`className`) for layout, spacing, sizing, and non-theme-dependent styles.
- **Theming & Colors:** For theme-dependent colors (text, background, borders, etc.), **prefer retrieving color values from the `useAppTheme` hook and applying them via the `style` prop.** Avoid using custom Tailwind/NativeWind classes for these dynamic theme colors to ensure consistency with the established theme engine.
- **Data Fetching:** Always use React Query hooks located in `features/*/hook.ts` for server data.
- **Authentication:** Use the `useAuth` hook from `features/auth/auth-context.tsx` to access authentication state and methods.
- **Types:** Use TypeScript strictly. Define schemas with `zod` for API responses and form validation.
- **Project Structure:** Place new features inside the `features/` directory, following the existing pattern of `api`, `hook`, `store`, and `types`.

