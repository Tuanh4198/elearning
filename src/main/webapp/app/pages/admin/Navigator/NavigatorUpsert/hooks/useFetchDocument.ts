import { useState } from 'react';
import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { IDocument } from 'app/shared/model/document.model';
import { useDebouncedValue } from '@mantine/hooks';

export const useFetchDocument = () => {
  const [search, setSearch] = useState<string>('');
  const [page, setPage] = useState<number>(1);
  const [debounced] = useDebouncedValue(search, 200);

  const fetchCategories = async () => {
    const params = { page: page - 1, size: ITEMS_PER_PAGE, search: debounced };
    return await axios.get<IDocument[]>(`/api/documents`, { params });
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: ['useFetchDocument', page, debounced],
    queryFn: fetchCategories,
    gcTime: 0,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    size: ITEMS_PER_PAGE,
    total: data?.headers?.xTotalCount,
    search,
    setSearch,
    page,
    setPage,
    refetch,
  };
};
