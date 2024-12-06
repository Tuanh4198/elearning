/* eslint-disable @typescript-eslint/no-non-null-assertion */
import { useEffect, useRef } from 'react';
import { IAnswerBody } from 'app/pages/user/Exam/ExamDetail/hooks/useEnterAnswer';
import { IQuizz } from 'app/shared/model/quizz.model';
import axios, { AxiosResponse } from 'axios';
import { QueryObserverResult, RefetchOptions, useMutation } from '@tanstack/react-query';
import { notiError, notiSuccess } from 'app/shared/notifications';
import dayjs from 'dayjs';
import { FORMAT_TIME } from 'app/config/constants';
import { IExamEmployeeResult } from 'app/shared/model/exam-employee-result.model';

export interface IBodySubmitFormTest {
  root_id: number;
  start_at: string;
  finished_at: string;
  answers: IAnswerBody[];
}

export const useSubmitFormTest = ({
  examId,
  answersRef,
  quizzs,
  totalTime,
  onSuccess,
}: {
  examId?: number;
  answersRef: { [key: number]: IAnswerBody };
  quizzs?: IQuizz[];
  totalTime: number;
  onSuccess: (options?: RefetchOptions) => Promise<QueryObserverResult<AxiosResponse<IExamEmployeeResult, any> | undefined, Error>>;
}) => {
  const startAtRef = useRef<string>();
  const resultRef = useRef<any>();

  const handleSetStartAt = () => {
    startAtRef.current = dayjs().format(FORMAT_TIME);
  };

  useEffect(() => {
    handleSetStartAt();
  }, []);

  const postData = async (body: IBodySubmitFormTest) => {
    const respon = await axios.post<{
      finished_at: string;
      id: number;
      minPointToPass: number;
      numberOfCorrect: number;
      numberOfQuestion: number;
      pass: boolean;
      rootId: number;
      startAt: string;
    }>('/api/exam-employees/submit', body);
    const result = await onSuccess();
    return {
      ...respon?.data,
      numberOfTest: result?.data?.data?.numberOfTest,
      status: result?.data?.data?.status,
    };
  };

  const mutation = useMutation({
    mutationFn: postData,
    onSuccess() {
      notiSuccess({ message: 'Nộp bài thi thành công' });
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Nộp bài thi thất bại!' });
    },
  });

  const onSubmit = (isTimeout = false) => {
    if (startAtRef.current && quizzs && examId) {
      mutation.mutate({
        root_id: examId,
        answers: Object.values(answersRef),
        start_at: startAtRef.current,
        finished_at: isTimeout ? dayjs(startAtRef.current).add(totalTime, 'minute').toISOString() : dayjs().format(FORMAT_TIME),
      });
    }
  };

  useEffect(() => {
    resultRef.current = mutation?.data;
  }, [mutation?.data]);

  useEffect(() => {
    const handleBeforeUnload = event => {
      if (!resultRef.current) {
        event.preventDefault();
        return (event.returnValue = '');
      }
    };
    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, []);

  return {
    onSubmit,
    mutation,
    result: mutation?.data,
    submiting: mutation.isPending,
    handleSetStartAt,
  };
};
