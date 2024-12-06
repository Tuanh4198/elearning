import { useState } from 'react';
import axios from 'axios';
import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IQuizz } from 'app/shared/model/quizz.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { QuizzTypeEnum } from 'app/shared/model/enumerations/quizz-type-enum.model';

interface IFilterParams {
  type?: QuizzTypeEnum;
  categoryId?: number;
  search?: string;
  quizzIds?: (number | string)[];
  page: number;
}

export const useFetchQuizzes = (filter: IFilterParams) => {
  const [size, setSize] = useState<number>(ITEMS_PER_PAGE);

  const fetchCategories = async () => {
    if (!filter.categoryId) return;
    const params = {
      page: filter.page - 1,
      size,
    };
    if (filter.search != null && filter.search.trim() !== '') params['search'] = filter.search.trim();
    if (filter.type != null && filter.type.trim() !== '') params['type'] = filter.type;
    if (filter.categoryId != null) params['categoryId'] = filter.categoryId;
    if (filter.quizzIds != null && filter.quizzIds.length > 0) {
      if (filter.quizzIds.length <= 0) return { data: [], headers: { xTotalCount: 0 } };
      params['quizzIds'] = filter.quizzIds;
      params['size'] = filter.quizzIds.length;
    }
    return await axios.get<IQuizz[]>(`/api/quizzes`, { params });
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [`useFetchQuizzes`, filter, size],
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
