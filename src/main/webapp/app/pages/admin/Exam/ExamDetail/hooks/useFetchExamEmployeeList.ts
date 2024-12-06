import { IFilterConditionExam } from 'app/pages/admin/Exam/ExamList/hooks/useFilterConditionExam';
import { useCallback, useState } from 'react';
import axios from 'axios';
import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IExamEmployee } from 'app/shared/model/exam-employee.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export const useFetchExamEmployeeList = ({ conditions }: { conditions?: IFilterConditionExam }) => {
  const [params, setParams] = useState<
    {
      page: number;
      size: number;
    } & IFilterConditionExam
  >({
    page: 0,
    size: ITEMS_PER_PAGE,
  });

  const handleNextPage = useCallback((value: number) => {
    setParams(old => ({
      ...old,
      page: value - 1,
    }));
  }, []);

  const fetchExamList = async () => {
    if (conditions?.rootId) {
      params.rootId = conditions.rootId;
    }
    if (conditions?.search) {
      params.search = conditions.search;
    }
    return await axios.get<IExamEmployee[]>(`/api/admin-exam-employees`, { params });
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: ['AdminFetchExamEmployeeListName', params, conditions],
    queryFn: fetchExamList,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    size: params.size,
    total: data?.headers?.xTotalCount,
    handleNextPage,
    refetch,
  };
};
