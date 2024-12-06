import { useScrollIntoView } from '@mantine/hooks';
import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { ICourseEmployee } from 'app/shared/model/course-employee.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import axios from 'axios';
import { useCallback, useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const defaultCategoryId = 'all';
const defaultStatus = 'IN_PROGRESS';
const FetchCourseListName = 'FetchCourseListUser';
export const useFetchCourseList = ({ withStatus, withCategory }: { withStatus?: boolean; withCategory?: boolean }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const categoryId = queryParams.get('categoryId');

  const { scrollIntoView } = useScrollIntoView<HTMLDivElement>();

  const [params, setParams] = useState<{
    page: number;
    size: number;
    categoryId?: string | null;
    status?: string | null;
  }>({
    page: 1,
    size: ITEMS_PER_PAGE,
    categoryId: withCategory ? defaultCategoryId : null,
    status: withStatus ? defaultStatus : null,
  });

  const handleNextPage = useCallback((value: number) => {
    setParams(old => ({
      ...old,
      page: value,
    }));
    scrollIntoView({ alignment: 'start' });
  }, []);

  const handleChangeCategoryId = useCallback((value: string) => {
    setParams(old => ({
      ...old,
      page: 1,
      categoryId: value,
    }));
  }, []);

  const fetchCourseList = async () => {
    return await axios.get<ICourseEmployee[]>(
      `/api/course-employees${!params.categoryId || params.categoryId === defaultCategoryId ? '' : `?categoryId=${params.categoryId}`}`,
      {
        params: {
          page: params.page - 1,
          size: params.size,
          status: params.status,
        },
      }
    );
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [`${FetchCourseListName}${withStatus ? 'Status' : ''}`, params],
    queryFn: fetchCourseList,
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
    page: params.page,
    defaultCategoryId,
    handleNextPage,
    handleChangeCategoryId,
    refetch,
  };
};
