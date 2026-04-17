import { Screen } from '@/components/core';
import SearchActiveScreen from './active';
import SearchHomeScreen from './home';
import useBackHandler from './hooks/use-back-handler';
import useSearchState from './hooks/use-search-state';

export default function SearchScreen() {
  const { isSearchActive, toggleSearchState } = useSearchState();

  useBackHandler({
    isActive: isSearchActive,
    handler: toggleSearchState,
  });

  return (
    <Screen edges={['top']}>
      {isSearchActive ? (
        <SearchActiveScreen toggleSearchState={toggleSearchState} />
      ) : (
        <SearchHomeScreen toggleSearchState={toggleSearchState} />
      )}
    </Screen>
  );
}
