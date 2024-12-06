import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IFilterConditionExam } from 'app/pages/admin/Exam/ExamList/hooks/useFilterConditionExam';
import { IExam } from 'app/shared/model/exam.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import axios from 'axios';
import { useCallback, useState } from 'react';

export const AdminFetchExamListName = 'AdminFetchExamListName';
export const useFetchExamList = ({ conditions }: { conditions?: IFilterConditionExam }) => {
  const [params, setParams] = useState<{
    page: number;
    size: number;
  }>({
    page: 1,
    size: ITEMS_PER_PAGE,
  });

  const handleNextPage = useCallback((value: number) => {
    setParams(old => ({
      ...old,
      page: value,
    }));
  }, []);

  const fetchExamList = async () => {
    return await axios.get<IExam[]>(
      `/api/exams`,
      // + conditions?.categoryId ? '?categoryId.equals=${conditions?.categoryId}' : ``
      // + conditions?.title ? '?title.contains=${conditions?.title}' : ``,
      {
        params: {
          page: params.page - 1,
          size: params.size,
        },
      }
    );
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [AdminFetchExamListName, params, conditions],
    queryFn: fetchExamList,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    size: params.size,
    page: params.page,
    total: data?.headers?.xTotalCount,
    handleNextPage,
    refetch,
  };
};
