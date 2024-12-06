import { useScrollIntoView } from '@mantine/hooks';
import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { ExamEmployeeStatusEnum } from 'app/shared/model/enumerations/exam-employee-status-enum.model';
import { IExamEmployee } from 'app/shared/model/exam-employee.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import axios from 'axios';
import { useCallback, useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

export const FetchExamEmployeesName = 'FetchExamEmployeesName';
const defaultCategoryId = 'all';
export const useFetchExamEmployees = ({ withStatus, withCategory }: { withStatus?: boolean; withCategory?: boolean }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const categoryId = queryParams.get('categoryId');

  const { scrollIntoView } = useScrollIntoView<HTMLDivElement>();

  const [params, setParams] = useState<{
    page: number;
    size: number;
    status?: ExamEmployeeStatusEnum | null;
    categoryId?: string | null;
  }>({
    page: 1,
    size: ITEMS_PER_PAGE,
    status: withStatus ? ExamEmployeeStatusEnum.NOT_COMPLETED : null,
    categoryId: withCategory ? defaultCategoryId : null,
  });

  const handleNextPage = useCallback((value: number) => {
    setParams(old => ({
      ...old,
      page: value,
    }));
    scrollIntoView({ alignment: 'start' });
  }, []);

  const handleNextStatus = useCallback((value: string) => {
    setParams(old => ({
      ...old,
      page: 1,
      status: value as ExamEmployeeStatusEnum,
    }));
  }, []);

  const handleChangeCategoryId = useCallback((value: string) => {
    setParams(old => ({
      ...old,
      page: 1,
      categoryId: value,
    }));
  }, []);

  const fetchExamList = async () => {
    return await axios.get<IExamEmployee[]>(
      `/api/exam-employees${!params.categoryId || params.categoryId === defaultCategoryId ? '' : `?categoryId=${params.categoryId}`}`,
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
    queryKey: [FetchExamEmployeesName, params],
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
    page: params.page,
    status: params.status,
    categoryId: params.categoryId,
    defaultCategoryId,
    handleNextPage,
    handleNextStatus,
    handleChangeCategoryId,
    refetch,
  };
};
