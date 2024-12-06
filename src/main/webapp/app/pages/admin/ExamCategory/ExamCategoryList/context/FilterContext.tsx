import React, { createContext, useContext, useMemo, useState } from 'react';
import { useDebouncedValue } from '@mantine/hooks';

interface IFilterParams {
  search: string;
  debouncedSearch: string;
  setSearch: (search: string) => void;
  page: number;
  setPage: (page: number) => void;
}

export const FilterContext = createContext<IFilterParams>({
  search: '',
  debouncedSearch: '',
  setSearch() {},
  page: 0,
  setPage() {},
});

export const useFilter = () => {
  const context = useContext(FilterContext);
  return context;
};

export const FilterProvider = ({ children }) => {
  const [search, setSearch] = useState<string>('');
  const [page, setPage] = useState<number>(1);
  const [debounced] = useDebouncedValue(search, 200);

  const value = useMemo(() => {
    return { debouncedSearch: debounced, search, setSearch, page, setPage };
  }, [search, setSearch, page, setPage, debounced]);

  return <FilterContext.Provider value={value}>{children}</FilterContext.Provider>;
};
