import SearchActiveScreen from './active';
import SearchHomeScreen from './home';
import useSearchState from './hooks/use-search-state';

export default function SearchScreen() {
  const { isSearchActive, toggleSearchState } = useSearchState();

  return (
    <>
      {isSearchActive ? (
        <SearchActiveScreen toggleSearchState={toggleSearchState} />
      ) : (
        <SearchHomeScreen toggleSearchState={toggleSearchState} />
      )}
    </>
  );
}
