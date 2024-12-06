import { useState } from 'react';
import axios from 'axios';
import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IQuizz } from 'app/shared/model/quizz.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

interface IFilterParams {
  search: string;
  page: number;
}

export const useFetchQuizzes = (filter: IFilterParams) => {
  const [size, setSize] = useState<number>(ITEMS_PER_PAGE);

  const fetchCategories = async () => {
    const params = { page: filter.page - 1, size, search: filter.search };
    return await axios.get<IQuizz[]>(`/api/quizzes`, { params });
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: ['useFetchQuizzes', filter.page, size, filter.search],
    queryFn: fetchCategories,
    placeholderData: keepPreviousData,
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
