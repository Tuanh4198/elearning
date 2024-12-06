import { useQuery, keepPreviousData } from '@tanstack/react-query';
import { CategoryTypeEnum, ICategory } from 'app/shared/model/category.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import axios from 'axios';
import { useCallback, useEffect, useRef, useState } from 'react';

export const FetchCategoriesName = 'FetchExamCategoriesName';
export const useFetchCategories = ({ type }: { type: CategoryTypeEnum }) => {
  const categoriesRef = useRef<ICategory[]>([]);

  const [params, setParams] = useState<{
    page: number;
    size: number;
    type: CategoryTypeEnum;
  }>({
    page: 0,
    size: ITEMS_PER_PAGE,
    type,
  });

  const handleNextPage = useCallback(() => {
    setParams(old => ({
      ...old,
      page: old.page + 1,
    }));
  }, []);

  const fetchCategories = async () => {
    const res = await axios.get<ICategory[]>(`/api/exam-employees/categories`, {
      params,
    });
    return {
      ...res,
      data: [...categoriesRef.current, ...res.data],
    };
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [FetchCategoriesName, params],
    queryFn: fetchCategories,
    placeholderData: keepPreviousData,
  });

  useEffect(() => {
    if (data?.data) {
      categoriesRef.current = data.data;
    }
  }, [data]);

  return {
    isLoading: isFetching || isLoading,
    data: data?.data,
    size: params.size,
    total: data?.headers?.xTotalCount,
    handleNextPage,
    refetch,
  };
};
