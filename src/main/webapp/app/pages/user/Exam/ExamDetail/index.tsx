import React, { Fragment, useMemo } from 'react';
import './styles.scss';
import { Text, Breadcrumbs, Grid } from '@mantine/core';
import { UserRoutes } from 'app/pages/user/routes';
import { Link, useParams } from 'react-router-dom';
import { ExamEmployeeStatusEnum } from 'app/shared/model/enumerations/exam-employee-status-enum.model';
import { useFetchExamDetail } from 'app/pages/user/Exam/ExamDetail/hooks/useFetchExamDetail';
import { IExam } from 'app/shared/model/exam.model';
import { useFetchExamEmployeeDetail } from 'app/pages/user/Exam/ExamDetail/hooks/useFetchExamEmployeeDetail';
import { ExamDetailColumnLeft } from 'app/pages/user/Exam/ExamDetail/components/ExamDetailColumnLeft';
import { ExamDetailColumnRight } from 'app/pages/user/Exam/ExamDetail/components/ExamDetailColumnRight';
import { useCheckExamCanJoin } from 'app/pages/user/Exam/ExamDetail/hooks/useCheckExamCanJoin';
import { useFetchExamResult } from 'app/pages/user/Exam/ExamDetail/hooks/useFetchExamResult';
import { QueryObserverResult, RefetchOptions } from '@tanstack/react-query';
import { AxiosResponse } from 'axios';
import { IExamEmployeeResult } from 'app/shared/model/exam-employee-result.model';
import { IQuizz } from 'app/shared/model/quizz.model';

interface IExamDetailProps {
  exam?: IExam;
  quizzs?: IQuizz[];
  status?: ExamEmployeeStatusEnum;
  isLoading: boolean;
  numberOfTest?: number;
  numberOfCorrect?: number;
  numberOfQuestion?: number;
  lastTestTime?: string;
  checkedExamCanJoin: boolean;
  refetchExamResult: (options?: RefetchOptions) => Promise<QueryObserverResult<AxiosResponse<IExamEmployeeResult, any> | undefined, Error>>;
}
const ExamDetail = ({
  exam,
  quizzs,
  status,
  isLoading,
  numberOfTest,
  numberOfCorrect,
  numberOfQuestion,
  lastTestTime,
  checkedExamCanJoin,
  refetchExamResult,
}: IExamDetailProps) => {
  const breadcrumbItems = useMemo(
    () => [
      { title: 'Kì thi', href: UserRoutes.EXAM },
      { title: 'Chi tiết kì thi', href: '#' },
    ],
    []
  );

  return (
    <Fragment>
      <Breadcrumbs
        mb="20px"
        separatorMargin="5px"
        separator={
          <Text size="12px" color="#E5E7EB">
            /
          </Text>
        }
      >
        {breadcrumbItems.map((item, index) => (
          <Link to={item.href} key={index}>
            <Text size="12px" color={breadcrumbItems.length - 1 === index ? '#2A2A86' : '#4B5563'}>
              {item.title}
            </Text>
          </Link>
        ))}
      </Breadcrumbs>
      <Grid gutter="20px">
        <Grid.Col span={8}>
          <ExamDetailColumnLeft isLoading={isLoading} exam={exam} />
        </Grid.Col>
        <Grid.Col span={4}>
          <ExamDetailColumnRight
            exam={exam}
            quizzs={quizzs}
            status={status}
            isLoading={isLoading}
            numberOfCorrect={numberOfCorrect || 0}
            numberOfTest={numberOfTest || 0}
            numberOfQuestion={numberOfQuestion || 0}
            lastTestTime={lastTestTime}
            checkedExamCanJoin={checkedExamCanJoin}
            refetchExamResult={refetchExamResult}
          />
        </Grid.Col>
      </Grid>
    </Fragment>
  );
};

const withExamDetailCanEnjoy = (WrappedComponent: (props: IExamDetailProps) => React.JSX.Element) => {
  return () => {
    const { id } = useParams();

    const { isLoading: isLoadingExamEmployee, data: examEmployee } = useFetchExamEmployeeDetail({ id, checkedExamCanJoin: true });

    const {
      isLoading: isLoadingExamResult,
      data: examResult,
      refetch: refetchExamResult,
    } = useFetchExamResult({ id, checkedExamCanJoin: true });

    if (!id) return null;

    return (
      <WrappedComponent
        exam={examEmployee?.exam}
        quizzs={examEmployee?.quizzs}
        isLoading={isLoadingExamEmployee || isLoadingExamResult}
        status={examResult?.status}
        numberOfCorrect={examResult?.numberOfCorrect}
        numberOfTest={examResult?.numberOfTest}
        numberOfQuestion={examResult?.numberOfQuestion}
        lastTestTime={examResult?.finishedAt}
        checkedExamCanJoin={true}
        refetchExamResult={refetchExamResult}
      />
    );
  };
};

export const ExamDetailCanEnjoy = withExamDetailCanEnjoy(ExamDetail);

const withExamDetailCanNotEnjoy = (WrappedComponent: (props: IExamDetailProps) => React.JSX.Element) => {
  return () => {
    const { id } = useParams();

    const { isLoading: isLoadingExam, data: exam } = useFetchExamDetail({ id });

    const { isLoading: isCheckingExamCanJoin, data: checkedExamCanJoin } = useCheckExamCanJoin({ id });

    const { isLoading: isLoadingExamEmployee, data: examEmployee } = useFetchExamEmployeeDetail({ id, checkedExamCanJoin });

    const { isLoading: isLoadingExamResult, data: examResult, refetch: refetchExamResult } = useFetchExamResult({ id, checkedExamCanJoin });

    if (!id) return null;

    return (
      <WrappedComponent
        exam={exam}
        quizzs={examEmployee?.quizzs}
        isLoading={isLoadingExam || isLoadingExamEmployee || isCheckingExamCanJoin || isLoadingExamResult}
        status={examEmployee?.status}
        numberOfCorrect={examResult?.numberOfCorrect}
        numberOfTest={examResult?.numberOfTest}
        numberOfQuestion={examResult?.numberOfQuestion}
        lastTestTime={examResult?.finishedAt}
        checkedExamCanJoin={!!checkedExamCanJoin}
        refetchExamResult={refetchExamResult}
      />
    );
  };
};

export const ExamDetailCanNotEnjoy = withExamDetailCanNotEnjoy(ExamDetail);
