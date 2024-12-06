import { keepPreviousData, useQuery } from '@tanstack/react-query';
import axios from 'axios';

export const CheckExamCanJoinName = 'CheckExamCanJoinName';
export const useCheckExamCanJoin = ({ id }: { id?: string }) => {
  const checkFunc = async () => {
    if (!id) throw new Error();
    return await axios.get<boolean>(`/api/exam-employees/exists/${id}`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [CheckExamCanJoinName, id],
    queryFn: checkFunc,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    refetch,
  };
};
