# Koinonia Daily Mobile

Koinonia Daily is a modern React Native application built with the **Expo ecosystem (SDK 54)**. It serves as a spiritual platform for accessing teachings, sermons, and songs by Apostle Joshua Selman and the Koinonia Ministry. The application features a premium, custom gold-themed aesthetic with a focus on high performance and user experience.

## 🚀 Tech Stack

- **Framework:** [Expo SDK 54](https://expo.dev/) (React Native)
- **Routing:** [Expo Router](https://docs.expo.dev/router/introduction/) (File-based routing)
- **Styling:** [NativeWind v4](https://www.nativewind.dev/) (Tailwind CSS for React Native)
- **State Management:** [Zustand](https://github.com/pmndrs/zustand) (Client state) & [React Query](https://tanstack.com/query/latest) (Server state)
- **Networking:** [Axios](https://axios-http.com/) with interceptors for token management
- **Authentication:** Custom JWT flow with `expo-secure-store`
- **UI Components:** [RN Primitives](https://rn-primitives.vercel.app/) (Radix-inspired) & [Lucide Icons](https://lucide.dev/)
- **Validation:** [Zod](https://zod.dev/)
- **Animation:** [React Native Reanimated](https://docs.swmansion.com/react-native-reanimated/) & [Moti](https://moti.fyi/)

## 📂 Directory Structure

- `app/`: Expo Router routes.
  - `(auth)/`: Authentication flow (Login, Register, Forgot Password).
  - `(tabs)/`: Main tab-based navigation (Home, Search, Library, Activity, Profile).
- `components/`:
  - `core/`: Fundamental UI building blocks (Screen, Text, View, Image).
  - `reusables/ui/`: Generic UI components (Button, Card, Input, etc.).
  - `ui/`: Feature-specific UI elements (BottomSheets, Gradients, Snackbars).
- `features/`: Domain-driven logic (auth, user, teaching, account).
  - `api.ts`: API service calls.
  - `hook.ts`: React Query hooks for data fetching.
  - `store.ts`: Zustand stores for client state.
  - `types.ts`: TypeScript interfaces and Zod schemas.
- `screens/`: Complex screen-level components and their unique sub-components.
- `hooks/`: Global custom hooks (theming, fonts, forms, overlays).
- `lib/`: Utilities and shared configurations (API clients, storage, loggers).
- `constants/`: Global constants including `theme.ts` (colors, gradients) and `font.ts`.
- `assets/`: Static assets (fonts, images, branding).

## 🛠️ Getting Started

### Prerequisites

- Node.js (Latest LTS)
- pnpm
- Expo Go app (on your mobile device) or Android/iOS simulators

### Installation

```bash
# Clone the repository and navigate to the mobile directory
cd mobile

# Install dependencies
pnpm install
```

### Running the App

```bash
# Start the Expo development server
pnpm start

# Run on Android
pnpm android

# Run on iOS
pnpm ios
```

## 💅 Development Conventions

- **File Naming:** Use `kebab-case` for all files and directories.
- **Styling:** Use NativeWind classes (`className`) for layout, spacing, and static styles.
- **Theming:** For theme-dependent colors, use the `useAppTheme` hook and apply values via the `style` prop to ensure consistency with the custom theme engine.
- **Data Fetching:** Always use React Query hooks located in `features/*/hook.ts`. Do not call APIs directly from components.
- **Form Management:** Use the `useAppForm` hook (wrapper around `react-hook-form` and `zod`).
- **Icons:** Prefer `@expo/vector-icons` (Lucide, MaterialIcons).

## 🧹 Maintenance

```bash
# Run linting
pnpm lint

# Format code with Prettier
pnpm prettier:fix
```

## 🔐 Environment Variables

Create a `.env` file based on `.env.sample`:
```env
EXPO_PUBLIC_API_URL=https://your-api-url.com/api/v1
```

---
Built with ❤️ for Koinonia Daily.
