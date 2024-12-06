import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IDepartment } from 'app/shared/model/department.model';
import axios from 'axios';

export const AdminFetchDepartmentName = 'AdminFetchDepartmentName';
export const useFetchDepartment = () => {
  const fetchDepartment = async () => {
    const requestUrl = `/api/departments?code.doesNotContains=''`;
    return await axios.get<IDepartment[]>(requestUrl);
  };

  const { isLoading, isFetching, data } = useQuery({
    queryKey: [AdminFetchDepartmentName],
    queryFn: fetchDepartment,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    total: data?.headers?.xTotalCount,
  };
};
