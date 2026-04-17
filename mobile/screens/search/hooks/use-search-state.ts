import { useLocalSearchParams, useRouter } from 'expo-router';

export const SearchState = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
} as const;

export type SearchStateType = keyof typeof SearchState;

export default function useSearchState() {
  const router = useRouter();
  const { searchState = SearchState.INACTIVE } = useLocalSearchParams<{ searchState: SearchStateType }>();
  const isSearchActive = searchState === SearchState.ACTIVE;

  const toggleSearchState = () => {
    router.setParams({ searchState: searchState === SearchState.ACTIVE ? SearchState.INACTIVE : SearchState.ACTIVE });
  };

  return { isSearchActive, toggleSearchState };
}
