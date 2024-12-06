import React, { Fragment, useEffect, useMemo } from 'react';
import { Text, Grid, Box, Pill, Image, Flex, Skeleton, Button } from '@mantine/core';
import { ExamEmployeeStatusEnum, ExamEmployeeStatusEnumTitle } from 'app/shared/model/enumerations/exam-employee-status-enum.model';
import { ExamMetafieldKey, IExam } from 'app/shared/model/exam.model';
import { compareDate, formatDateTime } from 'app/shared/util/date-utils';
import { parseNumber } from 'app/pages/user/Exam/utils';
import { APP_TIME_DATE_FORMAT } from 'app/config/constants';
import { useLocation, useNavigate } from 'react-router-dom';
import { useModalTakeAnExam } from 'app/pages/user/Exam/ExamDetail/components/ModalTakeAnExam';
import { IQuizz } from 'app/shared/model/quizz.model';
import { QueryObserverResult, RefetchOptions } from '@tanstack/react-query';
import { AxiosResponse } from 'axios';
import { IExamEmployeeResult } from 'app/shared/model/exam-employee-result.model';

interface IExamDetailColumnRightProps {
  exam?: IExam;
  quizzs?: IQuizz[];
  status?: ExamEmployeeStatusEnum;
  isLoading: boolean;
  numberOfTest: number;
  numberOfCorrect: number;
  numberOfQuestion: number;
  lastTestTime?: string;
  checkedExamCanJoin: boolean;
  refetchExamResult: (options?: RefetchOptions) => Promise<QueryObserverResult<AxiosResponse<IExamEmployeeResult, any> | undefined, Error>>;
}
export const ExamDetailColumnRight = ({
  numberOfQuestion,
  numberOfCorrect,
  numberOfTest,
  lastTestTime,
  exam,
  quizzs,
  status,
  isLoading,
  checkedExamCanJoin,
  refetchExamResult,
}: IExamDetailColumnRightProps) => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const isTesting = queryParams.get('isTesting');

  const maxNumberOfTest = useMemo(
    () => Number(exam?.metafields?.filter(i => i.key === ExamMetafieldKey.max_number_of_test)?.[0]?.value || 0),
    [exam]
  );

  const tagStatus = useMemo(() => {
    if (!status) return null;
    return ExamEmployeeStatusEnumTitle?.[status];
  }, [status]);

  const outOfDate = useMemo(() => {
    if (!exam?.expireTime) return true;
    return compareDate(exam?.expireTime);
  }, [exam?.expireTime]);

  const startOfDate = useMemo(() => {
    return compareDate(exam?.applyTime);
  }, [exam?.applyTime]);

  const showBtnJoinTest = useMemo(() => {
    return (
      checkedExamCanJoin &&
      numberOfTest < maxNumberOfTest &&
      status !== ExamEmployeeStatusEnum.PASS &&
      status !== ExamEmployeeStatusEnum.EXPIRED &&
      !startOfDate &&
      outOfDate
    );
  }, [checkedExamCanJoin, numberOfTest, maxNumberOfTest, status, startOfDate, outOfDate]);

  useEffect(() => {
    if (isTesting && showBtnJoinTest) {
      onOpenModalTakeAnExam();
      const newPath = location.pathname;
      navigate(newPath, { replace: true });
    }
  }, [showBtnJoinTest]);

  const { onOpenModalTakeAnExam, renderModalTakeAnExam } = useModalTakeAnExam();

  return (
    <Fragment>
      {isLoading ? (
        <Skeleton height={320} radius={16} />
      ) : (
        <Box bg="white" p="20px" className="exam-content-box">
          <Image src={exam?.thumbUrl} fallbackSrc="../../../../../content/images/defaultBanner.png" radius="8px" alt="Norway" mb="20px" />
          {checkedExamCanJoin && (
            <Flex direction="column" gap="20px">
              <Grid gutter="20px">
                <Grid.Col span={6}>
                  <Flex
                    direction="column"
                    align="center"
                    justify="center"
                    py="35px"
                    px="20px"
                    style={{
                      borderRadius: '16px',
                      border: '1px solid #E5E7EB',
                    }}
                  >
                    <Text color="#6B7280">Điểm</Text>
                    <Text color={status === ExamEmployeeStatusEnum.PASS ? '#12B886' : '#1F2A37'} size="20px" fw="600">
                      {numberOfCorrect && numberOfQuestion ? parseNumber((numberOfCorrect * 100) / numberOfQuestion) : 0}/100
                    </Text>
                  </Flex>
                </Grid.Col>
                <Grid.Col span={6}>
                  <Flex
                    direction="column"
                    align="center"
                    justify="center"
                    py="35px"
                    px="20px"
                    style={{
                      borderRadius: '16px',
                      border: '1px solid #E5E7EB',
                    }}
                  >
                    <Text color="#6B7280">Số lần làm</Text>
                    <Text color="#1F2A37" size="20px" fw="600">
                      {numberOfTest}/{maxNumberOfTest}
                    </Text>
                  </Flex>
                </Grid.Col>
              </Grid>
              {status && (
                <Text size="12px" fw="500">
                  <Pill size="sm" bg={tagStatus?.bg} style={{ color: tagStatus?.color }}>
                    {tagStatus?.label}
                  </Pill>
                </Text>
              )}
              {showBtnJoinTest && (
                <Button onClick={onOpenModalTakeAnExam} color="#2A2A86" variant="filled" radius="16px" size="md">
                  Làm bài
                </Button>
              )}
              {numberOfTest > 0 && lastTestTime && (
                <Flex align="center" justify="space-between">
                  <Text color="#6B7280">Thời gian làm bài gần nhất:</Text>
                  <Text color="#4B5563" fw={500}>
                    {formatDateTime(lastTestTime, APP_TIME_DATE_FORMAT)}
                  </Text>
                </Flex>
              )}
            </Flex>
          )}
        </Box>
      )}
      {renderModalTakeAnExam({
        exam,
        quizzs,
        onSubmitSuccess: refetchExamResult,
      })}
    </Fragment>
  );
};
