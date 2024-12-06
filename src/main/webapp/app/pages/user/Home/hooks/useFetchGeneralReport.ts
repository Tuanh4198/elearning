import axios from 'axios';
import { keepPreviousData, useQuery } from '@tanstack/react-query';

export interface IGeneralReport {
  totalCompletedCourse: number;
  totalCourse: number;
  totalCompletedExam: number;
  totalExam: number;
}
export const useFetchGeneralReport = () => {
  const fetchReport = async () => {
    return await axios.get<IGeneralReport>(`/api/statistics`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: ['useFetchGeneralReport'],
    queryFn: fetchReport,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isFetching || isLoading,
    data: data?.data,
    refetch,
  };
};
