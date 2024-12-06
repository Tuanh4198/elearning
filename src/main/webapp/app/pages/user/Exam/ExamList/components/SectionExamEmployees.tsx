import React from 'react';
import { Box, Chip, Container, Flex, Grid, Loader, LoadingOverlay, Pagination, Text } from '@mantine/core';
import { ExamEmployeeStatusEnum } from 'app/shared/model/enumerations/exam-employee-status-enum.model';
import { ExamCard } from 'app/pages/user/Exam/ExamList/components/ExamCard';
import { useFetchExamEmployees } from 'app/pages/user/Exam/ExamList/hooks/useFetchExamEmployees';
import { Empty } from 'app/shared/components/Empty';

export const SectionExamEmployees = () => {
  const { isLoading, data, page, size, total, status, handleNextPage, handleNextStatus } = useFetchExamEmployees({
    withStatus: true,
  });

  return (
    <>
      <Text color="black" size="20px" fw="700" mb="20px">
        Kì thi của bạn
      </Text>
      <Box bg="white" p="20px" className="exam-in-progress-box">
        <Box pos="relative">
          <LoadingOverlay
            style={{ borderRadius: '8px' }}
            zIndex={10}
            visible={isLoading && data && data?.length > 0}
            loaderProps={{ children: <Loader /> }}
          />
          <Flex align="center" justify="flex-start" gap="8px" mb="20px">
            <Chip.Group multiple={false} value={status} onChange={handleNextStatus}>
              <Chip
                bg="transparent"
                color="#FAB005"
                styles={{ label: { padding: '5px 12px' } }}
                value={ExamEmployeeStatusEnum.NOT_COMPLETED}
                variant={status === ExamEmployeeStatusEnum.NOT_COMPLETED ? 'filled' : 'outline'}
              >
                <Text color={status === ExamEmployeeStatusEnum.NOT_COMPLETED ? 'white' : '#FAB005'}>Chưa hoàn thành</Text>
              </Chip>
              <Chip
                bg="transparent"
                color="#FA5252"
                styles={{ label: { padding: '5px 15px' } }}
                value={ExamEmployeeStatusEnum.EXPIRED}
                variant={status === ExamEmployeeStatusEnum.EXPIRED ? 'filled' : 'outline'}
              >
                <Text color={status === ExamEmployeeStatusEnum.EXPIRED ? 'white' : '#FA5252'}>Đã hết hạn</Text>
              </Chip>
              <Chip
                bg="transparent"
                color="#12B886"
                styles={{ label: { padding: '5px 12px' } }}
                value={ExamEmployeeStatusEnum.PASS}
                variant={status === ExamEmployeeStatusEnum.PASS ? 'filled' : 'outline'}
              >
                <Text color={status === ExamEmployeeStatusEnum.PASS ? 'white' : '#12B886'}>Đã hoàn thành</Text>
              </Chip>
            </Chip.Group>
          </Flex>
          <Grid>
            {isLoading && (!data || data?.length <= 0) ? (
              Array.from({ length: 4 }).map((_, i) => (
                <Grid.Col key={i} span={3}>
                  <ExamCard loading />
                </Grid.Col>
              ))
            ) : !data || data?.length <= 0 ? (
              <Container py={30}>
                <Empty description="Không có dữ liệu kì thi" />
              </Container>
            ) : (
              data?.map(item => (
                <Grid.Col key={item.id} span={3}>
                  <ExamCard canEnjoy exam={item.exam} />
                </Grid.Col>
              ))
            )}
          </Grid>
        </Box>
        {Math.ceil(total / size) > 1 && (
          <Flex justify="flex-end" mt="20px">
            <Pagination
              value={page}
              disabled={isLoading}
              withControls={false}
              hideWithOnePage
              size="sm"
              total={Math.ceil(total / size)}
              onChange={handleNextPage}
            />
          </Flex>
        )}
      </Box>
    </>
  );
};
