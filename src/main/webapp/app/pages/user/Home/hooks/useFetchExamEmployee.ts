import { keepPreviousData, useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { useCallback, useState } from 'react';
import { ExamEmployeeStatusEnum } from 'app/shared/model/enumerations/exam-employee-status-enum.model';
import { IExamEmployee } from 'app/shared/model/exam-employee.model';

const defaultSize = 100;

export interface IExamEmployeeFilter {
  from: string | null;
  to: string | null;
  status: ExamEmployeeStatusEnum | null;
}
export const useFetchExamEmployee = ({ from, to, status }: IExamEmployeeFilter) => {
  const [params, setParams] = useState<{
    page: number;
    size: number;
    status?: string | null;
    from?: string | null;
    to?: string | null;
  }>({
    page: 0,
    size: defaultSize,
    status: status ? status : null,
    from: from ? from : null,
    to: to ? to : null,
  });

  const handleNextPage = useCallback((value: number) => {
    setParams(old => ({
      ...old,
      page: value - 1,
    }));
  }, []);

  const fetchExamList = async () => {
    return await axios.get<IExamEmployee[]>(`/api/exam-employees`, {
      params: {
        page: params.page,
        size: params.size,
        status: params.status,
        from: params.from,
        to: params.to,
      },
    });
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [`useFetchExamEmployee`, params],
    queryFn: fetchExamList,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    size: defaultSize,
    total: data?.headers?.xTotalCount,
    handleNextPage,
    refetch,
  };
};
