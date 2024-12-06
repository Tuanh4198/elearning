import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { ICourseEmployee } from 'app/shared/model/course-employee.model';
import axios from 'axios';
import { useCallback, useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { CourseEmployeeStatusEnum } from 'app/shared/model/enumerations/course-employee-status-enum.model';

const defaultSize = 100;

export interface ICourseEmployeeFilter {
  from: string | null;
  to: string | null;
  status: CourseEmployeeStatusEnum | null;
}
export const useFetchCourseEmployee = ({ from, to, status }: ICourseEmployeeFilter) => {
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

  const fetchCourseList = async () => {
    return await axios.get<ICourseEmployee[]>(`/api/course-employees`, {
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
    queryKey: [`useFetchCourseEmployee`, params],
    queryFn: fetchCourseList,
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
