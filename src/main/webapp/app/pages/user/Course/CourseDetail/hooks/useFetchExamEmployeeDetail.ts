import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IExamEmployee } from 'app/shared/model/exam-employee.model';
import axios from 'axios';

export const FetchExamEmployeeDetailName = 'FetchExamEmployeeDetailName';
export const useFetchExamEmployeeDetail = ({ id, checkedExamCanJoin }: { id?: string; checkedExamCanJoin: boolean | undefined }) => {
  const fetchExamEmployee = async () => {
    if (!id) throw new Error();
    if (checkedExamCanJoin == null || checkedExamCanJoin === false) throw new Error();
    return await axios.get<IExamEmployee>(`/api/exam-employees/${id}`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [FetchExamEmployeeDetailName, id, checkedExamCanJoin],
    queryFn: fetchExamEmployee,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    refetch,
  };
};
