import { useLocalSearchParams, useRouter } from 'expo-router';
import { useEffect, useState } from 'react';

export const SearchState = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
} as const;

export type SearchStateType = keyof typeof SearchState;

export default function useSearchState() {
  const router = useRouter();
  const { searchState = 'INACTIVE' } = useLocalSearchParams<{ searchState: SearchStateType }>();
  const [isSearchActive, setSearchActive] = useState(false);

  useEffect(() => {
    setSearchActive(searchState === SearchState.ACTIVE);
  }, [searchState]);

  const toggleSearchState = () => {
    router.setParams({ searchState: searchState === SearchState.ACTIVE ? SearchState.INACTIVE : SearchState.ACTIVE });
  };

  return { isSearchActive, toggleSearchState };
}
