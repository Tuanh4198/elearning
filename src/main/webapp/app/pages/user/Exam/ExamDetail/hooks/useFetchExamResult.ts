import { useQuery } from '@tanstack/react-query';
import { IExamEmployeeResult } from 'app/shared/model/exam-employee-result.model';
import axios from 'axios';

export const useFetchExamResult = ({ id, checkedExamCanJoin }: { id?: string; checkedExamCanJoin: boolean | undefined }) => {
  const fetchExam = async () => {
    if (!id) throw new Error();
    if (checkedExamCanJoin == null || checkedExamCanJoin === false) throw new Error();
    return await axios.get<IExamEmployeeResult>(`/api/exam-employees/${id}/result`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [id],
    queryFn: fetchExam,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    refetch,
  };
};
