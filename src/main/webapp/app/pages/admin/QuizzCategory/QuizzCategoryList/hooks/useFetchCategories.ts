import { useState } from 'react';
import axios from 'axios';
import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IQuizzCategory } from 'app/shared/model/quizz-category.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

interface IFilterParams {
  search: string;
  page: number;
}
export const useFetchCategories = (filter: IFilterParams) => {
  const [size, setSize] = useState<number>(ITEMS_PER_PAGE);

  const fetchCategories = async () => {
    const params = { page: filter.page - 1, size, search: filter.search };
    return await axios.get<IQuizzCategory[]>(`/api/quizz-categories`, { params });
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: ['useFetchCategories', filter, size],
    queryFn: fetchCategories,
    placeholderData: keepPreviousData,
    gcTime: 0,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    size,
    total: data?.headers?.xTotalCount,
    setSize,
    refetch,
  };
};
