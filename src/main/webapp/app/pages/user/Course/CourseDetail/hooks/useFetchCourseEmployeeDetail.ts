import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { ICourseEmployee } from 'app/shared/model/course-employee.model';
import axios from 'axios';

export const FetchCourseEmployeeDetailName = 'FetchCourseEmployeeDetailName';
export const useFetchCourseEmployeeDetail = ({ id }: { id?: string }) => {
  const fetchCourseEmployee = async () => {
    if (!id) throw new Error();
    return await axios.get<ICourseEmployee>(`/api/course-employees/get-by-course/${id}`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [FetchCourseEmployeeDetailName, id],
    queryFn: fetchCourseEmployee,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    refetch,
  };
};
