import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { ICourse } from 'app/shared/model/course.model';
import axios from 'axios';

export const FetchCourseDetailName = 'FetchCourseDetailName';
export const useFetchCourseDetail = ({ id }: { id?: string }) => {
  const fetchCourse = async () => {
    if (!id) throw new Error();
    return await axios.get<ICourse>(`/api/courses/${id}`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [FetchCourseDetailName, id],
    queryFn: fetchCourse,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    refetch,
  };
};
