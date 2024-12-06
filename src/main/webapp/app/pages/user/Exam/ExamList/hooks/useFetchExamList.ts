import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IExam } from 'app/shared/model/exam.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import axios from 'axios';
import { useCallback, useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

export const FetchExamListName = 'FetchExamListName';
const defaultCategoryId = 'all';
export const useFetchExamList = ({ withCategory }: { withCategory?: boolean }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const categoryId = queryParams.get('categoryId');

  const [params, setParams] = useState<{
    page: number;
    size: number;
    categoryId?: string | null;
  }>({
    page: 0,
    size: ITEMS_PER_PAGE,
    categoryId: withCategory ? defaultCategoryId : null,
  });

  const handleNextPage = useCallback((value: number) => {
    setParams(old => ({
      ...old,
      page: value - 1,
    }));
  }, []);

  const handleChangeCategoryId = useCallback((value: string) => {
    setParams(old => ({
      ...old,
      page: 0,
      categoryId: value,
    }));
  }, []);

  const fetchExamList = async () => {
    return await axios.get<IExam[]>(
      `/api/exams${params.categoryId === defaultCategoryId ? '' : `?categoryId.equals=${params.categoryId}`}`,
      {
        params: {
          page: params.page,
          size: params.size,
        },
      }
    );
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [FetchExamListName, params],
    queryFn: fetchExamList,
    placeholderData: keepPreviousData,
  });

  useEffect(() => {
    if (categoryId && withCategory) {
      handleChangeCategoryId(categoryId);
    }
  }, []);

  useEffect(() => {
    if (params.categoryId) {
      const newQueryParams = new URLSearchParams({ categoryId: params.categoryId }).toString();
      navigate({ search: `?${newQueryParams}` });
    }
  }, [params.categoryId]);

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    size: params.size,
    total: data?.headers?.xTotalCount,
    categoryId: params.categoryId,
    defaultCategoryId,
    handleNextPage,
    handleChangeCategoryId,
    refetch,
  };
};
