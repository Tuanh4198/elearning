import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IExam } from 'app/shared/model/exam.model';
import axios from 'axios';

export const FetchExamDetailName = 'FetchExamDetailName';
export const useFetchExamDetail = ({ id }: { id?: string }) => {
  const fetchExam = async () => {
    if (!id) throw new Error();
    return await axios.get<IExam>(`/api/exams/${id}`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [FetchExamDetailName, id],
    queryFn: fetchExam,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    refetch,
  };
};
