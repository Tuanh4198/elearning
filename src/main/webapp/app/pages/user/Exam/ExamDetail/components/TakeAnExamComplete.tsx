import React, { useMemo } from 'react';
import { Button, Flex, Image, Text } from '@mantine/core';
import { useNavigate } from 'react-router-dom';
import { AdminRoutes } from 'app/pages/admin/routes';
import { IExamEmployeeResult } from 'app/shared/model/exam-employee-result.model';
import { ExamMetafieldKey, IExam } from 'app/shared/model/exam.model';
import { calcDurationTimeFunc, parseNumber } from 'app/pages/user/Exam/utils';

export const TakeAnExamCompleted = ({ result, exam }: { result?: IExamEmployeeResult; exam?: IExam }) => {
  const navigate = useNavigate();

  const maxNumberOfTest = useMemo(
    () => Number(exam?.metafields?.filter(i => i.key === ExamMetafieldKey.max_number_of_test)?.[0]?.value || 0),
    [exam]
  );

  const totalNumberOfPoint = useMemo(
    () =>
      result?.numberOfCorrect && result?.numberOfQuestion ? parseNumber((result?.numberOfCorrect * 100) / result?.numberOfQuestion) : 0,
    [exam, result]
  );

  const calcDurationTime = useMemo(() => calcDurationTimeFunc(result?.startAt, result?.finishedAt), [result?.startAt, result?.finishedAt]);

  if (!result || !exam) return null;

  return (
    <Flex w="100%" align="center" justify="center" mt="80px">
      <Flex direction="column" gap="20px" w="345px">
        <Image w="80%" m="auto" src={'../../../../../content/images/exam-completed.svg'} alt="empty" />
        <Flex direction="column" align="center" justify="center" gap="8px">
          <Text color="#1F2A37" size="24px" fw="700" ta="center">
            Chúc mừng bạn đã <br />
            hoàn thành bài thi!
          </Text>
          <Text color="#4B5563" size="14px" fw="500">
            Số điểm đạt được
          </Text>
          <Text color="#12B886" size="24px" fw="700">
            {totalNumberOfPoint}/100
          </Text>
        </Flex>
        <Flex
          bg="white"
          style={{
            borderRadius: '16px',
            border: '2px solid #E5E7EB',
          }}
        >
          <Flex
            style={{ borderRight: '0.5px solid #E5E7EB' }}
            flex={1}
            px="20px"
            py="8px"
            align="center"
            justify="center"
            direction="column"
          >
            <Text color="#6B7280" size="12px" fw="500" mb="2px">
              Số câu đúng
            </Text>
            <Text color="#1F2A37" fw="600">
              {result.numberOfCorrect}/{result.numberOfQuestion}
            </Text>
          </Flex>
          <Flex
            style={{
              borderRight: '0.5px solid #E5E7EB',
              borderLeft: '0.5px solid #E5E7EB',
            }}
            flex={1}
            px="20px"
            py="8px"
            align="center"
            justify="center"
            direction="column"
          >
            <Text color="#6B7280" size="12px" fw="500" mb="2px">
              Số lần làm
            </Text>
            <Text color="#1F2A37" fw="600">
              {result?.numberOfTest}/{maxNumberOfTest}
            </Text>
          </Flex>
          <Flex
            style={{ borderLeft: '0.5px solid #E5E7EB' }}
            flex={1}
            px="20px"
            py="8px"
            align="center"
            justify="center"
            direction="column"
          >
            <Text color="#6B7280" size="12px" fw="500" mb="2px">
              Thời gian
            </Text>
            <Text color="#1F2A37" fw="600">
              {calcDurationTime}
            </Text>
          </Flex>
        </Flex>
        <Button onClick={() => navigate(AdminRoutes.DASHBOARD)} variant="filled" color="#2A2A86" size="lg" radius="16px">
          Về trang chủ
        </Button>
      </Flex>
    </Flex>
  );
};
